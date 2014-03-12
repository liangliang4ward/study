package inf;

import model.EnumEventSourceSign;

public interface EventHandler {
	public void fireEvent(Object source, EnumEventSourceSign sourceSign,
			String action);

	public void addActionListener(ActionListener al);
}
