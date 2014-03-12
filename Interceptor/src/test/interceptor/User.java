package test.interceptor;

public class User implements IUser {

	private String name;
	
	
	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

}
