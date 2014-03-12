package model;

import inf.ActionListener;
import inf.EventHandler;
import inf.impl.EventHandlerImpl;

public class Action {
	private EventHandler eh = new EventHandlerImpl();  
	
	
	public void setEventHandler(EventHandler eh){
		this.eh = eh;
	}
	
	
	public void write(){
		
		Blog b = new Blog();
		b.name = "blog";
		eh.fireEvent(b, EnumEventSourceSign.blog, "comment");
	}
	
	public void addActionListener(ActionListener al){
		eh.addActionListener(al);
	}
	
}
