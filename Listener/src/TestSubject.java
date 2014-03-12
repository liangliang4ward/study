
public class TestSubject {
	public static void main(String[] args) {
		
		Subject s = new Subject();
		s.registe(new Listener() {
			
			@Override
			public void onNotify() {
				System.out.println("I kown. But I don't know.");
			}
		});
		
		s.setValue("123");
		
	}
}
