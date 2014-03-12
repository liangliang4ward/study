import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;


public class JavassistRun {
	
	
	public static void main(String[] args) throws Throwable{
		ClassPool cp = ClassPool.getDefault();
		
		Translator xlat = new VerboseTranslator();
		Loader l  = new Loader(cp);
		l.addTranslator(cp, xlat);
		
        l.run("HelloWorld", new String[]{});
	}
	
	
	public static class VerboseTranslator implements Translator
	{
	    public void start(ClassPool pool) {}
	    

		@Override
		public void onLoad(ClassPool pool, String classname)
				throws NotFoundException, CannotCompileException {
			if(classname.equals("HelloWorld")){
				CtClass s = pool.get(classname);
			}
	        System.out.println("onLoad called for " + classname);

		}
	}
}
