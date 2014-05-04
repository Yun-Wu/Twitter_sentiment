import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class extractfeature {
	String srcpath;
	String dstpath;
	String dictpath;
	
	BufferedReader reader; 
	BufferedWriter writer;
	BufferedReader dictloader;
	
	HashMap<String, Integer> dictionary;
	
	int totalLines = 0;
	
	public extractfeature(String src, String dst, String dictpath) throws IOException {
		this.srcpath = src;
		this.dstpath = dst;
		this.dictpath = dictpath;
		
		dictionary = new HashMap<String, Integer>();
	}
	
	public void binaryFeature() throws IOException {
		
		loadFile();
		/* load dictionary */
		loadDictionary();
		
		/* extract feature */
		String line;
		Set<String> dict = dictionary.keySet();
		while ( (line = reader.readLine()) != null) {
			
			String[] words = line.split(" ");
			HashSet<String> t = new HashSet<String>();
			for (int i=1; i<words.length; i++) {
				t.add(words[i]);
			}
			
			if (words[0].equals("positive")) writer.write("1 ");
			else if (words[0].equals("negative")) writer.write("-1 ");
			else continue;
			
			int index = 1;
			for (String word : dict) {
				writer.write(index++ + ":");
				if (t.contains(word)) {
					writer.write("1");
				}
				else writer.write("0");
				
				writer.append(' ');
			}
			writer.append('\n');
		}
		
		writer.close();
		reader.close();
	}
	
	public void freqFeature() throws IOException {
		loadFile();
		/* load dictionary */
		String line;
		loadDictionary();
		
		/* extract feature */
		Set<String> dict = dictionary.keySet();
		while ( (line = reader.readLine()) != null) {

			String[] words = line.split(" ");
			HashMap<String, Integer> t = new HashMap<String, Integer>();
			for (int i=1; i<words.length; i++) {
				if (!t.containsKey(words[i])) {
 					t.put(words[i], 1);
				}
				else {
					int count = t.get(words[i]);
					t.put(words[i], count+1);
				}
			}
			
			if (words[0].equals("positive")) writer.write("1 ");
			else if (words[0].equals("negative")) writer.write("-1 ");
			else continue;
			
			int index = 1;
			for (String word : dict) {
				writer.write(index++ + ":");
				if (t.containsKey(word)) {
					writer.write(t.get(word).toString());
				}
				else writer.write("0");
				
				writer.append(' ');
			}
			writer.append('\n');
		}
		
		writer.close();
		reader.close();
		
	}
	
	public void tfidfFeature() throws IOException {
		loadFile();
		
		/* use this to count the number of file in which one word appears */
		HashMap<String, Integer> idf = new HashMap<String, Integer>();
		
		reader = new BufferedReader(new FileReader(this.srcpath));
		String line;
		while ((line = reader.readLine()) != null) {
			
			String[] words = line.split(" ");
			HashSet<String> wordInLine = new HashSet<String>();
			totalLines++;
					
			for (int i=0; i<words.length; i++) {
				
				/* idf */
				if (!wordInLine.contains(words[i])) {
					wordInLine.add(words[i]);
					
					if (idf.containsKey(words[i])) {
						int count = idf.get(words[i]);
						idf.put(words[i], count + 1);
					}
					else {
						idf.put(words[i], 1);
					}
				}
			}
		}
		
		/* load dictionary */
		loadDictionary();
		Set<String> dict = dictionary.keySet();
		/* to calculate tf-idf *
		
		Iterator<String> it = dict.iterator();
		while (it.hasNext()) {
			String word = it.next();
			
			double res = (tf.get(word) / totalWords) * Math.log(totalLines / idf.get(word)); 
			tfidf.put(word, res);	
		}
		*/
		reader = new BufferedReader(new FileReader(srcpath)); 
		/* write tf-idf into file */
		while ( (line = reader.readLine()) != null) {
			
			String[] words = line.split(" ");
			HashMap<String, Integer> t = new HashMap<String, Integer>();
			for (int i=1; i<words.length; i++) {
				if (t.containsKey(words[i]))
					t.put(words[i], t.get(words[i])+1);
				else 
					t.put(words[i], 1);
			}
			
			if (words[0].equals("positive")) writer.write("1 ");
			else if (words[0].equals("negative")) writer.write("-1 ");
			else continue;
			
			int index = 1;
			for (String word : dict) {
				writer.write(index++ + ":");
				if (t.containsKey(word)) {
					Double res = ((double)t.get(word) / words.length) * Math.log(totalLines / idf.get(word));
					writer.write(res.toString());
				}
				else writer.write("0");
				
				writer.append(' ');
			}
			writer.append('\n');
		}
		
		writer.close();
		reader.close();
	}
	
	private void loadDictionary() throws NumberFormatException, IOException {
		dictloader = new BufferedReader(new FileReader(dictpath));
		if (dictionary.isEmpty()) {
			String line;
			while( (line = dictloader.readLine()) != null) {
				String[] words = line.split("  "); 
				/* ignore positive and negative */
				dictionary.put(words[1], Integer.parseInt(words[0]));
			}
		}
		dictloader.close();
	}
	
	private void loadFile() throws IOException {
		reader = new BufferedReader(new FileReader(srcpath));
		writer = new BufferedWriter(new FileWriter(dstpath));
	}
	
	public static void main (String[] args) throws IOException {
		String src = "/u/chenqr/cs388/hw4/fuzzy_processed.txt";
		String dst = "/u/chenqr/cs388/hw4/feature/binaryFeature";
		String dictpath = "/u/chenqr/cs388/hw4/dictionary";
		
		extractfeature ef = new extractfeature(src, dst, dictpath);
		ef.binaryFeature();
//		ef.freqFeature();
//		ef.tfidfFeature();
		System.out.println("done.");
	}
	
}