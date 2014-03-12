package fastclass;

import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastClass;


public class TestFastClass {
	private static final int LOOP = 100000000;
	public static void main(String[] args) throws ClassNotFoundException {

		reflect();
		_new();
		fastClass();
	

	}
	
	public static void printTime(String preFix,long t1,long t2){
		System.out.println(preFix+" "+(t2-t1)+" ms");
	}
	
	public static void _new(){
		long s1 = System.currentTimeMillis();
		
		for(int i=0;i<LOOP;i++){
			Model s = new Model();
		}
		
		
		printTime("_new",s1,System.currentTimeMillis());

	}
	
	public static void reflect(){
		long s1 = System.currentTimeMillis();

		for (int i = 0; i < LOOP; i++) {
			Class c = null;
			try {
				c = Class.forName("fastclass.Model");
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}

//			try {
//				Object s = c.newInstance();
//			} catch (InstantiationException e) {
//				e.printStackTrace();
//			} catch (IllegalAccessException e) {
//				e.printStackTrace();
//			}
		}
		printTime("reflect",s1,System.currentTimeMillis());

	}
	
	public static void fastClass(){
		long s1 = System.currentTimeMillis();


		for(int i=0;i<LOOP;i++){
			FastClass s = FastClass.create(Model.class);
//			try {
//				Object m = s.newInstance();
//			} catch (InvocationTargetException e) {
//				e.printStackTrace();
//			}
		}
		printTime("fastclass",s1,System.currentTimeMillis());
	}
	
	
}
