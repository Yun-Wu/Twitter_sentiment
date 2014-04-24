import java.io.IOException;

public abstract class Preprocess {

	public Preprocess() {
		// TODO create test file

	}
	
	//generate formatted path
	public abstract void process() throws IOException;

	public String filter(String s) {
		//case-insensitive
		s.toLowerCase();
		
		// replace all urls with URL
		s = s.replaceAll("(?i)(?:https?|ftps?)://[\\w/%.-]+", "URL ");

		// replace all html entities
		String[] html = { "&ndash;", "&quot;", "&nbsp;", "&amp;", "&gt;",
				"&lt;" };
		String[] ascii = { "-", "\"", " ", "&", ">", "<" };
		String punctuations = "!#$%^&()_+=[]\\{}|:\"?;,.`~";

		for (int i = 0; i < html.length; i++) {
			s = s.replace(html[i], ascii[i]);
		}

		String[] tmp = s.split(" ");
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < tmp.length; i++) {
			// check if it's a target
			if (tmp[i].startsWith("@")) {
				buffer.append("Target ");
				while (i < tmp.length && tmp[i].startsWith("@"))
					i++;
				if (i == tmp.length)
					break;
			}
			// check if it's a hashtag
			if (tmp[i].startsWith("#") && tmp[i].length() > 1
					&& isLetter(tmp[i].charAt(1))) {
				buffer.append(" " + tmp[i].substring(1));
				continue;
			}
			boolean flag = false;
			for (int j = 0; j < tmp[i].length(); j++) {
				if(punctuations.indexOf(tmp[i].charAt(j)) != -1){
					if(!flag){
						flag = true;
						buffer.append(' ');
					}
				}else {
					if(flag) {
						flag = false;
						buffer.append(' ');
					}
				}					
				buffer.append(tmp[i].charAt(j));
			}
			buffer.append(' ');
		}
		return buffer.toString();
	}

	public boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	public boolean isAllAscii(String s) {
		for (int i = 0; i < s.length(); i++) {
	        int c = s.charAt(i);
	        if (c > 0x7F) {
	            return false;
	        }
	    }
		return true;
	}
}
