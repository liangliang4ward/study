package test_;
import test_.serial.SerialBean;
    /**
     *
     * This is an example of how to use the SerialBean. It opens COM1 and reads
     * six messages with different length form the serial port.
     *
     */
    class SerialExample
    {
        public static void main(String[] args)
        {
            //TO DO: Add your JAVA codes here
        	
            SerialBean SB = new SerialBean(1);
            String Msg;
            
            SB.Initialize();
            
            for (int i = 5; i <= 10; i++)
            {
            	System.out.println("zx");
            	Msg = Integer.toString(i);
            	SB.WritePort( Msg);
            	/*flush();*/
            	Msg = SB.ReadPort(1);
                //SB.WritePort( Msg);
               
                System.out.println(Msg);
            }
            SB.ClosePort();
            System.out.println("½áÊø");
        }
    }
