import java.lang.management.ManagementFactory;


public class Test1 {
	public static void main(String[] args) {
		String name = ManagementFactory.getRuntimeMXBean().getName();
		System.out.println(name);
		
	}
}
