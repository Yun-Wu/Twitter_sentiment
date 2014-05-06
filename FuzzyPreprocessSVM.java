import java.io.IOException;
import java.util.HashSet;
import java.util.Set;


public class FuzzyPreprocessSVM extends FuzzyPreprocess{

	public FuzzyPreprocessSVM(String input, String output, String slang) {
		super(input, output, slang);
	}
	
	public String filter(String s){
		s = super.filter(s);
		s = neg(s);
		return s;
	}
	
	private String neg(String s){
		Set<String> negs = new HashSet<String>();
		String stop = ".*[.:;!?].*";
		negs.add("never");		
		negs.add("no");		
		negs.add("nothing");		
		negs.add("nowhere");		
		negs.add("noone");		
		negs.add("none");		
		negs.add("not");		
		negs.add("havent");		
		negs.add("hasnt");		
		negs.add("hadnt");		
		negs.add("cant");		
		negs.add("couldnt");		
		negs.add("shouldnt");		
		negs.add("wont");		
		negs.add("wouldnt");		
		negs.add("dont");		
		negs.add("doesnt");		
		negs.add("didnt");		
		negs.add("isnt");		
		negs.add("arent");		
		negs.add("aint");
		
		StringBuffer buffer = new StringBuffer();
		String[] tmp = s.split(" ");
		boolean flag = false;
		for(int i = 0; i < tmp.length; i++){
			if(tmp[i].trim().length()<1) continue;
			if(!flag){
				if(negs.contains(tmp[i])||tmp[i].endsWith("n't"))
					flag = true;
				buffer.append(tmp[i].trim()+" ");
			}else {
				if(tmp[i].matches(stop)){
					flag = false;
					buffer.append(tmp[i].trim()+" ");
				}
				else
					buffer.append(tmp[i].trim()+"_neg ");
			}
		}
		buffer.append("\n");
		return buffer.toString();
	}
	
	public static void main(String[] args) {

		String inputpath = "/u/ywu/nlp/final/trainingandtestdata/fuzzy_processed.txt";
		String outputpath = "/u/ywu/nlp/final/trainingandtestdata/fuzzy_processed_svm.txt";
		String slangpath = "/u/ywu/workspace/Preprocess/src/slangDic";

		FuzzyPreprocessSVM p = new FuzzyPreprocessSVM(inputpath, outputpath,
				slangpath);

		try {
			p.process();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
