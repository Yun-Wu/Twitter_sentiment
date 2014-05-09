import java.io.*;
import java.util.*;

public class treeClean {
    private static String input = null;
    private static String output = null;
    private static boolean tag = false;
    private static boolean binary = false;
 
	public static void main(String[] args) throws FileNotFoundException {
                
        for (int argIndex = 0; argIndex < args.length; ) {
                if (args[argIndex].equalsIgnoreCase("-input")) {
                        input = args[argIndex + 1];
                        argIndex += 2;
                } else if (args[argIndex].equalsIgnoreCase("-output")) {
                        output = args[argIndex + 1];
                        argIndex += 2;
                } else if (args[argIndex].equalsIgnoreCase("-tag")) {
                	    tag = true;
                	    argIndex ++;
                } else if (args[argIndex].equalsIgnoreCase("-binary")) {
                	    binary = true;
                	    argIndex ++;
                } else if (args[argIndex].equalsIgnoreCase("-help")) {
                        help();
                        System.exit(0);
                } else {
                        System.err.println("Unknown argument " + args[argIndex + 1]);
                        throw new IllegalArgumentException("Unknown argument " + args[argIndex + 1]);
                }
        }
        processTweet();
    }

    public static void processTweet() throws FileNotFoundException {
        Scanner sc = new Scanner (new File(input));
        PrintStream out = new PrintStream (output);
        while (sc.hasNextLine()) {
                String tweet = sc.nextLine();
                if (tweet.startsWith("(") && tweet.endsWith(")"))
                    out.println(tweet);  
        }
        sc.close();
    }


    public static void help() {
        System.err.println("Known command line arguments:");
        System.err.println("  -input <filename>: input file with sentiment tag");
        System.err.println("  -output <filename>: pre-processed to better support sentiment treebank");
        System.err.println("  -tag : with tag for golden output, by default false"); 
        System.err.println("  -binary : binary sentiment with only positive/negative, default false"); 
    }

}