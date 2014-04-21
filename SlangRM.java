import java.io.*;
import java.util.*;

public class SlangRM {
	public static void main(String[] args) throws FileNotFoundException {
		 if (args.length != 3) {
		 	System.out.println("Usage: SlangRm [dictName] [inputName] [outputName]");
		 	return;
		 }
		 HashMap<String, String> dict = buildDict(args[0]);
		 slangRemove(dict, args);

        // For debug 
		/* 
		 Set<Map.Entry<String, String>> dictSet = dict.entrySet();
		 for (Map.Entry<String, String> item : dictSet)
			  System.out.println(item.getKey() + " : " + item.getValue());	 	
		*/

	}

	public static HashMap<String, String> buildDict(String dictName) throws FileNotFoundException {
		  HashMap<String, String> dict = new HashMap<String, String>(6000);
		  Scanner sc = new Scanner(new File(dictName));
		  while (sc.hasNextLine()) {
		  	String line = sc.nextLine();
		  	Scanner lineSc = new Scanner (line);
		  	if (lineSc.hasNext()) {
		  		String key = lineSc.next().toLowerCase();
		  		if (lineSc.hasNext() && lineSc.next().equals("-")) {
		  			if (lineSc.hasNextLine()) {
		  			    String value = lineSc.nextLine().trim();
		  				dict.put(key, value);
		  		    }
		  		} 
		    }
		    lineSc.close();
	    }
	    sc.close();
	    return dict;
    }

    public static void slangRemove (HashMap<String, String> dict, String[] args) throws FileNotFoundException {
    	Scanner sc = new Scanner (new File(args[1]));
    	PrintStream out = new PrintStream (args[2]);
    	while (sc.hasNextLine()) {
    		String tweet = sc.nextLine();
    		Scanner tweetSc = new Scanner (tweet);
    		while (tweetSc.hasNext()) {
    			String token = tweetSc.next().toLowerCase();
    			int len = token.length();
    			while (len > 0 && isPunc(token.charAt(len - 1))) {
    				len--;
    			}
    			String suf = token.substring(len, token.length());
    			token = token.substring(0, len);
    			if (dict.containsKey(token)) 
    				out.print(dict.get(token));
    			else 
    				out.print(token);

    			out.print(suf + " ");
    		}
    		out.println();
    		tweetSc.close();
    	}
        sc.close();
        out.close();
    }

    public static boolean isPunc (char ch) {
    	String punc = ".:;,?!~";
    	return punc.indexOf(ch) >= 0;
    }


}