package LL1;
import java.util.*;

import lexer.Code;
import lexer.Token;

/*  
 * 
 *             
 *             
对产生式进行编号 1-12    select集
1、E→TE’              {(,i}
2、E’→ATE’            {+,-}
3、E’→ ε         	  {$,)}
4、T→FT’              {(,i}
5、T’→MFT’            {*,/}
6、T’→ ε              {+,-,$,)}
7、F→(E)              {(  }
8、F→ i               { i }
9、A → +               {+}
10、A → -             { - }
11、M → *             { * }
12、M-> /             { / }

预测分析表
	      0      1      2     3       4       5       6        7        
	      i      (      )     +       -       *       /        $          
0、E     E→TE’  E→TE’          
1、E1                  E’→ ε E’→ATE’  E’→ATE’                  E’→ ε
2、T      T→FT’ T→FT’                                                 
3、T1                  T’→ ε  T’→ ε  T’→ ε  T’→MFT’  T’→MFT’   T’→ ε
4、F      F→ i   F→(E)                 
5、A                        A → +     A → -
6、M                                          M → *   M → /

      first        follow
0、E    (,i          ),$
1、E1  +,-,ε         $,)     
2、T   (, i        +,-,$,)
3、T1   ε,*,/      +,-,$,)
4、F    (,i       *,/,+,-,$,)
5、A    +,-        (,i
6、M    *,/        (,i

对非终结符号和终结符号编码   其中终结符号在预测分析表中存储的下标有大小为10的偏移量     [code-10]
0  E      10  i
1  E1     11  (
2  T      12  )
3  T1     13  +
4  F      14  -
5  A      15  *
6  M      16  /
          17  $
		  18  ε(~)



其中空串ε用~替代

*/

public class PredictTable {
	
	int NTnum = 7;       //非终结符号个数 预测分析表的行数
	int Tnum = 8;        //包括美元符号
	int gennum = 12;       //产生式个数
	int flag=0;
    int[][] predtable=new int[NTnum][Tnum];    //预测分析表，存放产生式的编号
    
    Stack<Integer> s = new Stack<Integer>();
    
    int[][] generator=new int[][] {        //存放产生式  第一个数字代表产生式左部
    	{0,2,1},
    	{1,5,2,1},
    	{1,18},
    	{2,4,3},
    	{3,6,4,3},
    	{3,18},
    	{4,11,0,12},
    	{4,10},
    	{5,13},
    	{5,14},
    	{6,15},
    	{6,16}    	
    };
    
    
    void PushGen(int n) {   //将序号为n的产生式的右部压入栈中
    	for(int index = generator[n-1].length-1;index>=1;index--) {
    		if(generator[n-1][index]!=18)               //如果不是空串
    			s.push(generator[n-1][index]);
    	}
    }
    
    
   
    boolean IsT(int code) {            //判断是否为终结符号
    	if (code>=10&&code<=16||code==18) return true;
    	return false;
    }
    
    boolean IsNT(int code) {            //判断是否为非终结符号
    	if (code>=0&&code<=6) return true;
    	return false;
    }
    
    boolean IsDollar(int code) {            //判断是否为美元符号
    	if (code==17) return true;
    	return false;
    }
    
    void error() {                                  //不匹配  产生错误信息
    	flag=1;   	
    	System.out.println("匹配失败");
    	System.exit(0);
    	
    }
    void error(String s) {                                  //不匹配  产生错误信息
    	flag=1;   	
    	System.out.println(s);
    	
    }
    
