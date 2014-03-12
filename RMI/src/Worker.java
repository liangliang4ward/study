import java.io.Serializable;



public class Worker implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5051941181879911539L;

	/**
	 * 
	 */

	private String name = "jone";
	
	private int age = 12;
	
	private Tool t =new Tool();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Tool getT() {
		return t;
	}

	public void setT(Tool t) {
		this.t = t;
	} 
	
	public String work(){
		return name+" is "+age+" and "+t.doSth();
	}
	
}
