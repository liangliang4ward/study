package inf.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import model.ActionEvent;
import model.EnumEventSourceSign;
import inf.ActionListener;

public abstract class BasicActionListener implements ActionListener{

	@Override
	public void onAction(ActionEvent<?> event) {
		String action = event.getAction().toLowerCase();  
		 Class[] params = new Class[]{event.getClass()};
		 try {
			Method md = this.getClass().getDeclaredMethod(action, params);
			
			md.invoke(this, new Object[]{event});
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} 
		
	}

	@Override
	public abstract EnumEventSourceSign getSourceSign() ;

}
