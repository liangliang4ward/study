package t1;

 

import java.util.EventObject;

 

public class DemoEvent extends EventObject

{

        Object obj;

        public DemoEvent(Object source)

        {

               super(source);

               obj = source;

        }

        public Object getSource()

        {

               return obj;

        }

        public void say()

        {

               System.out.println("This is say method...");

        }

}