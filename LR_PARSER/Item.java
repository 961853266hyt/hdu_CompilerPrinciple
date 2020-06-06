package LR_PARSER;
/**
 * @param generator_num(产生式编号)     dot_position(点的位置)
 * 
 * 
 * @author胡逸藤
 */
public class Item {
	int generator_num;  //产生式编号
	int dot_position;   //点的位置  从0开始     *d*d*d*  (0,1,2...产生式长度)
	
	public Item(int generator_num, int dot_position){
		this.generator_num = generator_num;
		this.dot_position = dot_position;
	}
	
	public boolean equals(Item item) {
		return (this.generator_num == item.generator_num&&this.dot_position == item.dot_position);
	}
}
