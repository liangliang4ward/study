
 

package t1;

import java.util.*;

 

public class DemoSource

{

       private Vector repository = new Vector();

       DemoListener dl;

       public DemoSource()

       {

 

       }

       public void addDemoListener(DemoListener dl)

       {

              repository.addElement(dl);

       }

       public void notifyDemoEvent()

       {

              Enumeration enum1 = repository.elements();

              while(enum1.hasMoreElements())

              {

                    dl = (DemoListener)enum1.nextElement();

                    dl.demoEvent(new DemoEvent(this));

              }

       }

}

 