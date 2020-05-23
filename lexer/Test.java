package lexer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import LL1.PredictTable;
import 递归下降子程序.RecurDesc;
public class Test {

	public static void main(String[] args) {
		Lexer l=new Lexer();
		try {
			l.TokenScan();
		} catch (IOException e) {
			e.printStackTrace();
		}
		l.printlist();
		
		/* 测试递归下降
		 * RecurDesc r=new RecurDesc(l.tokenlist);
		r.E();
		if(r.flag==0) System.out.print("match success!!!");
		else System.out.print("match failed!!!");
		*/  
		
		//测试LL（1）语法分析
		PredictTable  p=new PredictTable();
		p.analyze(l.tokenlist);
	}

}