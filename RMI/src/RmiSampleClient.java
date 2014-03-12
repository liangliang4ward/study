import java.rmi.Naming;   
import java.rmi.RemoteException;   
import java.util.Map;
  
  
  
public class RmiSampleClient {   
  
    /**  
     * @param args  
     */  
    public static void main(String[] args) {   
        try {   
            String url = "//localhost:8808/SAMPLE-SERVER";   
            RmiSample RmiObject = (RmiSample) Naming.lookup(url);   
            System.out.println(" 1 + 2 = " + RmiObject.work(new Worker()));   
            Map<String, String> maps = RmiObject.getMaps();
            System.out.println(maps);

        } catch (RemoteException rex) {   
            System.out.println("Error in lookup: " + rex.toString());   
        } catch (java.net.MalformedURLException me) {   
            System.out.println("Malformed URL: " + me.toString());   
        } catch (java.rmi.NotBoundException ne) {   
            System.out.println("NotBound: " + ne.toString());   
        }   
  
    }   
  
}   