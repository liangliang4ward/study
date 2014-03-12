package cn.hexing.visitor;

public class ConcreteVisitor implements Visitor{

	@Override
	public void visit(StringElement stringE) {
		System.out.println(stringE.getValue());
	}

}
