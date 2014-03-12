package _reflect;

public class Run {
	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
		
		
		Class<?> clazz = Run.class.getClassLoader().loadClass("_reflect.Persion");
		_reflect.Persion h = (Persion) clazz.newInstance();
		h.getName();
		
		
	}
}
