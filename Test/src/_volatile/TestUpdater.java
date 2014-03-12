package _volatile;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * 
 * @author gaoll
 * use AtomicReferenceFieldUpdater
 *
 */
public class TestUpdater {

	
	private volatile Book whatImReading;
	
	private static final AtomicReferenceFieldUpdater<TestUpdater, Book> updater
	=AtomicReferenceFieldUpdater.newUpdater( 
			TestUpdater.class, Book.class, "whatImReading" );
	
	public Book getWhatImReading(){
		return whatImReading;
	}
	
	public void setWhatImReading(Book book){
		updater.compareAndSet(this, this.whatImReading, book);
	}

	public static void main(String[] args) {
		TestUpdater tu = new TestUpdater();
		Book b = new Book();
		b.setName("java thinking");
		tu.setWhatImReading(b);
		
		Book b1 = new Book();
		b1.setName("test");
		tu.setWhatImReading(b1);
		System.out.println(tu.getWhatImReading().getName());
			
	}

	
	private static class Book{
		private String name;
		public String getName(){
			return this.name;
		}
		
		public void setName(String name){
			this.name = name;
		}
		
	}

}

