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

  	public static boolean binary = false; 

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
        	} else if (args[argIndex].equalsIgnoreCase("-binary")) {
        		binary = true;
        		argIndex++;
            } else if (args[argIndex].equalsIgnoreCase("-help")) {
        		help();
        		System.exit(0);
      		} else {
      			help();
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
				int count = 0;
			  System.out.println(count);
        while (inProcess)  {
        	int val = checkOne(input, parsed, inputFormat);
        	if (val >= 0 && val < 6) 
        		result[val]++;
        	else
        		inProcess = false;
					System.out.println(count++);
        }

        printSummary(result);
        input.close();
        parsed.close();

        // Unit test
        /*
        String l1 = "(0 (1 (2 (3 (4 (5 Was) (6 (7 thinking) (8 (9 Everything) (10 (11 Is) (12 Awesome))))) (13 (14 was) (15 (16 (17 a) (18 lock)) (19 (20 for) (21 (22 2015) (23 (24 Best) (25 (26 Original) (27 Song)))))))) (28 but)) (29 (30 (31 (32 this) (33 (34 @JanelleMonae) (35 track))) (36 (37 from) (38 (39 Rio) (40 2)))) (41 (42 is) (43 (44 pretty) (45 (46 damn) (47 infectious)))))) (48 .))";
       	System.out.println(getLineCount(l1) + "/" + 48); 

       	String l2 = "(0 (1 (2 GOOD) (3 (4 JOB) (5 (6 GUYS) (7 (8 RIO) (9 2))))) (10 (11 (12 WAS) (13 AWESOME)) (14 .)))";
       	System.out.println(getLineCount(l2) + "/" + 14); 
       	*/
 
	}

	public static int checkOne(Scanner input, Scanner parsed, Input inputFormat) {

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


       
		int parsedSentiment = ERR; 
		String pText = "";
		double[] softVector = new double[5]; 

		switch (inputFormat) {
		    case HARD:
		    // parsed file format:
            // This movie is great.
            // positive
        		do {
					if (parsed.hasNextLine()) {
						String pLine = parsed.nextLine();
						while(!pLine.startsWith("  ")) {
							pText = pLine.trim();
							if (parsed.hasNextLine())
						        pLine = parsed.nextLine();

						}
						parsedSentiment = getSentiment(pLine);
					}
					else 
						return EOF;
				} while (!text.endsWith(pText));
				break;
			case SOFT:
			    do {
						String pLine = "";
						do {
							pText = pLine;
							if (parsed.hasNextLine()) 
								pLine = parsed.nextLine();
							else 
								return EOF;
						} while (!pLine.trim().startsWith("(0"));

                        
						System.out.println("\noriginal Text: ...");
						System.out.println(text);
						System.out.println("parsed Text: ...");
						System.out.println(pText);
						System.out.println("endWith?: " + text.endsWith(pText));
						System.out.println('\n');
						

						parseProb(pLine, parsed, softVector);
				    } while (!text.endsWith(pText));
				parsedSentiment = getProbSentiment(softVector);
				break;
			default:
				System.err.println("Incorrect inputFormat...");
				break;
		}

    
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

	public static int getProbSentiment(double[] prob) {
		if (prob == null || prob.length != 5) return ERR;
		double[] triVec = new double[3];
		triVec[0] = prob[0] + prob[1];  // NEG
		triVec[1] = prob[2];            // NET
		triVec[2] = prob[3] + prob[4];  // POS
		
		// generate only binary output
		if (binary) {
			triVec[0] += triVec[1] * 0.2;
			triVec[2] += triVec[1] * 0.8;
			triVec[1] = 0;
		}
		else {
		  triVec[0] = prob[0] + prob[1] * 0.6;  // NEG
		  triVec[1] = prob[1] * 0.4 + 0.8 * prob[2];            // NET
		  triVec[2] = prob[3] + prob[4] + 0.2*prob[2]; 

		}

		double max = 0; 
		int maxIndex = 0;
		for (int i = 0; i < 3; i++) {
			if (triVec[i] > max) {
				max = triVec[i];
				maxIndex = i;
			}
		}
		if (maxIndex == 0) return NEG;
		else if (maxIndex == 1) return NET;
		else if (maxIndex == 2) return POS;
		else return ERR;
	}


	public static void parseProb (String treeLine, Scanner input, double[] prob) {
		// get rid of the tree line such as 
		//  (0 (1 I) (2 (3 (4 am) (5 happy)) (6 .)))
	    System.out.println(treeLine);
		int lineCount = getLineCount(treeLine);   // get the very last number in the tree line   

		// read of the root probablity
		//  0:  0.0161  0.0371  0.1325  0.6179  0.1964
		if (input.hasNextLine()) {
			String line = input.nextLine();
			Scanner lsc = new Scanner (line);
			if (lsc.hasNext()) {
				String token = lsc.next();
				if (token.equals(0 + ":")) {
					for (int i = 0; i < 5; i++) {
						if (lsc.hasNextDouble()) {
							prob[i] += lsc.nextDouble();
						}
					}
				}
			} 
			lsc.close();
		}

		for (int i = 1;  i <= lineCount; i++) {
			if (input.hasNextLine()) {
				String line = input.nextLine().trim();
				if (!line.startsWith(i + ":"))
					System.err.println("Parse error, line don't start with :" + i + ":");
			}	
			else {
					System.err.println("Parse error, expecting " + lineCount + " lines, " + 
					                   "stoped at " + i); 
			}
		}
	}

    
	public static int getLineCount (String tLine) {
		String prev = "";
		String curr = "";
		Scanner lsc = new Scanner (tLine);
        if (lsc.hasNext())
        	curr = lsc.next();

		while (lsc.hasNext()) {
			 prev = curr;
             curr = lsc.next();
		}

		int count = Integer.parseInt(prev.substring(1));
		return count;
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
    	System.err.println("  -binary: make binary decision on soft input");
  }

}

