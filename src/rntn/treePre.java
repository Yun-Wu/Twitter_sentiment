import java.io.*;
import java.util.*;

public class treePre {
    public static final int ERR = -2;
    public static final int POS = 0;
    public static final int NEG = 2;
    public static final int NET = 4;

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
        processTweet(input, output);
    }

    public static void processTweet(String input, String output) throws FileNotFoundException {
        Scanner sc = new Scanner (new File(input));
        PrintStream out = new PrintStream (output);
        while (sc.hasNextLine()) {
                String tweet = sc.nextLine();
                Scanner tweetSc = new Scanner (tweet);
                int sentiment = getSentiment(tweetSc.next());
                if (binary && sentiment == NET) {
                    tweetSc.close();
                    continue;
                }
                if (tag) {
                    if (sentiment == POS)
                        out.print("positive\t");
                    else if (sentiment == NEG)
                        out.print("negative\t");
                    else if (sentiment == NET)
                        out.print("neutral\t");
                    else 
                        out.print("error\t");
                }

                String token = "";
                while (tweetSc.hasNext()) {
                        token = tweetSc.next().toLowerCase();
                        if (!isDelete(token)) {
                            out.print(token + " ");
														//output += token + " ";
                        }
                }
                String end = ".?!";
								token = token.trim();
								int index = token.length() - 1;
								if (index > 0) {
										char ch = token.charAt(index);
										if (end.indexOf(ch) < 0)
														out.print(".");
								}

                out.println("");  // add the period at the very end
                tweetSc.close();
        }
        sc.close();
    }

    public static int getSentiment(String sentiment) {
        sentiment = sentiment.trim().toLowerCase();
        if (sentiment.contains("positive"))
            return POS;
        else if (sentiment.contains("negative"))
            return NEG;
        else 
            return NET;
    }

    public static boolean isDelete(String token) {
        return token.equalsIgnoreCase("url");
    }


    public static void help() {
        System.err.println("Known command line arguments:");
        System.err.println("  -input <filename>: input file with sentiment tag");
        System.err.println("  -output <filename>: pre-processed to better support sentiment treebank");
        System.err.println("  -tag : with tag for golden output, by default false"); 
        System.err.println("  -binary : binary sentiment with only positive/negative, default false"); 
    }

}