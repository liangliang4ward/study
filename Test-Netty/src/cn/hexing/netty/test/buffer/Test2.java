package cn.hexing.netty.test.buffer;

import java.io.IOException;
import java.nio.channels.Selector;

public class Test2 {
	public static void main(String[] args) throws IOException {
		Selector s=Selector.open();
		System.out.println(s);
	}
}
