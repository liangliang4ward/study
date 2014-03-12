   
  
import java.net.MalformedURLException;   
import java.rmi.Naming;   
import java.rmi.RemoteException;   
import java.rmi.registry.LocateRegistry;   
  
  
  
public class RmiSampleServer {   
  
    /**  
     * @param args  
     */  
    public static void main(String[] args) {   
        try{   
            LocateRegistry.createRegistry(8808);   
            RmiSampleImpl server= new RmiSampleImpl();   
            Naming.rebind("//localhost:8808/SAMPLE-SERVER" , server);   
        }catch (MalformedURLException me){   
            System.out.println("Malformed URL: " + me.toString());   
        }catch(RemoteException re){   
            System.out.println("Remote Exception: "+re.toString());   
        }   
    }   
  
}   