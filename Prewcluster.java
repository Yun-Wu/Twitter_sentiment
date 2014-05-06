import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Prewcluster extends Preprocess{
	String inputpath = null;
	String outputpath = null;
	
	public Prewcluster(String input, String output) {
		this.inputpath = input;
		this.outputpath = output;
	}
	
	public static void main(String[] args) {
		String inputpath = "/u/ywu/nlp/final/codes/tweetCorpus.txt";
		String outputpath = "/u/ywu/nlp/final/trainingandtestdata//word_cluster1.txt";
		Prewcluster p = new Prewcluster(inputpath, outputpath);
		try {
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// preprocess the corpus to input files for word cluster algorithm
	// only contains tweets
	// change all urls to "URL"
	// leave out all the targets
	// remove hashtags
	public void process() throws IOException {
		File input = new File(inputpath);
		File output = new File(outputpath);

		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		FileWriter writer = new FileWriter(output);

		System.out.println("Preparing for word clustering");

		String line = null;
		while ((line = br.readLine()) != null) {
//			String[] tmp = line.split(",");
//			if (tmp.length < 6)
//				continue;
//			int i = line.indexOf(tmp[5].trim());
//			String tweet = line.substring(i + 1, line.length() - 1);
			
			int index = line.indexOf("\t");
			if ( index == -1)
				continue;
			String tweet = line.substring(index+1);
			tweet = deleteNonascii(tweet);
			tweet = filter(tweet);
			writer.write(tweet + "\n");
		}

		br.close();
		writer.close();

		System.out.println("Finished generating training files for word clustering");
	}

}
