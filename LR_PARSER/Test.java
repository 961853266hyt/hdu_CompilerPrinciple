package LR_PARSER;

import java.io.IOException;

import lexer.Lexer;

public class Test {

	public static void main(String[] args) {
		Lexer l=new Lexer();
		try {
			l.TokenScan();
		} catch (IOException e) {
			e.printStackTrace();
		}
		l.printlist();
		
		SLR slr = new SLR();
		LR lr = new LR();
		lr.LR_Analyse(slr.GOTO, slr.ACTION, l.tokenlist);
	}

}
