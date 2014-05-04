import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class combineFeature {
	BufferedReader reader1;
	BufferedReader reader2;
	BufferedWriter writer;
	
	
	public combineFeature (String src1, String src2, String dst) throws IOException {
		reader1 = new BufferedReader(new FileReader(src1));
		reader2 = new BufferedReader(new FileReader(src2));
		writer = new BufferedWriter(new FileWriter(dst));
	}
	
	public void process() throws IOException {
		String line1, line2;
		boolean firstline = true;
		int index = -1;
		
		while ((line1 = reader1.readLine()) != null) {
			line2 = reader2.readLine();
			if (line2 == null) {
				System.err.println("ERROR");
				return;
			}
			
			if (firstline) {
				String[] words = line1.split(" ");
				String lastword = words[words.length-1];
				String[] tok = lastword.split(":");
				
				index = Integer.parseInt(tok[0]);
				firstline = false;
			}
			
			writer.write(line1);
//			writer.append(' ');
			
			String[] words = line2.split(" ");
			int start = index;
			
			for (int i=0; i<words.length; i++) {
				String[] tok = words[i].split(":");
				if (tok.length > 1) {
					writer.write(++start + ":" + tok[1]);
					writer.append(' ');
				}
			}
			
			writer.newLine();
		}
		
		reader1.close();
		reader2.close();
		writer.close();
	}
	
	public static void main (String[] args) throws IOException {
		String src1 = "/u/chenqr/cs388/hw4/feature/binaryFeature";
		String src2 = "/u/chenqr/cs388/hw4/bigram/BinaryFeature";
		String dst = "/u/chenqr/cs388/hw4/feature/unigram-bigram";
		
		combineFeature cf = new combineFeature(src1, src2, dst);
		cf.process();
		
		System.out.println("done.");
		
	}
}