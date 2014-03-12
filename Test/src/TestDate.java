import java.util.Calendar;


public class TestDate {
	public static void main(String[] args) {
		System.out.println(System.nanoTime()+"-"+System.currentTimeMillis());

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.nanoTime());
		
		System.out.println(c.getTime());
	}
}
