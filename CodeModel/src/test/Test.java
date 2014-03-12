
package test;

import dw.sample.Singleton;

public class Test {

    private static Singleton singleton;

    public Test(String constructP) {
        if (singleton == null) {
            singleton = Singleton.getInstance();
        }
    }

}
