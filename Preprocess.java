import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Preprocess {

	public Preprocess(){
		// TODO create test file

	}

	public static void main(String[] args) {
		try {
			String inputpath = "/u/ywu/nlp/final/trainingandtestdata/training.1600000.processed.noemoticon.csv";
			String wordclusterpath = "/u/ywu/nlp/final/trainingandtestdata//word_cluster.txt";
			prewordcluster(inputpath, wordclusterpath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	//preprocess the corpus to input files for word cluster algorithm
	//only contains tweets
	//change all urls to "URL"
	//leave out all the targets
	//remove hashtags
	private static void prewordcluster(String inputpath, String outpath) throws IOException {
		File input = new File(inputpath);
		File output = new File(outpath);

		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		FileWriter writer = new FileWriter(output);

		System.out.println("Preprocess for word clustering");

		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tmp = line.split(",");
			if (tmp.length < 6)
				continue;
			int i = line.indexOf(tmp[5].trim());
			String tweet = line.substring(i+1, line.length() - 1);
			tweet = filter(tweet);
			writer.write(tweet+"\n");
		}

		br.close();
		writer.close();
		
		System.out.println("Finished preprocessing for word clustering");
	}

	public static String filter(String s) {
		//replace all urls with URL
		s = s.replaceAll("(?i)(?:https?|ftps?)://[\\w/%.-]+", "URL");
		
		//replace all html entities
		String[] html = {"&ndash;", "&quot;", "&nbsp;", "&amp;", "&gt;", "&lt;"};
		String[] ascii = {"-", "\"", " ", "&", ">", "<"};
		
		for(int i = 0; i < html.length; i++){
			s = s.replace(html[i], ascii[i]);
		}
		
		String[] tmp = s.split(" ");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tmp.length; i++) {
			// check if it's a target
			if (tmp[i].startsWith("@")){
				buffer.append("Target");
				while(i < tmp.length && tmp[i].startsWith("@")) i++;
				if(i == tmp.length) break;
			}
			// check if it's a hashtag
			if (tmp[i].startsWith("#") && tmp[i].length() > 1
					&& isLetter(tmp[i].charAt(1))) {
				buffer.append(" " + tmp[i].substring(1));
				continue;
			}
			buffer.append(tmp[i]+" ");

		}
		return buffer.toString();
	}
	
	private static boolean isLetter(char c){
		return (c>='a' && c<='z') || (c>='A' && c<='Z');
	}
}
