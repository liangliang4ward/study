import java.util.ArrayList;
import java.util.List;

public class Subject {
	private List<Listener> listeners;
	private Object value;
	
	public Subject(){
		listeners=new ArrayList<Listener>();
		value=new Object();
	}
	
	public void registe(Listener listener){
		listeners.add(listener);
	}
	
	public void unRegiste(Listener listener){
		listeners.remove(listener);
	}
	
	private void sendNotify(){
		for(Listener listener:listeners){
			listener.onNotify();
		}
	}
	
	public Object getValue(){
		return value;
	}
	
	public void setValue(Object value){
		if (!this.value.equals(value)){
			this.value=value;
			sendNotify();
		}
	}
}
