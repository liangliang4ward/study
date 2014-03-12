package cn.hexing.visitor;

public interface Visitable {
	public void accept(Visitor visitor); //用于接受访问者
}
