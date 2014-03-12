package inf;
import model.ActionEvent;
import model.EnumEventSourceSign;


public interface ActionListener {
	 public void onAction(ActionEvent<?> event);
	 
	 EnumEventSourceSign getSourceSign();  
}
