package cn.hexing.netty.other;

import java.nio.channels.spi.SelectorProvider;

public class TestSelector {
	public static void main(String[] args) {
		
		SelectorProvider s = SelectorProvider.provider();
		System.out.println(s);
	}
}
