import java.io.IOException;


public class FuzzyPreprocessPCFG extends FuzzyPreprocess{

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
			
		//s = splitPunc(s);
		
		//for pcfg
		s = replaceEmoticons(s);
		
		s = deletePuncs(s);
		
		s = addPeriod(s);
		
		s = removeSingular(s);
		
		return s;
	}
	
	protected String deleteURL(String s) {
		// replace all urls with URL
		return s.replaceAll("(?i)(?:https?|ftps?)://[\\w/%.-]+", "");
	}
	
	protected String replaceEmoticons(String s){
		s = s.replaceAll(":\\)", "I am happy.");
		s = s.replaceAll(":\\(", "I am sad.");
		s = s.replaceAll("<[3]+", "I love it.");
		return s;
	}
	
	protected String deletePuncs(String s){
		s = s.trim();
		s = s.replaceAll("([:\"?!\\(\\);,.`~ ])\\1{1,}", "$1");
		if(s.charAt(0)=='\"') return s.trim().substring(1, s.length()-1).trim();
		else return s.trim();
	}
	
	protected String addPeriod(String s){
		String endPunc = "!?.])";
		String midPunc = "#$%^&(_+=[\\|:\";,`\'~";
		int len = s.length();
		char c = s.charAt(len-1);
		if(endPunc.indexOf(c) != -1) return s;
		else if(midPunc.indexOf(c) != -1) return s.substring(0, len-1) + '.';
		else return s+".";
	}
	
	protected String removeSingular(String s){
		return s.replaceAll("([.!?\\)]) (\\w)* *[.!?\\)]", "$1");
	}
}
