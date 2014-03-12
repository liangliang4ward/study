
package dw.sample;


public class Singleton {

    private static Singleton instance;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    /**
     * This method will say Hello to the name.
     * 
     */
    public void sayHello(String name) {
        System.out.println("Hello ".concat(name).concat("!"));
    }

}
