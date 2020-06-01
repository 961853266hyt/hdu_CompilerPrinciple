package LR_PARSER;

/*
 * 	0  S1     5 i     12 :=
    1  S      6 (     13 $
    2  E      7 )
    3  T      8 +
 *  4  F      9 -
 *            10 *
 *            11 /
 */
 
public class Code {
	public final static int S1=0,S=1,E=2,T=3,F=4,ID=5,Left=6,Right=7,Add=8,Sub=9,Muti=10,Div=11,Eval=12,Dollar=13;
	
	public static String toString(int code) {
		switch(code){
			case S1: return "S1";
			case S: return "S";
			case E: return "E";
			case T: return "T";
			case F: return "F";
			case ID: return "i";
			case Left: return "(";
			case Right: return ")";
			case Add: return "+";
			case Sub: return "-";
			case Muti: return "*";
			case Div: return "/";
			case Eval: return":=";
			case Dollar: return "$";
		}
		return null;		
	}
	
	public static int GetCode(String s) {
		switch(s){
		case "S1": return S1;
		case "S": return S;
		case "E": return E;
		case "T": return T;
		case "F": return F;
		case "ID":return ID;
		case "(": return Left;
		case ")": return Right;
		case "+": return Add;
		case "-": return Sub;
		case "*": return Muti;
		case "/": return Div;
		case ":=":return Eval;
		case "$": return Dollar;
	}
	return -1;	
	}
}
