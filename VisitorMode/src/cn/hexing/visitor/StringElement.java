package cn.hexing.visitor;

public class StringElement implements Visitable

{

	private String value;

	public StringElement(String string) {

		value = string; // 通过构造方法，将String字符串包装起来

	}

	public String getValue() {

		return value;

	}

	public void accept(Visitor visitor) {

		visitor.visit(this); // 实现回调

	}

}