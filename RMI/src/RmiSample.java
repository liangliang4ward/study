import java.rmi.Remote;   
import java.rmi.RemoteException;   
import java.util.Map;
  
public interface RmiSample extends Remote {   
    public String sum(String key) throws RemoteException;   
    
    public Map<String,String> getMaps() throws RemoteException;
    
    public String work(Worker w) throws RemoteException;
}  