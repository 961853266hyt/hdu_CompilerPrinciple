package LR_PARSER;

public class Behave {
	public static int s=0, r=1, acc=2, GOTO=3;
	public int act;
	public int gen_num;
	public Behave(int act, int gen_num) {
		this.act = act;
		this.gen_num = gen_num;
	}
	public Behave(int gen_num) {
		this.act = GOTO;
		this.gen_num = gen_num;
	}
}
