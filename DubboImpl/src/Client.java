import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

public class Client {

	public static void main(String[] args) {
		HelloService s = getProxy(HelloService.class, "localhost", 1211);
		System.out.println(s.sayHello("RMI"));
	}

	@SuppressWarnings("unchecked")
	public static <T> T getProxy(Class<T> interfaceClass, final String host,
			final int port) {
		return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
				new Class<?>[] { interfaceClass }, new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] arguments) throws Throwable {
						Socket socket = new Socket(host, port);
						ObjectOutputStream output = new ObjectOutputStream(
								socket.getOutputStream());
						output.writeUTF(method.getName());
						output.writeObject(method.getParameterTypes());
						output.writeObject(arguments);
						ObjectInputStream input = new ObjectInputStream(socket
								.getInputStream());
						return input.readObject();
					}
				});
	}
}
