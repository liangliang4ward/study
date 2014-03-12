package t1;

 

public class TestDemo

{

       DemoSource ds;

 

       public TestDemo()

       {

              try{

                    ds = new DemoSource();

                    Listener1 l1 = new Listener1();

                    Listener2 l2 = new Listener2();

                    Listener3 l3 = new Listener3();

 

                    ds.addDemoListener(l1);

                    ds.addDemoListener(l2);

                    ds.addDemoListener(l3);

                    ds.addDemoListener(new DemoListener(){
                               public void demoEvent(DemoEvent event){
                                         System.out.println("Method come from 匿名类...");
                               }
                       });

                    ds.notifyDemoEvent();

 

              }catch(Exception ex)

              {ex.printStackTrace();}

       }

 

       public static void main(String args[])

       {

              new TestDemo();

       }

}