import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class splitfile {
	BufferedReader reader1; // count the number of lines  
	BufferedReader reader2; // to split files
	BufferedWriter writer1;
	BufferedWriter writer2;
	double ratio;
	
	public splitfile (String src, String dst1, String dst2, double ratio) throws IOException {
		reader1 = new BufferedReader(new FileReader(src));
		reader2 = new BufferedReader(new FileReader(src));
		writer1 = new BufferedWriter(new FileWriter(dst1));
		writer2 = new BufferedWriter(new FileWriter(dst2));
		
		this.ratio = ratio;
	}
	
	public void process() throws IOException {
		int total = 0, positive = 0, negative = 0;
		
		String line;
		/* count the number of each category */
		while ((line = reader1.readLine()) != null) {
			total++;
			String[] words = line.split(" ");
			if (words[0].equals("1")) positive++;
			else if (words[0].equals("-1")) negative++;
			
		}
		
		int pn = (int)(positive * ratio), nn = (int)(negative * ratio);
		
		System.out.println("total: " + total);
		System.out.println("positive: " + positive + " pn: " + pn);
		System.out.println("negative: " + negative + " nn: " + nn);

		
		/* split train and test files */
		while ((line = reader2.readLine()) != null) { 
			String word = line.substring(0, 1);
			if (word.equals("1")) {
				if (pn >= 0) {
					writer1.write(line);
					writer1.newLine();
					pn--;
				}
				else {
					writer2.write(line);
					writer2.newLine();
				}
				
			}
			else {
				if (nn >= 0) {
					writer1.write(line);
					writer1.newLine();
					nn--;
				}
				else {
					writer2.write(line);
					writer2.newLine();
				}
				
			}
		}
			
		reader1.close();
		reader2.close();
		writer1.close();
		writer2.close();
	}
	
	
	public static void main (String[] args) throws IOException {
		String src = "/u/chenqr/cs388/hw4/feature";
		String dst1 = "/u/chenqr/cs388/hw4/train";
		String dst2 = "/u/chenqr/cs388/hw4/test";
		
		splitfile sf  = new splitfile(src, dst1, dst2, 0.9);
		sf.process();
		
		System.out.println("done.");
	}
	
	
}