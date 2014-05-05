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

public class BigramDictionary {
	HashMap<String, Integer> dictfreq;
	HashSet<String> dictionary;
	ArrayList<String> list;
	
	BufferedReader reader; 
	BufferedWriter writer;
	
	String srcpath;
	String dstpath;
	
	final String[] stopword = {"a", "an", "the"};
	HashSet<String> stopset;
	
	public BigramDictionary(String srcpath, String dstpath) throws IOException {
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
			for (int i=1; i<words.length-1; i++) {
				
				/* don't count stopword and words started with non-character */
				if (stopset.contains(words[i]) || stopset.contains(words[i+1]) 
						|| !startWithChar(words[i]) || !startWithChar(words[i+1])) 
					continue;
				
				String bigram = words[i] + " " + words[i+1];
				if (dictionary.contains(bigram)) {
					int f = dictfreq.get(bigram);
					dictfreq.put(bigram, f + 1);
				}
				else {
					dictionary.add(bigram);
					dictfreq.put(bigram, 1);
				}
			}
		}
		
		/* delete redundant words in dictionary*/
		Iterator<String> iterator = dictionary.iterator();
		while (iterator.hasNext()) {
			String word = iterator.next();
			int frequency = dictfreq.get(word);
			if (frequency >= 5) {
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
	
	public static boolean startWithChar (String word) {
		if (word.length() == 0) return false;
		char c = word.charAt(0); 
		return ('a' <= c && c <= 'z') || ('A' <= c && c <= 'Z');
	}
	
	public static void main (String[] args) throws IOException {
		String src = "/u/chenqr/cs388/hw4/fuzzy_processed.txt";
		String dst = "/u/chenqr/cs388/hw4/bigram/BigramDictionary";
		
		BigramDictionary dictionary = new BigramDictionary(src, dst);
		dictionary.build();
		System.out.println("done.");
	}
}