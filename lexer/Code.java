package lexer;

import java.util.Hashtable;

/**
 * 
 * 每个终结符号的编码  保留字区：BEGIN=10,END=11,IF=12,THEN=13,ELSE=14,FOR=15,WHILE=16,DO=17,AND=18,OR=19,NOT=20 以及 NUM=100,ID=101
 * @author 胡逸藤
 */
public class Code{               //种别码的赋值
	public final static int 
			BEGIN=10,END=11,IF=12,THEN=13,ELSE=14,FOR=15,WHILE=16,
			DO=17,AND=18,OR=19,NOT=20,
			DOLLAR=50,NUM=100,ID=101,OP=102,DIV=103; 
	
	public final static Hashtable<Integer,String> tokentable=new Hashtable<Integer,String>();
	
	
	public String toString(int tokencode){
		return tokentable.get(tokencode);
	}
	
	public Code() {
		tokentable.put(10, "begin");
		tokentable.put(11, "end");
		tokentable.put(12, "if");
		tokentable.put(13, "then");
		tokentable.put(14, "else");
		tokentable.put(15, "for");
		tokentable.put(16, "while");
		tokentable.put(17, "do");
		tokentable.put(18, "and");
		tokentable.put(19, "or");
		tokentable.put(20, "not");
		tokentable.put(100, "num");
		tokentable.put(101, "id");
		tokentable.put(102, "op");
		tokentable.put(103, "div");
	}
	
}
