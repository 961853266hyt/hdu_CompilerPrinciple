package 递归下降子程序;
import java.util.*;
import lexer.*;

/*              
对产生式进行编号 1-12    select集
1、E→TE’              {(,i}
2、E’→ATE’            {+,-}
3、E’→ ε         	  {$,)}
4、T→FT’              {(,i}
5、T’→MFT’            {*,/}
6、T’→ ε              {+,-,$,)}
7、F→(E)              {(  }
8、F→ i               { i }
9、A → +              {+,-}
10、A → -             { - }
11、M → *             { * }
12、M-> /             { / }
*/

public class RecurDesc{
    public int flag=0;
    int i=11,generator_num=0;
    ArrayList<Token> tokenlist;
    Token p=null;
   
    char[][] select=new  char[][]{{'(','i'},{'+','-'},{'$',')'},{'(','i'},{'*','/'},{'+','-','$',')'},{'('},{'i'},{'+'},{'-'}, {'*'},{'/'}};

    public RecurDesc(ArrayList<Token> tokenlist){
        this.tokenlist=tokenlist;
        p=tokenlist.get(11);             //指向当前符号的指针初始化
        Token dollar=new Token(Code.DOLLAR,"$");
        tokenlist.add(dollar);
    }
    
	public void readnext(){
        i++;
        if(i<tokenlist.size())
            p=tokenlist.get(i); 
        else error();
	}
	
	public void printgenerator(int num) {           //将当前采用的产生式打印出来
		System.out.print(++generator_num+"、 ");
		switch(num-1) {
			case 0:
				System.out.println("E->TE'");break;
			case 1:
				System.out.println("E'->ATE'");break;
			case 2:
				System.out.println("E'->ε");break;
			case 3:
				System.out.println("T->FT'");break;
			case 4:
				System.out.println("T'->MFT'");break;
			case 5:
				System.out.println("T'->ε");break;
			case 6:
				System.out.println("F->(E)");break;
			case 7:
				System.out.println("F->i");break;
			case 8:
				System.out.println("A->+");break;
			case 9:
				System.out.println("A->-");break;
			case 10:
				System.out.println("M->*");break;
			case 11:
				System.out.println("M->/");break;

		}
		
	}              

	public void error(String NT){             //不匹配时产生错误信息
        System.out.println("error:"+NT);
        flag=1;
        return;
    }
	public void error(){                       
        System.out.println("match failed\n");
        flag=1;
        return;
    }

	public void match(String c)
	{
		if(p.val.equals(c)) readnext();
		else error(p.val);
	}

	public boolean select(Token p,int n){      //判断终结符号c或者ID是否在序号为n的产生式的select集中
		String c=p.val;
        int index=n-1;
        
        
        if(p.code==Code.ID) {                //如果是ID
	        for(int i=0;i<select[index].length;i++){
	            if(select[index][i]=='i') return true;
	        }
        }
        else{                                //如果是其他终结符号
        	 for(int i=0;i<select[index].length;i++){
 	            if(c.equals(select[index][i]+"")&&select[index][i]!='i') return true;
 	        }
        }
        return false;
	}

	public void E(){                        
        if(select(p,4))   {printgenerator(1);T();E1();}       
        else error("E");
	}

	public void E1(){                      
        if(select(p,9))   
    	{	
    		printgenerator(2);
    		A(); T(); E1();
    	}  
        else if(select(p,10))
        {	
    		printgenerator(2);
    		A(); T(); E1();
    	}  
        else if(select(p,3)) 
        {
        	printgenerator(3);
        	return;                   	
        }
        else error("E1");
	}

	public void T(){                
        if(select(p,7))   
        {	
        	printgenerator(4);
        	F();T1();
        } 
        else if(select(p,8))
        {
         	printgenerator(4);
         	F();T1(); 
        }
        else error("T");
	}

	public void T1(){
        if(select(p,11))   
        {	
        	printgenerator(5);
        	M();F();T1();
        }   
        else if(select(p,12))
        {
        	printgenerator(5);
        	M();F();T1();
        }
        else if(select(p,6))   
        {	
        	printgenerator(6);
        	return;                      /*???????????????*/
        }
        else error("T1");
	}

	public void F(){
        if(p.val.equals("("))
        {	
        	printgenerator(7);
            match("(");   
            E();
            match(")");   
        }
        else if(p.code==Code.ID) 
        {	
        	printgenerator(8);
        	readnext();
        }
        else error("F");
	}
	
	public void A(){
        if(p.val.equals("+")) 
        {
        	printgenerator(9);
        	match("+");
        }
        else if(p.val.equals("-")) 
        {
        	printgenerator(10);
        	match("-");
        }
        else error("A");
	}

	public void M(){
        if(p.val.equals("*")) 
        {
        	printgenerator(11);
        	match("*");
        }
        else if(p.val.equals("/")) 
        {
        	printgenerator(12);
        	match("/");
        }
        else error("M");
	}

}
