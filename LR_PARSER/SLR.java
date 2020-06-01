package LR_PARSER;

import java.util.*;

/*
 *     项集的表示   整数对   <产生式编号，点的位置>
 * 
 * 
 * 二维数组存放产生式   
 * 
 * 	[0]	S’→ S                       first       follow
	[1]	S → i := E         S1        i            $
	[2]	E → E+T            S         i            $
    [3]	E → E-T            E        (,i        $,),+,-
	[4] E → T              T        (,i        $,),+,-,*,/
	[5] T → T*F            F        (,i		   $,),+,-,*,/
	[6] T → T/F
	[7]	T → F
	[8] F → (E)
	[9] F → i
	
	当需要使用第一条产生式S’→ S进行规约时，输入串被接受
	
	项集的闭包
	
	0  S1     5 i     12 :=
    1  S      6 (     13 $
    2  E      7 )
    3  T      8 +
 *  4  F      9 -
 *            10 *
 *            11 /
 *            
 *  
 *         分析表          
 *        			 GOTO         					 ACTION                                             
 *       S1     S     E    T     F 		 i   (    )    +      -     *    /    :=    $      
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
 *         
 */


public class SLR {
	int NTnum = 5;
	int Nnum = 9;
	int gen_num = 10; 
	int state_num = 19;
	
