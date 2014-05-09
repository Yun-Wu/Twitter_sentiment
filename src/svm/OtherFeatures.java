import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OtherFeatures {
	BufferedReader reader; 
	BufferedWriter writer;
	
	public OtherFeatures (String src, String dst) throws IOException {
		reader = new BufferedReader(new FileReader(src));
		writer = new BufferedWriter(new FileWriter(dst));
	}
	
	public void process () throws IOException {
		String line;
		int positive = 0, negative = 0;
		
		while ( (line = reader.readLine()) != null) {
			String words[] = line.split(" ");
			/* number of number tokens */
			int number = 0; 
			/* number of url tokens */
			int url = 0;
			for (int i=0; i<words.length; i++) {
				/* num */
				if (isNum(words[i])) number++;
				/* url */
				if (isURL(words[i])) url++;
			}
			
			if (words[0].equals("positive")) {
				writer.write("1 ");
				positive++;
			}
			else if (words[0].equals("negative")) {
				writer.write("-1 ");
				negative++;
			}
			else continue;
			
			/* write down features */
			int index = 1;
			writer.write(index++ + ":" + number++);
			writer.append(' ');
			writer.write(index++ + ":" + url);
			
			writer.newLine();
		}
		
		System.out.println("positive: " + positive);
		System.out.println("negative: " + negative);
		System.out.println((float)positive / (positive + negative));
		
		reader.close();
		writer.close();
	}
	
	
	private boolean isNum(String word) {
		if (word.length() == 0) return false;
		char c = word.charAt(0);
		
		return '0'<=c && c<='9';
	}
	
	private boolean isURL(String word) {
		word = word.toLowerCase();
		
		return word.equals("url");
	}
	
	
	public static void main (String[] args) throws IOException {
		String src = "/u/chenqr/cs388/hw4/fuzzy_processed.txt";
		String dst = "/u/chenqr/cs388/hw4/feature/OtherFeature";
		OtherFeatures other = new OtherFeatures(src, dst);
		
		other.process();
		System.out.println("done.");
	}
	
}