import java.io.IOException;

public class FuzzyPreprocessPCFG extends FuzzyPreprocess {

	public FuzzyPreprocessPCFG(String input, String output, String slang) {
		super(input, output, slang);
	}

	public static void main(String[] args) {

		String inputpath = "/u/ywu/nlp/final/trainingandtestdata/tweetCorpus.txt";
		String outputpath = "/u/ywu/nlp/final/trainingandtestdata/fuzzy_processed.txt";
		String slangpath = "/u/ywu/workspace/Preprocess/src/slangDic";

		FuzzyPreprocessPCFG p = new FuzzyPreprocessPCFG(inputpath, outputpath,
				slangpath);

		try {
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public String filter(String s) {
		// case-insensitive
		s = s.toLowerCase();

		s = deleteURL(s);

		s = replaceHTML(s);

		s = replaceUsername(s);

		s = replaceHashtag(s);

		// s = splitPunc(s);

		// for pcfg
		s = deletePuncs(s);

		s = addPeriod(s);

		s = removeSingleton(s);
		
		s = removeParen(s);
		
		s = replaceEmoticons(s);

		return s;
	}

	protected String deleteURL(String s) {
		// replace all urls with URL
		return s.replaceAll("(?i)(?:https?|ftps?)://[\\w/%.-_\\?&=]+", "");
	}

	protected String replaceEmoticons(String s) {
		String hat = "[<>]?"; // optional hat/brow
		String eye = "[:;=8]"; // eyes
		String nose = "[\\-o\\*\\']?"; // optional nose
		String happymouth = "[\\)\\]dDpP]"; // happy mouth
		String rhappymouth = "[\\(]";
		String sadmouth = "[\\(\\[/|@]"; // sad mouth
		String rsadmouth = "[\\)]";
		String suprisemouth = "[oO]";

		String happy = hat + eye + nose + happymouth + "|" + rhappymouth + nose
				+ eye + hat;
		String sad = hat + eye + nose + sadmouth + "|" + rsadmouth + nose
				+ eye + hat;
		String suprise = " " + hat + eye + nose + suprisemouth;
		
		String heart = " <[3]+";
		String brokenheart = " </3";
		
		s = s.replaceAll(happy, " I am happy.");
		s = s.replaceAll(sad, " I am sad.");
		s = s.replaceAll(suprise, " I am surprised.");
		s = s.replaceAll(heart, " I love it.");
		s = s.replaceAll(brokenheart, " I do not love it.");
		return s;
	}

	protected String deletePuncs(String s) {
		s = s.trim();
		s = s.replaceAll("([:\"?!\\(\\);,.`~ ])\\1{1,}", "$1");
		if (s.charAt(0) == '\"')
			return s.trim().substring(1, s.length() - 1).trim();
		else
			return s.trim();
	}

	protected String addPeriod(String s) {
		String endPunc = "!?.])";
		String midPunc = "#$%^&(_+=[\\|:\";,`\'~";
		int len = s.length();
		char c = s.charAt(len - 1);
		if (endPunc.indexOf(c) != -1)
			return s;
		else if (midPunc.indexOf(c) != -1)
			return s.substring(0, len - 1) + '.';
		else
			return s + ".";
	}

	protected String removeSingleton(String s) {
		s = s.replaceAll("([.!?\\)]) (\\w)* *[.!?\\)]", "$1");
		s = s.replaceAll("\\b[a-zA-Z]\\.", ".");
		return s;
	}
	
	protected String removeParen(String s) {
		return s.replaceAll("\\((.)*\\)", "");
	}
}
