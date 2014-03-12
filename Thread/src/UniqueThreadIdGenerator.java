import java.util.concurrent.atomic.AtomicInteger;

public class UniqueThreadIdGenerator {

     private static final AtomicInteger uniqueId = new AtomicInteger(0);

     private  final ThreadLocal < Integer > uniqueNum = 
         new ThreadLocal < Integer > () {
             @Override protected Integer initialValue() {
                 return uniqueId.getAndIncrement();
         }
     };
 
     public  int getCurrentThreadId() {
         return uniqueNum.get();
     }
     
     public static void main(String[] args) {
    	 UniqueThreadIdGenerator u2 = new UniqueThreadIdGenerator();
    	 UniqueThreadIdGenerator u1 = new UniqueThreadIdGenerator();
    	 System.out.println(u2.getCurrentThreadId());
    	 System.out.println(u1.getCurrentThreadId());
	}
 }
