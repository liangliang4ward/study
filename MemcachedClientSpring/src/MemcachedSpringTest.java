

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.meetup.memcached.MemcachedClient;

public class MemcachedSpringTest {

	private MemcachedClient cachedClient;
	
	@Before
	public void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:memcached.xml");
		cachedClient = (MemcachedClient)context.getBean("memcachedClient");
	}
	
	@Test
	public void testSetSingle(){
		System.out.println(cachedClient.set("k4", "test4"));
		System.out.println(cachedClient.set("k4", "test3"));

	}
	
	@Test 
	public void testGetSingle(){
		System.out.println(cachedClient.get("k3"));
		System.out.println(cachedClient.get("k4"));
	}
	
	@Test
	public void testSet() {
		
//		System.out.println(cachedClient.get("user"));
//		UserBean user = new UserBean("lou", "jason");
		for(int i=0;i<10;i++){
			int j = (int) ((100-0+1)*Math.random());
			cachedClient.set("k"+j, "v"+j);
		}
//		cachedClient.set("k1", "test1");
//		for(int i=0;i<10000;i++){
//			cachedClient.set("k"+0, "test"+0);	
//		}
		
//		UserBean cachedBean = (UserBean)user;
//		System.out.println(cachedBean.getUsername());
	}
	@Test
	public void testGet(){
//		long t1 = System.currentTimeMillis();
//		for(int i=0;i<10000;i++){
//			System.out.println(cachedClient.get("k"+i));
//		}
//		long t2 = System.currentTimeMillis();
//		System.out.println("use time:"+(t2-t1));

	} 
}