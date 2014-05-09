import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class FuzzyPreprocess extends Preprocess {
	String inputpath = null;
	String outputpath = null;
	String slangpath = null;

	public FuzzyPreprocess(String input, String output, String slang) {
		this.inputpath = input;
		this.outputpath = output;
		this.slangpath = slang;
	}
		//
	public void process() throws IOException {
		File input = new File(inputpath);
		File output = new File(outputpath);

		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		FileWriter writer = new FileWriter(output);

		HashMap<String, String> dict = SlangRM.buildDict(slangpath);

		System.out.println("Preparing for sentiment classification");

		String line = null;
		while ((line = br.readLine()) != null) {

//			 String[] tmp = line.split(",");
//			 if (tmp.length < 6)
//			 continue;
//			 int i = line.indexOf(tmp[5].trim());
//			 String tweet = line.substring(i + 1, line.length() - 1);
//			
//			 //guarantee that there are only contains ASCII codes
//			 if(!isAllAscii(tweet)) continue;
//			
//			 String tag = null;
//			 if(tmp[0].charAt(1) == '0') tag = "negative\t";
//			 else if(tmp[0].charAt(1) == '4') tag = "positive\t";
//			 else tag = "neutral\t";

			int index = line.indexOf("\t");
			if (index == -1)
				continue;
			String tweet = line.substring(index + 1);
			String tag = line.substring(0, index + 1);
			tweet = deleteNonascii(tweet);

			tweet = filter(tweet);
			tweet = fuzzy(tweet, dict);

			writer.write(tag);
			writer.write(tweet + "\n");
		}

		br.close();
		writer.close();

		System.out
				.println("Finished generating training files for sentiment classification");
	}

	protected String fuzzy(String s, HashMap<String, String> dict)
			throws FileNotFoundException {

		s = s.replaceAll("(.)\\1{2,}", "$1$1$1");// omggggggg->omggg

		// replace slangs according to slang dict

		Scanner tweetSc = new Scanner(s);
		StringBuffer buffer = new StringBuffer();

		while (tweetSc.hasNext()) {
			String token = tweetSc.next().toLowerCase();
			if (dict.containsKey(token))
				buffer.append(dict.get(token) + " ");
			else
				buffer.append(token + " ");
		}
		tweetSc.close();
		return buffer.toString();
	}

	public static void main(String[] args) {

		String inputpath = "/u/ywu/nlp/final/trainingandtestdata/tweetCorpus.txt";
		String outputpath = "/u/ywu/nlp/final/trainingandtestdata/fuzzy_processed.txt";
		String slangpath = "/u/ywu/workspace/Preprocess/src/slangDic";

		FuzzyPreprocess p = new FuzzyPreprocess(inputpath, outputpath,
				slangpath);

		try {
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
