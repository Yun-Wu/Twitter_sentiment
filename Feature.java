import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Feature {
	String inputpath = null;
	File input = null;
	Set<String> features = null;
	
	Feature(String inputpath){
		this.inputpath = inputpath;
		input = new File(inputpath);
		features = new HashSet<String>();
	}
	
	private void getFeatures(String regex) throws IOException{
		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = null;
		
		String line = null;
		while ((line = br.readLine()) != null) {
			
			int index = line.indexOf("\t");
			if (index == -1)
				continue;
			String tweet = line.substring(index + 1);
			matcher = pattern.matcher(tweet);
			while (matcher.find()) {
				features.add(matcher.group(1));
				System.out.println(matcher.group(1));
			}
		}
		reader.close();
		br.close();
	}
	
	private void getVectors(String outputpath) throws IOException{
		FileReader reader = new FileReader(input);
		BufferedReader br = new BufferedReader(reader);
		
		File output = new File(outputpath);
		FileWriter writer = new FileWriter(output);
		
		boolean flag = false;
		String line = null;
		while ((line = br.readLine()) != null) {
			
			int index = line.indexOf("\t");
			if (index == -1)
				continue;
			String tag = line.substring(0, index);
			String tweet = line.substring(index + 1);
			
			if(tag.equals("positive"))
				writer.write("1 ");
			else if(tag.equals("negative"))
				writer.write("-1 ");
			
			int i = 0;
			for(String f:features){
				writer.write(""+i);
				flag = Pattern.compile("\\b"+f+"\\b").matcher(tweet).find();
				if(flag)
					writer.write(":1 ");
				else
					writer.write(":0 ");
				writer.flush();
				i++;
			}
			writer.write("\n");
		}
		writer.close();
		reader.close();
		br.close();
	}
	
	public void getCaps(String output){
		try {
			getFeatures("\\b([A-Z]+)\\b");
			getVectors(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getLengs(String output){
		try {
			getFeatures("\\b(\\w*(\\w)\\2{2,}\\w*)\\b");
			getVectors(output);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args){
		String input = "/u/ywu/nlp/final/trainingandtestdata/fuzzy_processed.txt";
		Feature f = new Feature(input);
		
//		String capspath = "/u/ywu/nlp/final/trainingandtestdata/features_caps.txt";
//		f.getCaps(capspath);
		
		String longpath = "/u/ywu/nlp/final/trainingandtestdata/features_lengs.txt";
		f.getLengs(longpath);
	}
}
