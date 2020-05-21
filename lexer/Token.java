package lexer;


/**
 * @author ºúÒİÌÙ
 * Token  <code,val>  
 */
public class Token {
	public final int code;        //´æ·ÅÖÖ±ğÂë
	public  String val;
	
	public Token(int code,String s) {
		this.code=code;
		val=new String(s);
	}
	
	public String toString() {
		return ""+(char)code;
	}
}