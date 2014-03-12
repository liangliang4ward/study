package _volatile;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class Test1 {
	private volatile String left, right;

	private static final AtomicReferenceFieldUpdater leftUpdater = AtomicReferenceFieldUpdater
			.newUpdater(String.class, String.class, "left");
	private static AtomicReferenceFieldUpdater rightUpdater = AtomicReferenceFieldUpdater
			.newUpdater(String.class, String.class, "right");

	String getLeft() {
		return left;
	}

	boolean compareAndSetLeft(String expect, String update) {
		return leftUpdater.compareAndSet(this, expect, update);
	}
	
	public static void main(String[] args) {
		Test1 t = new Test1();
		t.compareAndSetLeft("5", "1");
		System.out.println(t.getLeft());
	}
	

}
