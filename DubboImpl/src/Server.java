import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	
	public  static void main(String[] args) throws Exception{
		ServerSocket server = new ServerSocket(1211);
		for(;;) {
		final Socket socket = server.accept(); 
		final HelloService service  = new HelloServiceImpl();
		new Thread(new Runnable() {
		@Override
		public void run() {
			try {
				ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
				String methodName = input.readUTF();
				Class<?>[] parameterTypes = (Class<?>[])input.readObject();
				Object[] arguments = (Object[])input.readObject();
				ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
				Method method = service.getClass().getMethod(methodName,parameterTypes);
				Object result = method.invoke(service, arguments);
				output.writeObject(result);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		} ).start();
		}}
}