	public void printgenerator(int num) {           //将当前采用的产生式打印出来

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
	
	public void PrintInput(int index, ArrayList<Token> tokenlist) {
		
		for(int i = index;i<=tokenlist.size()-1;i++) {
			System.out.print(tokenlist.get(i).val);
		}
		System.out.print("     ");
	}
	
	public void PrintStack(Stack<Integer> s) {
		
		for(int i=s.size()-1;i>=0;i--) {
			System.out.print(toString(s.get(i)));
		}
		System.out.print("     ");
	}
	
    int GetCode(String s) {
    	switch(s) {
    		case "E" : return 0;
    		case "E1": return 1;
    		case "T" : return 2;
    		case "T1": return 3;
    		case "F" : return 4;
    		case "A" : return 5;
    		case "M" : return 6;
    		
    		case "i" : return 10;
    		case "(":  return 11;
    		case ")" : return 12;
    		case "+":  return 13;
    		case "-" : return 14;
    		case "*" : return 15;
    		case "/" : return 16;
    		
    		case "$" : return 17; 
    		case "~" : return 18;
    	}		
    	return -1;	
    }
	
    String toString(int code) {
    	switch(code){
	    	case 0 : return "E";
			case 1 :  return "E1";
			case 2 : return "T";
			case 3 :  return  "T1";
			case 4 : return "F";
			case 5 : return "A";
			case 6 : return "M";
		
			case 10 : return "i";
			case 11:  return "(";
			case 12 : return ")";
			case 13:  return "+";
			case 14 : return "-";
			case 15 : return "*";
			case 16 : return "/";
			
			case 17 : return "$"; 
			case 18 : return "~";
    	}
    	
    	return null;
    }
    public void analyze(ArrayList<Token> tokenlist) {         //LL(1)语法分析
    	
    	s.push(GetCode("$"));  
    	s.push(0);           //栈初始化   开始符号和美元符号入栈
    	
    	int top = s.peek();      //始终指向栈顶符号
    	
    	int index = 11;             
    	Token p = null;//指向输入中的当前符号
    	Token dollar=new Token(Code.DOLLAR,"$");
        tokenlist.add(dollar);
        
        PrintStack(s);
		PrintInput(index, tokenlist);
		System.out.print("\n");
		
    	while(top!=GetCode("$")) {
    		
    		p = tokenlist.get(index); 
    		int pcode;
    		pcode=(p.code==Code.ID? GetCode("i"): GetCode(p.val));
    		
    		if(pcode==-1) error("输入符号非法！！！");
    		
    		if(top==pcode) {            //如果栈顶的终结符号和当前输入匹配
    			
    			s.pop();
    			index++;
    			PrintStack(s);
    			PrintInput(index, tokenlist);
    			System.out.print("\n");
    			
    		}
    		
    		else if(IsT(top)) {       //如果是其他终结符号
    			
    			error();
    		}
    		
    		else if(IsNT(top) && predtable[top][pcode-10]==0) {   //如果预测分析表中该条目为error
    			
    			error();
    			
    		}
    		
    		else if(IsNT(top) && predtable[top][pcode-10]!=0) {
    			
    			
    			s.pop();
    			PushGen(predtable[top][pcode-10]);
    			
    			PrintStack(s);
    			PrintInput(index, tokenlist);
    			printgenerator(predtable[top][pcode-10]);       //打印当前采用的表达式
    			
    		
    		}
    		
    		else error();
    		
    		top=s.peek();
    		   		
    	}
    	
    	if(flag==0) System.out.print("该表达式合法!!!");
    	else error("不是cp语言合法表达式!!!");
      	
    }
	
	 public PredictTable() {              //预测分析表初始化  若为0则error
		 predtable[0][0]=1;
		 predtable[0][1]=1;
		 predtable[1][2]=3;
		 predtable[1][3]=2;
		 predtable[1][4]=2;
		 predtable[1][7]=3;
		 predtable[2][0]=4;
		 predtable[2][1]=4;
		 predtable[3][2]=6;
		 predtable[3][3]=6;
		 predtable[3][4]=6;
		 predtable[3][5]=5;
		 predtable[3][6]=5;
		 predtable[3][7]=6;
		 predtable[4][0]=8;
		 predtable[4][1]=7;
		 predtable[5][3]=9;
		 predtable[5][4]=10;
		 predtable[6][5]=11;
		 predtable[6][6]=12;
	}
	
	
}
