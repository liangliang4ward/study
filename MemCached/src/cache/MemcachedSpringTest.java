package cache;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.danga.MemCached.MemCachedClient;

public class MemcachedSpringTest {

	private MemCachedClient cachedClient;
	
	@Before
	public void init() {
		ApplicationContext context = new ClassPathXmlApplicationContext("classpath*:app.xml");
		cachedClient = (MemCachedClient)context.getBean("memcachedClient");
	}
	
	@Test
	public void testSet() {
		cachedClient.set("", "");
//		System.out.println(cachedClient.get("user"));
//		UserBean user = new UserBean("lou", "jason");
//		for(int i=0;i<10;i++){
//			cachedClient.set("k"+i, "v"+i);
//		}
//		cachedClient.set("k1", "test1");
//		for(int i=0;i<10000;i++){
//			cachedClient.set("k"+i, "test"+i);	
//		}
		
//		UserBean cachedBean = (UserBean)user;
//		System.out.println(cachedBean.getUsername());
	}
	@Test
	public void testGet(){
//		long t1 = System.currentTimeMillis();

//		for(int i=0;i<10000;i++){
			System.out.println(cachedClient.get("k"+0));
//		}
//		long t2 = System.currentTimeMillis();
//		System.out.println("use time:"+(t2-t1));

	} 
}