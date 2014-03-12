package model;

public class ActionEvent<T> {
	private T source;  
	
	private String action;
	
	public ActionEvent(T source,String action){
		this.source=source;
		this.action = action;
	}
	
	public T getSource(){
		return source;
	}
	
	public String getAction(){
		return action;
	}

}
