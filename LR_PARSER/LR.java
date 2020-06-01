package LR_PARSER;
import java.util.*;
import lexer.Token;
public class LR {   //LR语法分析器
	
/*
  * 0  S1     5 i     12 :=
    1  S      6 (     13 $
    2  E      7 )
    3  T      8 +
 *  4  F      9 -
 *            10 *
 *            11 /
 *            
 *  [0]	S’→ S                       first       follow
	[1]	S → i := E         S1        i            $
	[2]	E → E+T            S         i            $
    [3]	E → E-T            E        (,i        $,),+,-
	[4] E → T              T        (,i        $,),+,-,*,/
	[5] T → T*F            F        (,i		   $,),+,-,*,/
	[6] T → T/F
	[7]	T → F
	[8] F → (E)
	[9] F → i
 *         分析表          
 *        			 GOTO         					                                             
 *           S1     S     E    T     F 		   i    (    )    +      -     *    /    :=    $      
 *    0             2                          s1
 *    1                                                                              s3
 *    2                                                                                   acc
 *    3                   4    5     6         s8   s7
 *    4                                                      s9     s10                   r1
 *    5                                                  r4  r4      r4    s13            r4
 *    6                                                  r7  r7      r7    r7   r7        r7
 *    7                  17    5     6         s8   s7
 *    8                                                  r9  r9      r9    r9   r9        r9
 *    9                       11     6         s8   s7
 *    10                      12     6         s8   s7 
 *    11                                                 r2   r2     r2   s13   s15       r2
 *    12                                                 r3   r3     r3   s13   s15       r3
 *    13                            14         s8   s7                                        
 *    14                                                 r5   r5     r5   r5    r5        r5
 *    15                            16         s8   s7
 *    16                                                 r6   r6     r6   r6    r6        r6
 *    17                                                 s18  s9     s10  
 *    18                                                 r8   r8     r8   r8    r8        r8
 *         
 *                   ACTION 
 *         i   (    )    +      -     *    /    :=    $      
 *    0
 *    1
 *    2
 *    3
 *    4
 *    5
 *    6
 *    7  
 *    8   
 *       
 */
 
	public int GetCode(Token t) {
		if(t.code == lexer.Code.ID) return Code.GetCode("ID");
		
		return Code.GetCode(t.val);	
	}
	
	public void error(String s) {
		System.out.println("匹配"+s+"时发生错误！！！");
		
	}
	
	public void PrintStack(Stack<Integer> s) {
		
		for(int i=0;i<=s.size()-1;i++) {
			System.out.print(s.get(i));
		}
		System.out.print("     ");
	}
	
	public void PrintCurrentToken(ArrayList<Token> t,int index) {
		for(int i = 11;i<=index;i++) {
			System.out.print(t.get(i).val);
		}
		System.out.print("   ");
		
	}
	public void success() {
		System.out.println("语法分析完成！！！\n该语句合法！！！");
	}
	
	public void LR_Analyse(Behave[][] GOTO, Behave[][] ACTION, ArrayList<Token> tokenlist) {         //得到生成的SLR分析表
		Stack<Integer> state_stack = new Stack<Integer>();
		int index = 11;
		int top, code;
		Token t = tokenlist.get(index);
		state_stack.push(0);
		tokenlist.add(new Token(Code.Dollar,"$"));
		
		while(true){
			top = state_stack.peek();
			code = GetCode(t);
			if(ACTION[top][code-5]==null) {error(t.val);return;}
			if(ACTION[top][code-5].act == Behave.s) {
				PrintStack(state_stack);
				PrintCurrentToken(tokenlist,index);
				System.out.println("移入");
				state_stack.push(ACTION[top][code-5].gen_num);			
				t = tokenlist.get(++index);				
			}
			else if(ACTION[top][code-5].act == Behave.r){
				int gen_num = ACTION[top][code-5].gen_num;
				PrintStack(state_stack);
				PrintCurrentToken(tokenlist,index);
				SLR.PrintGenerator(gen_num);
				for(int i=0;i<SLR.Getlength(gen_num);i++) {   //弹出栈顶的待规约串长度个元素
					state_stack.pop();
				}
				top = state_stack.peek();
				if(GOTO[top][SLR.GetNT(gen_num)]==null)
				{
					error(t.val);
					return;
				}
				state_stack.push(GOTO[top][SLR.GetNT(gen_num)].gen_num);
				
			}
			else if(ACTION[top][code-5].act == Behave.acc) {
				PrintStack(state_stack);
				PrintCurrentToken(tokenlist,index);
				SLR.PrintGenerator(0);
				success();
				break;
			}
			else error(t.val);			
		}
		
	}
}