	ArrayList<Item> itemset = new ArrayList<Item>();  
	ArrayList<ArrayList<Item>> itemsetfamily = new ArrayList<ArrayList<Item>>();   //存放项集族
	Behave[][] analyse_table = new Behave[state_num][NTnum+Nnum];    //分析表
	public Behave[][] ACTION = new Behave[state_num][Nnum];
	public Behave[][] GOTO = new Behave[state_num][NTnum];
	/*
	 * 闭包伪算法
	 * 将I中的各个项加入到闭包中
	 * 如果   A->a*Bc在 closure(I)中,并且B-*>x 不在闭包中，则将这个项集加入闭包
	 */
	   public SLR(){
		   GOTO[0][Code.S] = new Behave(2);
		   GOTO[3][Code.E] = new Behave(4);
		   GOTO[3][Code.T] = new Behave(5);
		   GOTO[3][Code.F] = new Behave(6);
		   GOTO[7][Code.E] = new Behave(17);
		   GOTO[7][Code.T] = new Behave(5);
		   GOTO[7][Code.F] = new Behave(6);
		   GOTO[9][Code.T] = new Behave(11);
		   GOTO[9][Code.F] = new Behave(6);
		   GOTO[10][Code.T] = new Behave(12);
		   GOTO[10][Code.F] = new Behave(6);
		   GOTO[13][Code.F] = new Behave(14);
		   GOTO[15][Code.F] = new Behave(16);
		   
		   ACTION[0][Code.ID-5] = new Behave(Behave.s,1);
		   ACTION[1][Code.Eval-5] = new Behave(Behave.s,3);
		   ACTION[2][Code.Dollar-5] = new Behave(Behave.acc,-1);
		   ACTION[3][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[3][Code.Left-5] = new Behave(Behave.s,7);
		   ACTION[4][Code.Add-5] = new Behave(Behave.s,9);
		   ACTION[4][Code.Sub-5] = new Behave(Behave.s,10);
		   ACTION[4][Code.Dollar-5] = new Behave(Behave.r,1);
		   
		   ACTION[5][Code.Right-5] = new Behave(Behave.r,4);
		   ACTION[5][Code.Add-5] = new Behave(Behave.r,4);
		   ACTION[5][Code.Sub-5] = new Behave(Behave.r,4);
		   ACTION[5][Code.Muti-5] = new Behave(Behave.s,13);
		   ACTION[5][Code.Dollar-5] = new Behave(Behave.r,4);
		   
		   ACTION[6][Code.Right-5] = new Behave(Behave.r,7);
		   ACTION[6][Code.Add-5] = new Behave(Behave.r,7);
		   ACTION[6][Code.Sub-5] = new Behave(Behave.r,7);
		   ACTION[6][Code.Muti-5] = new Behave(Behave.r,7);
		   ACTION[6][Code.Div-5] = new Behave(Behave.r,7);
		   ACTION[6][Code.Dollar-5] = new Behave(Behave.r,7);
		   
		   ACTION[7][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[7][Code.Left-5] = new Behave(Behave.s,7);
		   
		   ACTION[8][Code.Right-5] = new Behave(Behave.r,9);
		   ACTION[8][Code.Add-5] = new Behave(Behave.r,9);
		   ACTION[8][Code.Sub-5] = new Behave(Behave.r,9);
		   ACTION[8][Code.Muti-5] = new Behave(Behave.r,9);
		   ACTION[8][Code.Div-5] = new Behave(Behave.r,9);
		   ACTION[8][Code.Dollar-5] = new Behave(Behave.r,9);
		   		   
		   ACTION[9][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[9][Code.Left-5] = new Behave(Behave.s,7);
		   ACTION[10][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[10][Code.Left-5] = new Behave(Behave.s,7);
		   
		   ACTION[11][Code.Right-5] = new Behave(Behave.r,2);
		   ACTION[11][Code.Add-5] = new Behave(Behave.r,2);
		   ACTION[11][Code.Sub-5] = new Behave(Behave.r,2);
		   ACTION[11][Code.Muti-5] = new Behave(Behave.s,13);
		   ACTION[11][Code.Div-5] = new Behave(Behave.s,15);
		   ACTION[11][Code.Dollar-5] = new Behave(Behave.r,2);
		   
		   ACTION[12][Code.Right-5] = new Behave(Behave.r,3);
		   ACTION[12][Code.Add-5] = new Behave(Behave.r,3);
		   ACTION[12][Code.Sub-5] = new Behave(Behave.r,3);
		   ACTION[12][Code.Muti-5] = new Behave(Behave.s,13);
		   ACTION[12][Code.Div-5] = new Behave(Behave.s,15);
		   ACTION[12][Code.Dollar-5] = new Behave(Behave.r,3);
		   
		   ACTION[13][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[13][Code.Left-5] = new Behave(Behave.s,7);
		   
		   ACTION[14][Code.Right-5] = new Behave(Behave.r,5);
		   ACTION[14][Code.Add-5] = new Behave(Behave.r,5);
		   ACTION[14][Code.Sub-5] = new Behave(Behave.r,5);
		   ACTION[14][Code.Muti-5] = new Behave(Behave.r,5);
		   ACTION[14][Code.Div-5] = new Behave(Behave.r,5);
		   ACTION[14][Code.Dollar-5] = new Behave(Behave.r,5);
		   
		   ACTION[15][Code.ID-5] = new Behave(Behave.s,8);
		   ACTION[15][Code.Left-5] = new Behave(Behave.s,7);
		   
		   ACTION[16][Code.Right-5] = new Behave(Behave.r,6);
		   ACTION[16][Code.Add-5] = new Behave(Behave.r,6);
		   ACTION[16][Code.Sub-5] = new Behave(Behave.r,6);
		   ACTION[16][Code.Muti-5] = new Behave(Behave.r,6);
		   ACTION[16][Code.Div-5] = new Behave(Behave.r,6);
		   ACTION[16][Code.Dollar-5] = new Behave(Behave.r,6);
		   
		   ACTION[17][Code.Right-5] = new Behave(Behave.s,18);
		   ACTION[17][Code.Add-5] = new Behave(Behave.s,9);
		   ACTION[17][Code.Sub-5] = new Behave(Behave.s,10);
		   
		   ACTION[18][Code.Right-5] = new Behave(Behave.r,8);
		   ACTION[18][Code.Add-5] = new Behave(Behave.r,8);
		   ACTION[18][Code.Sub-5] = new Behave(Behave.r,8);
		   ACTION[18][Code.Muti-5] = new Behave(Behave.r,8);
		   ACTION[18][Code.Div-5] = new Behave(Behave.r,8);
		   ACTION[18][Code.Dollar-5] = new Behave(Behave.r,8);
	   }
	
	   public static int[][]  generator=new int[][] {        //存放产生式  第一个数字代表产生式左部
		   {Code.S1, Code.S},
		   {Code.S, Code.ID, Code.Eval, Code.E},
		   {Code.E, Code.E, Code.Add, Code.T},
		   {Code.E, Code.E, Code.Sub, Code.T},
		   {Code.E, Code.T},
		   {Code.T, Code.T, Code.Muti, Code.F},
		   {Code.T, Code.T, Code.Div, Code.F},
		   {Code.T, Code.F},
		   {Code.F, Code.Left, Code.E, Code.Right},
		   {Code.F, Code.ID}
	    };
	    
	    public static int Getlength(int gen_num) {       //计算产生式右部的待规约串的长度
	    	return generator[gen_num].length-1;
	    	
	    }
	    
	    public static int GetNT(int gen_num) {       //返回产生式左部文法符号的code
	    	return generator[gen_num][0];
	    	
	    }
	    
	    public static void PrintGenerator(int gen_num) {
	    	for(int i = 0;i<generator[gen_num].length;i++) {
	    		System.out.print(Code.toString(generator[gen_num][i]));
	    		if(i == 0) System.out.print("->");
	    	}
	    	System.out.print("\n");    	
	    }
	    
	    public boolean HasItemSet(ArrayList<Item> itemset) {    //看状态表里有没有该状态
	    	for(int i=0;i<itemsetfamily.size();i++) {
	    		if(IsEqual(itemsetfamily.get(i),itemset)) return true;
	    	}    	
	    	return false;	    	
	    }
	    
	    public boolean IsEqual(ArrayList<Item> I1, ArrayList<Item> I2) {
	    	if(I1.size()!=I2.size()) return false;
	    	for(int i=0;i<I1.size();i++) {
	    		int flag=0;
	    		for(int j=0;j<I2.size();j++) {
	    			if(I1.get(i).equals(I2.get(j))) {flag=1;break;}
	    		}
	    		if(flag == 0) return false;
	    	}
	    	return true;
	    }
	    
	    
	    public boolean IsNT(int num) {
	    	if(num>=0 && num<=4) return true;
	    	return false;
	    	
	    }
	    
	    public boolean IsT(int num) {
	    	if(num>=5&&num<=12) return true;
	    	return false;
	    	
	    }
	    
	    public int NextNT(Item item) {   //计算并返回该状态下一个符号
	    	int item_num;
	    	
	    	if(item.dot_position+1 == generator[item.generator_num].length)  return -1;    //待规约状态
	    	
	    	item_num= generator[item.generator_num][item.dot_position+1];
	    	if(IsNT(item_num)){
	    		return item_num;
	    	}
	    	
	    	return -2;   
	    }
	    
	    public int NextT(Item item) {   //计算并返回该状态下一个符号
	    	int item_num;
	    	
	    	if(item.dot_position+1 == generator[item.generator_num].length)  return -1;    //待规约状态
	    	
	    	item_num= generator[item.generator_num][item.dot_position+1];
	    	if(IsT(item_num)){
	    		return item_num;
	    	}
	    	
	    	return -2;   
	    }
	    
	    public int NextToken(Item item) {   //计算并返回该状态下一个符号

	    	if(item.dot_position+1 == generator[item.generator_num].length)  return -1;    //待规约状态
	    	   	
	    	return 	generator[item.generator_num][item.dot_position+1];
	    }
	    
	    public void connect(ArrayList<Item> I1, ArrayList<Item> I2){    //将两个项集的内容合起来
	       System.arraycopy(I2, 0, I1, I1.size(), I2.size());

	    }
	    
	    
	    public void add_initial_state(ArrayList<Item> I,int X) {   //将文法符号X对应产生式的初始状态加入到项集I中  
	    	for(int i=0;i<gen_num;i++) {
	    		if(generator[i][0] == X) {  //找到匹配的产生式左部
	    			I.add(new Item(i,0));
	    		}	    			    		
	    	}	    	
	    }
	    
	    
	    
	    
	public ArrayList<Item> closure(ArrayList<Item> itemset) {       //计算项集itemset的闭包   设置一个
		boolean[] added = new boolean [NTnum];  //布尔数组  该数组的下标是非终结符号的编号 , 用于判断该其产生式对应的初始状态是否被加入项集
		Item temp =null;
		ArrayList<Item> closure_item = new ArrayList<Item>(); 
		
	
		for(int i=0;i<itemset.size()-1;i++) {      //先将项集中的所有项加入它的闭包中，并同时更新added数组
			int dot_pos,gen_num;
			
			temp = itemset.get(i);
			closure_item.add(temp);
			
			gen_num = temp.generator_num;
			dot_pos = temp.dot_position;
			
			added[generator[gen_num][0]] = (dot_pos == 0? true:added[generator[gen_num][0]]);   
			
		}
		
		int NT;
		for(int i=0;i<=closure_item.size()-1;i++) {		
			temp=closure_item.get(i);
			if((NT = NextNT(temp))>=0) {     //如果该状态点的右边符号是非终结符号,将所有它的产生式的初始状态加入闭包
				added[NT] = true;
				add_initial_state(closure_item, NT);
			}
		}
		return closure_item;
	}
	
	public ArrayList<Item> closure(Item item){     //重载  计算项item的闭包
		ArrayList<Item> closure_item = new ArrayList<Item>(); 
		closure_item.add(item);
		Item temp = null;
		int NT;
		
		for(int i=0;i<=closure_item.size()-1;i++) {
			temp=closure_item.get(i);
			if((NT = NextNT(temp))>=0) {     //如果该状态点的右边符号是非终结符号,将所有它的产生式的初始状态加入闭包
				add_initial_state(closure_item, NT);
			}
		}
		
		return closure_item;
		
	}
	
	  
	
	public ArrayList<Item> GOTO(ArrayList<Item> I,int X) {    //I是一个项集,X是一个文法符号   返回I面临文法符号X时的状态变换
		
		ArrayList<Item> closure_item = new ArrayList<Item>(); 
	    
		for(int i=0;i<=I.size()-1;i++) {
			if(NextToken(I.get(i)) == X){
				ArrayList<Item> t = new  ArrayList<Item>();
				I.get(i).dot_position++;
				t.add((I.get(i)));
				connect(closure_item,closure(t));
			}
		}
		return closure_item;
	}
	/*
	public void generate_itemset() {
		Item init_state = new Item(0, 0);
		itemsetfamily.add(closure(init_state));
		
		while(true) {
			for(int j = 0;j<itemsetfamily.size();j++) {   //处理goto表	
				for(int i=Code.S1;i<=Code.F;i++) { 	
					ArrayList<Item> a = GOTO(itemsetfamily.get(j), i);
					if(!HasItemSet(a)){  //如果没有这个项集
						itemsetfamily.add(a);
						analyse_table[itemsetfamily.size()-1][i] = new Behave(Behave.GOTO, itemsetfamily.size()-1);
					}
				}
			}
			
			for(int i=0;i<itemsetfamily.size();i++) {    //处理action表
				for(int j=Code.ID;j<=Code.Dollar;j++) {
					for(Item I:itemsetfamily.get(i)) {
						//GOTO(I, NextT(I))
						if(NextT(I) == -1) {
							
						}
						else if(NextT(I)>=0&&) {
							
						}
					}
					
				}
			}
		}
		
		
		
	}*/
	
	
}









