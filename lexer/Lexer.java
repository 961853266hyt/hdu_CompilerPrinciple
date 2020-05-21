package lexer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * @author 胡逸藤
 *
 */
/*
(1)关键词： begin  end  if   then  else   for  while  do  and  or  not
(2)标识符id， (_|letter)(_|letter|digit)*  ，并且标识符不能是关键词
(3)无符号整数NUM：数字串    (digit)+
(4)运算符和分界符： +、-、*、/、>、<、=、:=、>=、<=、<>、++、--、(、)、; 、 # 
注意：:=表示赋值运算符、#表示注释开始的部分，;表示语句结束，<>表示不等关系
(5) 空白符包括" "、"\t"和"\n"，用于分割ID、NUM、运算符、分界符和关键词，词法分析阶段要忽略空白符。

*/
public class Lexer {
	public int line=1;
	private char temp;   //存放预先读入的下一个字符   
	
	ArrayList<Token> tokenlist=new ArrayList<Token>(11);    //Token类型动态数组
	
	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	void addtoken(Token t) {
		tokenlist.add(t);
	}

	public Lexer() {               
		addtoken(new Token(Code.BEGIN,"begin"));   //执行词法分析器之前 预先导入11个关键词
		addtoken(new Token(Code.END,"end"));
		addtoken(new Token(Code.IF,"if"));
		addtoken(new Token(Code.THEN,"then"));
		addtoken(new Token(Code.ELSE,"else"));
		addtoken(new Token(Code.FOR,"for"));
		addtoken(new Token(Code.WHILE,"while"));
		addtoken(new Token(Code.DO,"do"));
		addtoken(new Token(Code.AND,"and"));
		addtoken(new Token(Code.OR,"or"));
		addtoken(new Token(Code.NOT,"not"));
	}
	
	 public boolean IsOPorDIV(char c) {
		 if(c=='+'||c=='-'||c=='*'||c=='/'||c=='>'||c=='<'||c==':'||c=='('||c==')'||c==';' ||c=='#'||c==' '||c=='$'||c=='\t'||c=='\n')
				 return true;
		 return false;
	 }
	 
	public boolean IsDigit(char c) {
		return (c>='0'&&c<='9');
	}
	
	public boolean IsLetter_(char c) {
		return (c>='A'&&c<='Z'||c>='a'&&c<='z'||c=='_');
	}
	
	public Token IsKeyword(String s) {       //判断该单词是否是关键词
		Token w;
		for(int i=0;i<11;i++) {
			w=tokenlist.get(i);
			if(s.equals(w.val)) return w;
		}
		return null;
	}
	
	void showerror() {          //向控制台输出错误信息 告知具体位置
		
		System.out.println("\nerror occurs at line"+":"+line);
		
	}
	
	void printlist() {
		Code c=new Code();
		Token w;
		for(int i=11;i<tokenlist.size();i++) {
			w=tokenlist.get(i);
			System.out.println("<"+c.toString(w.code)+"  "+w.val+">");
		}
	}
	
	void readchar() throws IOException {
		temp=(char)br.read();
	}
	boolean readchar(char target) {
		try {
			readchar();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return target==temp;	
	}
	
	public void TokenScan() throws IOException {
		
		int flag=0;                 //判断是否已经读取过下一个字符
		
		while(temp!='$') {
			
			do {                //过滤空白符和注释
				if(flag==0) readchar();
				flag=0;
				if(temp==' '||temp=='\t') continue;
				else if(temp=='\n') {line++;continue;}
				else if(temp=='#') 
				{              //如果下一个字符是注释符
					do {
						readchar();
					   }while(temp!='\n');				
				}
				else break;
			}while(true);
			
				switch(temp) {
					case '+':
					{	
						
						if(readchar('+')) addtoken(new Token(Code.OP,"++")); 
						else {flag=1;addtoken(new Token(Code.OP,"+"));}
						break;
					}
					case '=':
					{
						addtoken(new Token(Code.OP,"="));	
						break;
					}
					
					case '>':
					{	
						
						if(readchar('=')) addtoken(new Token(Code.OP,">=")); 
						else {flag=1;addtoken(new Token(Code.OP,">"));}
						break;
					}
					
					case '<':
					{	
						
						if(readchar('=')) addtoken(new Token(Code.OP,"<=")); 
						else if(temp=='>') addtoken(new Token(Code.OP,"<>"));
						else {flag=1;addtoken(new Token(Code.OP,"<"));}
						break;
					}
					
					case '-':
					{	
						
						if(readchar('-')) addtoken(new Token(Code.OP,"--")); 
						else {flag=1;addtoken(new Token(Code.OP,"-"));}
						break;
					}
					
					case ':':
					{
						
						if(readchar('=')) addtoken(new Token(Code.OP,":=")); 
						else {flag=1;showerror();}           //：不是任何词法单元
						break;
					}
					
					case '*':
					{
						addtoken(new Token(Code.OP,"*"));
						break;
					}
					
					case '/':
					{
						addtoken(new Token(Code.OP,"/"));
						break;
					}
					
					case '(':
					{
						addtoken(new Token(Code.DIV,"("));
						break;
					}
					
					case ')':
					{
						addtoken(new Token(Code.DIV,")"));
						break;
					}
					
					case ';':
					{
						addtoken(new Token(Code.DIV,";"));
						break;
					}
				}
			
				if(IsDigit(temp)) {                 //判断是否为数字串
						int val=0;
					    do{
					        val=val*10+Character.digit(temp,10);
					        temp=(char)br.read();
					    }while(IsDigit(temp));
					    flag=1;
					    
					    if(IsOPorDIV(temp)==false) showerror();     //以字母开头的标识符非法
					    else addtoken(new Token(Code.NUM,""+val));
				}
				
				else if(IsLetter_(temp)){                 //如果下一个字符是字母或者下划线
					StringBuffer sb=new StringBuffer();
					do {
						sb.append(temp);
						readchar();
					}while(IsLetter_(temp)||IsDigit(temp));
					flag=1;
					String SB=sb.toString();
					Token t;
					if((t=IsKeyword(SB))!=null)                           //如果保留字中有该串，添加关键词
						addtoken(t);
					else {                         //如果没有在保留字中说明这是一个标识符,将其添加至tokenlist表
						t=new Token(Code.ID,SB);
						addtoken(t); 
					}
				}
			}
		br.close();
		}
		
	}
	
	

