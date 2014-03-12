package inf.impl;
import inf.ActionListener;
import inf.EventHandler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import model.ActionEvent;
import model.EnumEventSourceSign;


public class EventHandlerImpl implements EventHandler{

	
	
	public List<ActionListener> listeners;
	
	private LinkedHashMap<EnumEventSourceSign, List<ActionListener>> m_listeners;
	@Override
	public void fireEvent(Object source, EnumEventSourceSign sourceSign,
			String action) {
		
		List<ActionListener> als = m_listeners.get(sourceSign);
		
		if(als!=null){
			ActionEvent<Object> event = new ActionEvent<Object>(source, action);
			for(ActionListener al : als){
				al.onAction(event);
			}
		}
	}

	public void init(){
		 m_listeners = new LinkedHashMap<EnumEventSourceSign, List<ActionListener>>();
		 
		 if(listeners!=null){  
			 for(ActionListener a : listeners){
				 List<ActionListener> as = m_listeners.get(a.getSourceSign());
				 
				 if(as==null){
					 as = new ArrayList<ActionListener>();
					 m_listeners.put(a.getSourceSign(), as);  
				 }
				 
				 as.add(a);
			 }
		 }
	}
	
	public void setListeners(List<ActionListener> listeners){
		this.listeners=listeners;
	}

	@Override
	public void addActionListener(ActionListener al) {
		List<ActionListener> s = m_listeners.get(al.getSourceSign());
		if(s==null){
			s = new ArrayList<ActionListener>();
			m_listeners.put(al.getSourceSign(), s);
		}
		s.add(al);
	}
	
}
