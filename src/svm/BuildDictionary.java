import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class BuildDictionary {
	HashMap<String, Integer> dictfreq;
	HashSet<String> dictionary;
	ArrayList<String> list;
	
	BufferedReader reader; 
	BufferedWriter writer;
	
	String srcpath;
	String dstpath;
	
	final String[] stopword = {"a", "an", "the"};
	HashSet<String> stopset;
	
	public BuildDictionary(String srcpath, String dstpath) throws IOException {
		this.srcpath = srcpath;
		this.dstpath = dstpath;
		
		dictfreq = new HashMap<String, Integer>();
		dictionary = new HashSet<String>();
		
		list = new ArrayList<String>();
		
		reader = new BufferedReader(new FileReader(srcpath));
		writer = new BufferedWriter(new FileWriter(dstpath));
		
		stopset = new HashSet<String>(); 
		for (String k : stopword) {
			stopset.add(k);
		}
	}
	
	
	public void build() throws IOException {
		String line;
		/* count the frequency of each word */
		while ((line = reader.readLine()) != null) {
			String[] words = line.split(" "); 
			for (int i=1; i<words.length; i++) {
				if (dictionary.contains(words[i])) {
					int f = dictfreq.get(words[i]);
					dictfreq.put(words[i], f + 1);
				}
				else {
					dictionary.add(words[i]);
					dictfreq.put(words[i], 1);
				}	
			}		
		}
		
		/* delete redundant words in dictionary*/
		Iterator<String> iterator = dictionary.iterator();
		while (iterator.hasNext()) {
			String word = iterator.next();
			int frequency = dictfreq.get(word);
			if (frequency >= 5 && !stopset.contains(word)) {
				list.add(word);
			}
		}
		
		/* sort dictionary */
		Collections.sort(list);
		
		/* save dictionary */
		int i = 0;
		for (String word : list) {
			writer.write(i++ + "  " + word + "  " + dictfreq.get(word));
			writer.newLine();
		}
		
		writer.close();
		reader.close();
	}
	
	public static boolean startWithNum (String word) {
		char c = word.charAt(0); 
		return ('0' <= c && c <= '9');
		
	}
	
	public static void main (String[] args) throws IOException {
		String src = "/u/chenqr/cs388/hw4/fuzzy_processed.txt";
		String dst = "/u/chenqr/cs388/hw4/dictionary.txt";
		
		BuildDictionary dictionary = new BuildDictionary(src, dst);
		dictionary.build();
	}
}