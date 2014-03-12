import java.util.ArrayList;
import java.util.List;

import model.Action;
import model.ActionEvent;
import model.EnumEventSourceSign;

import inf.ActionListener;
import inf.impl.BlogEventListener;
import inf.impl.EventHandlerImpl;


public class Test {
	public static void main(String[] args) {
		
		EventHandlerImpl eh = new EventHandlerImpl();
		
		
		
		List<ActionListener> listeners = new ArrayList<ActionListener>();
		listeners.add(new BlogEventListener());
		eh.setListeners(listeners );
		eh.init();
		
		Action a = new Action();
		a.setEventHandler(eh);
		a.addActionListener(new ActionListener() {
			
			@Override
			public void onAction(ActionEvent<?> event) {
				System.out.println(event);
			}
			
			@Override
			public EnumEventSourceSign getSourceSign() {
				return EnumEventSourceSign.blog;
			}
		});
		a.write();
		
	}
}
