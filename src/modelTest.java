import java.io.*;
import java.util.*;

/*
 * modelTest evaluates a sentiment model by comparing the result given by 
 * sentiment parser with the original label. Return the correctness in 
 * percentage. 
 *
 */



public class modelTest {
  	static enum Input {
    	HARD, SOFT
  	}


	public static final int EOF = -1;
	public static final int ERR = -2;
	public static final int POS = 0;
	public static final int NEG = 2;
	public static final int NET = 4;


	public static void main(String[] args) throws FileNotFoundException {
 		String fileG = null;
    	String fileP = null;
    	Input inputFormat = Input.HARD;

		 
  		for (int argIndex = 0; argIndex < args.length; ) {
      		if (args[argIndex].equalsIgnoreCase("-fileG")) {
        		fileG = args[argIndex + 1];
        		argIndex += 2;
      		} else if (args[argIndex].equalsIgnoreCase("-fileP")) {
        		fileP = args[argIndex + 1];
        		argIndex += 2;
      		} else if (args[argIndex].equalsIgnoreCase("-input")) {
        		inputFormat = Input.valueOf(args[argIndex + 1].toUpperCase());
        		argIndex += 2;
            } else if (args[argIndex].equalsIgnoreCase("-help")) {
        		help();
        		System.exit(0);
      		} else {
        		System.err.println("Unknown argument " + args[argIndex + 1]);
        		throw new IllegalArgumentException("Unknown argument " + args[argIndex + 1]);
      		}
    	}
		// result holder. 
		// 0, 1 postive correct/incorrect
		// 2, 3 negative correct/incorrect
		// 4, 5 netural correct/incorrect
		int[] result = new int[6];

		Scanner input = new Scanner(new File(fileG));
		Scanner parsed = new Scanner(new File(fileP));
         
        boolean inProcess = true;
        while (inProcess)  {
        	int val = checkOne(input, parsed);
        	if (val >= 0 && val < 6) 
        		result[val]++;
        	else
        		inProcess = false;
        }

        printSummary(result);
        input.close();
        parsed.close();
	}

	public static int checkOne(Scanner input, Scanner parsed) {

		// input file format:
		// positive		This movie is great. 
		int inputSentiment = ERR;
		String text = ""; 
		if (input.hasNextLine()) {
			String line = input.nextLine().trim();
			Scanner lsc = new Scanner (line);
			if (lsc.hasNext())
				inputSentiment = getSentiment(lsc.next());
			if (lsc.hasNextLine())
				text = lsc.nextLine().trim();
			lsc.close();
		}
		else
			return EOF;


        // parsed file format:
        // This movie is great.
        // positive
		int parsedSentiment = ERR; 
		String pText = "";

        do {
			if (parsed.hasNextLine()) {
				pText = parsed.nextLine().trim();
				if (parsed.hasNextLine()) {
					parsedSentiment = getSentiment(parsed.nextLine());
				}
			}
			else 
				return EOF;
		} while (!text.endsWith(pText));
    
        //  DEBUG
	    //	System.out.println(text);
	    //	System.out.println(pText);


        // System.out.println(inputSentiment + "/" + parsedSentiment);

		return inputSentiment + (inputSentiment == parsedSentiment ? 0 : 1);

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


	public static void printSummary(int[] result) {
		int totalPos = result[0] + result[1];
		if (totalPos != 0)
			System.out.println("Postive: " + result[0] + "/" + totalPos + "\t=\t" + 100.0 * result[0] / totalPos);
		int totalNeg = result[2] + result[3];
		if (totalNeg != 0)
			System.out.println("Negative: " + result[2] + "/" + totalNeg + "\t=\t" + 100.0 * result[2] / totalNeg);
		int totalNet = result[4] + result[5];
		if (totalNet != 0)
			System.out.println("Netrual: " + result[4] + "/" + totalNet + "\t=\t" + 100.0 * result[4] / totalNet);
        int total = 0, totalCor = 0;
        for (int i = 0; i < 6; i++) {
        	total += result[i];
        	if (i % 2 == 0)
        		totalCor += result[i];
        }
		System.out.println("Total: " + totalCor + "/" + total + "\t=\t" + 100.0 * totalCor / total);
	}


    public static void help() {
    	System.err.println("Known command line arguments:");
    	System.err.println("  -fileG <filename>: Annotated golden file with sentiment information");
    	System.err.println("  -fileP <filename>: Parsed sentiment file to be evalauted");
    	System.err.println("  -input <format>: Which format to parsed file, Hard [positive/negative] or Soft[sentiment score]");
  }

}


















