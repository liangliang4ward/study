package fastclass;

import java.lang.reflect.InvocationTargetException;

import net.sf.cglib.reflect.FastClass;

public class TestFastClass1 {
	public static void main(String[] args) throws InvocationTargetException {
		FastClass s = FastClass.create(TestInnerClass.class);
		s.newInstance();
	}
}
