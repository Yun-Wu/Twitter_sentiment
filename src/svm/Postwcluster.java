import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Postwcluster extends Preprocess {
	static HashMap<String, String> word2path = new HashMap<String, String>();
	static HashMap<String, String> path2word = new HashMap<String, String>();

	String wclusterpath = null;
	String inputpath = null;
	String outputpath = null;

	public Postwcluster(String wclusterpath, String inputpath, String outputpath) {
		this.wclusterpath = wclusterpath;
		this.inputpath = inputpath;
		this.outputpath = outputpath;
	}

	public static void main(String[] args) {
		String wclusterpath = "/u/ywu/nlp/final/condor/word_cluster-c1000-p1.out/paths";
		String wclusteroutpath = "/u/ywu/nlp/final/trainingandtestdata/clusters.txt";
		String inputpath = "/u/ywu/nlp/final/trainingandtestdata/training.1600000.processed.noemoticon.csv";
		String outputpath = "/u/ywu/nlp/final/trainingandtestdata/train.txt";

		Postwcluster p = new Postwcluster(wclusterpath, inputpath, outputpath);
		try {
			p.generateClusters();
			p.printClusters(wclusteroutpath);
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void generateClusters() throws IOException {
		HashMap<String, Integer> path2freq = new HashMap<String, Integer>();
		File input = new File(inputpath);
		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split("\\t");
			String path = tmp[0];
			String word = tmp[1];
			int freq = Integer.parseInt(tmp[2].trim());
			if (tmp.length < 3)
				continue;
			word2path.put(word, path);
			if (path2freq.get(path) == null || path2freq.get(path) < freq) {
				path2freq.put(path, freq);
				path2word.put(path, word);
			}
		}

		br.close();
	}

	void printClusters(String wclusteroutpath) throws IOException {
		File output = new File(wclusteroutpath);
		FileWriter writer = new FileWriter(output);

		for (String path : path2word.keySet()) {

			writer.write(path + ": " + path2word.get(path));

			for (String word : word2path.keySet()) {
				if (word2path.get(word).equals(path))
					writer.write(" " + word);
			}
			writer.write("\n");
		}
		writer.close();
	}
	
	public void process() throws IOException {
		generateClusters();
		
		File input = new File(inputpath);
		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		
		File output = new File(outputpath);
		FileWriter writer = new FileWriter(output);
		
		System.out.println("Preprocess for sentiment training with word clusters");
		
		String line = null;
		int num = 0;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(",");
			if (tmp.length < 6)
				continue;
			int i = line.indexOf(tmp[5].trim());
			String tweet = line.substring(i + 1, line.length() - 1);
			// guarantee that s only contains ASCII code
			if (!isAllAscii(line))
				continue;
			tweet = filter(tweet);
			tweet = clustering(tweet);
			if(tmp[0].charAt(1) == '0') writer.write("negative\t");
			else if(tmp[0].charAt(1) == '4') writer.write("positive\t");
			else writer.write("neutral\t");
			writer.write(tweet + "\n");
			num++;
		}

		System.out.println("Preprocessed " + num + " tweets.");
		
		br.close();
		writer.close();
	}

	// split tweets with different sentiment to seperate files
	// for sentiment treebank training
	// tentative to change
	public void splitFiles(String inputpath, String pospath,
			String netpath, String negpath) throws IOException {
		File input = new File(inputpath);
		File posfile = new File(pospath);
		File netfile = new File(netpath);
		File negfile = new File(negpath);

		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);

		FileWriter pos = new FileWriter(posfile);
		FileWriter net = new FileWriter(netfile);
		FileWriter neg = new FileWriter(negfile);

		int posnum = 0;
		int netnum = 0;
		int negnum = 0;

		System.out.println("Preprocess for sentiment training");

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(",");
			if (tmp.length < 6)
				continue;
			int i = line.indexOf(tmp[5].trim());
			String tweet = line.substring(i + 1, line.length() - 1);
			// guarantee that s only contains ASCII code
			if (!isAllAscii(line))
				continue;
			tweet = filter(tweet);
			tweet = clustering(tweet);
			char c = tmp[0].charAt(1);
			switch (c) {
			case '0':
				neg.write(c + ": " + tweet + "\n");
				negnum++;
				break;
			case '2':
				net.write(c + ": " + tweet + "\n");
				netnum++;
				break;
			case '4':
				pos.write(c + ": " + tweet + "\n");
				posnum++;
				break;
			}
		}

		System.out
				.println("Preprocessed " + negnum + " negative tweets, "
						+ netnum + " neutral tweets and " + posnum
						+ " positive tweets");
		br.close();
		pos.close();
		net.close();
		neg.close();
	}

	//replace the word in the same cluster by the most frequent one
	public static String clustering(String s) {
		StringBuffer buffer = new StringBuffer();

		String[] tmp = s.split(" ");

		for (int i = 0; i < tmp.length; i++) {
			String word = null;
			// TODO realize fuzzword
			// if (word2path.get(tmp[i]) == null) tmp[i] = fuzzword(tmp[i]);
			word = path2word.get(word2path.get(tmp[i]));
			buffer.append(word + " ");
		}

		return buffer.toString();
	}
}
