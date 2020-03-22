package javax.core.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * HTML 内容操作
 * @author Tanyongde
 *
 */
public class HtmlUtils {
	
	private HtmlUtils(){}

	/**
	 * 将HTML代码中的特殊代码替换成显式字符
	 * 例如 & 替换为  &amp;
	 * 	   < 替换为  &lt;
	 * @param txt
	 * @return
	 */
	public static String htmlEncode(String txt){
		
		txt = txt.replaceAll("&","&amp;");
		txt = txt.replaceAll("&amp;amp;","&amp;");
		txt = txt.replaceAll("&amp;quot;","&quot;");
		txt = txt.replaceAll("\"","&quot;");
		txt = txt.replaceAll("&amp;lt;","&lt;");
		txt = txt.replaceAll("<","&lt;");
		txt = txt.replaceAll("&amp;gt;","&gt;");
		txt = txt.replaceAll(">","&gt;");
		txt = txt.replaceAll("&amp;nbsp;","&nbsp;");

		return txt;
	}
	
	/**
	 * 将转换后的HTML代码中的特殊代码替换成unicode显示字符
	 * 例如 &lt; 替换为 &#60;
	 * @param txt
	 * @return
	 */
	public static String HtmlUnicode(String txt) {
		txt = txt.replaceAll("&nbsp;","&#160;");
		txt = txt.replaceAll("&quot;","&#34;");
		txt = txt.replaceAll("&lt;","&#60;");
		txt = txt.replaceAll("&gt;","&#62;");
		
		return txt;
	}
	
	/**
	 * 将HTML中的显式字符替换成特殊代码
	 * 例如 &amp;  替换为  &
	 * 	   &lt; 替换为 <
	 * @param txt
	 * @return
	 */
	public static String unHtmlEncode(String txt){
		txt = txt.replaceAll("&amp;","&");
		txt = txt.replaceAll("&quot;","\"");
		txt = txt.replaceAll("&lt;","<");
		txt = txt.replaceAll("&gt;",">");
		txt = txt.replaceAll("&nbsp;"," ");
		
		return txt;
	}
	
	/**
	 * 将HTML中的unicode显式字符替换成特殊代码
	 * 例如 &62;  替换为  >
	 * @param txt
	 * @return
	 */
	public static String unHtmlUnicode(String txt) {
		txt = txt.replaceAll("&#160;"," ");
		txt = txt.replaceAll("&#34;","\"");
		txt = txt.replaceAll("&60;","<");
		txt = txt.replaceAll("&62;",">");
		
		return txt;
	}
	
	/**
	 * 将HTML标签用特殊代码代替
	 * @param input
	 * @return
	 */
	public static String setHtmlTag(String input){
		input=StringUtils.trim(input);
		input=input.replaceAll("<","&lt;");
		input=input.replaceAll(">","&gt;");
		input=input.replaceAll("\r","");
		input=input.replaceAll("\n","<br>");
		return input;
	}
	/**
	 * 将HTML特殊代码用标签代替
	 * @param input
	 * @return
	 */
	public static String getHtmlTag(String input){
		input=StringUtils.trim(input);
		input=input.replaceAll("<br>","\r\n");
		input=input.replaceAll("&lt;","<");
		input=input.replaceAll("&gt;",">");
		return input;
	}    
	
	
	/**
	 * 给文字设置一个颜色值，并转换为 HTML 代码
	 * @param sw
	 * @param sTemp
	 * @param sColor
	 * @return
	 */
	public static String markColor(String sw,String sTemp,String sColor)
	{
		String sReturn="";
		int i=0,j=0;
		int iTempLength = sTemp.length();
		int iLengthS1 = sw.length();
		String sTemp1=sw.toLowerCase();
		String sTemp2 = sTemp.toLowerCase();
		while(true)
		{
			i=sTemp2.indexOf(sTemp1,j);
			if(i==-1)
			{
				sReturn += sTemp.substring(j,iTempLength);
				break;
			}
			sReturn += sTemp.substring(j,i) + "<font color=\""+sColor+"\">"+ sTemp.substring(i,i+iLengthS1) + "</font>";
			
			j= i + iLengthS1;
			if(j>iTempLength)
				j = iTempLength;
		}
		return sReturn;
	}
	

	/**
	 * 去除HTML代码
	 * 
	 * @param content
	 * @return
	 */
	public static String removeHtml(String content) {
		if (null == content) return "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			p_html = Pattern.compile("<[^>]+>", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);
			content = m_html.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 去除HTML代码中的Iframe
	 * 
	 * @param content
	 * @return
	 */
	public static String removeIframe(String content) {
		if (null == content) return "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			p_html = Pattern.compile("<iframe[^>]+>", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);
			content = m_html.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 去除HTML代码中的样式
	 * 
	 * @param content
	 * @return
	 */
	public static String removeStyle(String content) {
		if (null == content) return "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			p_html = Pattern.compile("<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);
			content = m_html.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 去除HTML代码中的脚本
	 * 
	 * @param content
	 * @return
	 */
	public static String removeScript(String content) {
		if (null == content) return "";
		java.util.regex.Pattern p_html;
		java.util.regex.Matcher m_html;
		try {
			p_html = Pattern.compile("<[\\s]*?script[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>", Pattern.CASE_INSENSITIVE);
			m_html = p_html.matcher(content);
			content = m_html.replaceAll("");
		} catch (Exception e) {
			return "";
		}
		return content;
	}
	
	/**
	 * 去除HTML代码中的空格
	 * 
	 * @param content
	 * @return
	 */
	public static String removeSpace(String content) {
		if (null == content) return "";
		return content.replaceAll("\\s*(\\r\\n)\\s*", "").replaceAll(">(\\s+)", ">").replaceAll("(\\s+)<", "<");
	}
	
	
	/**
	 * 使用正则表达式,进行替换,以保证文件里面的音频文件可以显示
	 * @param str 目标字符串
	 * @param pageUrl 要替换的页面地址
	 * @return 经过处理后的字符串
	 */
	public static String regStr(String str, String pageUrl ) {		
		try {
			str = str.replaceFirst("name=filename", "name=\"filename\"");
			String patternString = "(<param\\s*name=\"[^\"]*\"\\s+value\\s*=\\s*\"?)(\\w{1,20}(.swf|.mp3|.wav|.avi)\"?)";
			Pattern pattern = Pattern.compile(patternString,
					Pattern.CASE_INSENSITIVE);
			Matcher matcher = pattern.matcher(str);
			StringBuffer sb = new StringBuffer();			
			while (matcher.find()) {
				matcher.appendReplacement( sb, matcher.group(1) + pageUrl + matcher.group(2) );
			}
			matcher.appendTail(sb);
			  str = sb.toString();
			  str = str.replaceAll( "SRC=\"", "SRC=\"" + pageUrl );
			  str = str.replaceAll( "src=\"", "src=\"" + pageUrl );	
			  
			  str = new String( str.getBytes( "ISO-8859-1" ), "GBK" );	
			
		} catch (PatternSyntaxException exception) {
		}catch( Exception e ){
		}
		return str;
	}
}
