import java.io.IOException;

public abstract class Preprocess {

	public Preprocess() {
		// TODO create test file

	}

	// generate formatted path
	public abstract void process() throws IOException;

	public String filter(String s) {
		// case-insensitive
		s = s.toLowerCase();

		s = replaceURL(s);

		s = replaceHTML(s);

		s = replaceUsername(s);

		s = replaceHashtag(s);
		
		

		s = splitPunc(s);
		return s;
	}

	protected String replaceURL(String s) {
		// replace all urls with URL
		return s.replaceAll("(?i)(?:https?|ftps?)://[\\w/%.-]+", "URL ");
	}

	protected String replaceHTML(String s) {
		// replace all html entities
		String[] html = { "&ndash;", "&quot;", "&nbsp;", "&amp;", "&gt;",
				"&lt;" };
		String[] ascii = { "-", "\"", " ", "&", ">", "<" };

		for (int i = 0; i < html.length; i++) {
			s = s.replace(html[i], ascii[i]);
		}

		return s;
	}

	protected String replaceUsername(String s) {
		// replace all urls with URL
		return s.replaceAll("@[\\S]+", "target ");
	}

	protected String replaceHashtag(String s) {
		// replace all urls with URL
		return s.replaceAll("#", " ");
	}
				
	protected String splitPunc(String s) {
		String punctuations = "!#$%^&()_+=[]\\{}|:\"?;,.`~";
		StringBuffer buffer = new StringBuffer();
		boolean flag = false;
		for (int j = 0; j < s.length(); j++) {
			if (punctuations.indexOf(s.charAt(j)) != -1) {
				if (!flag) {
					if (!(s.charAt(j) == '.' && j > 0 && j < s.length() - 1
							&& isNumber(s.charAt(j - 1)) && isNumber(s
								.charAt(j + 1)))) {     //do not split if it's a decimal point
						flag = true;
						buffer.append(' ');
					}
				}
			} else {
				if (flag) {
					flag = false;
					buffer.append(' ');
				}
			}
			buffer.append(s.charAt(j));
		}
		return buffer.toString();
	}

	private boolean isLetter(char c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}

	private boolean isNumber(char c) {
		return (c >= '0' && c <= '9');
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
