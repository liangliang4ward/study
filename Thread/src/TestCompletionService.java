import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class TestCompletionService {

	public void test() throws InterruptedException, ExecutionException{
		ExecutorService es = Executors.newFixedThreadPool(5);
		List<Person> ps = new ArrayList<Person>();
		for(int i=0;i<10;i++){
			ps.add(new Person(i));
		}
		
		CompletionService<String> ecs = new ExecutorCompletionService<String>(es);
		for(final Person p:ps){
			Future<String> s = ecs.submit(new Callable<String>() {
				
				@Override
				public String call() throws Exception {
					return p.thisA();
				}
			});
			System.out.println(s.get());
		}
		
		for(int i=0;i<10;i++){
			Future<String> s = ecs.take();
			System.out.println(s.get());
		}
	}
	public static void main(String[] args) throws InterruptedException, ExecutionException {
		new TestCompletionService().test();
	}
	
	class Person{
		
		int i;
		public Person(int i){
			this.i=i;
		}
		
		public String thisA(){
			return ""+i;
		}
	}
	
}
