	
public class TestTime {
	public static void main(String[] args) {
		long t1=System.currentTimeMillis();
		long t2=System.currentTimeMillis();
		long t3=System.nanoTime();
		Long i = new Long(1);
		long t4=System.nanoTime();
		System.out.println("t2-t1="+(t2-t1));
		System.out.println("t4-t3="+(t4-t3));
		
	}
}
