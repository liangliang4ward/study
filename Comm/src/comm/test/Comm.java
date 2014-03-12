package comm.test;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;

public class Comm {
	public static void main(String[] args) throws NoSuchPortException {
		CommPortIdentifier serialPortId = CommPortIdentifier.getPortIdentifier("COM4");
	}
}
