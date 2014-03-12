

import java.awt.*;
import java.awt.event.*;

public class DeadLock extends Frame {
	protected static final String[] names = { "One", "Two" };// 创建一个字符串数组，用于存放线程的名字

	private int accounts[] = { 1000, 1000 };// 存入账号

	// 创建TextArea组件
	private TextArea info = new TextArea(5, 40);

	private TextArea status = new TextArea(5, 40);

	public DeadLock() {// 构造方法
		super("致命的死锁！");// 调用父类Frame的带参构造方法
		this.setLayout(new GridLayout(2, 1));
		add(makePanel(info, "账号"));
		add(makePanel(status, "线程"));
		validate();
		pack();
		show();
		// 创建DeadLockThread对象
		DeadLockThread A = new DeadLockThread(0, this, status);
		DeadLockThread B = new DeadLockThread(1, this, status);
		this.addWindowListener(new WindowAdapter() {// 添加单击事件监听
					public void windowClosing(WindowEvent e) {
						System.exit(0);
					}
				});
	}

	public synchronized void transfer(int from, int into, int amount) {// 转账
		info.append("\n帐户 One:$" + accounts[0]);// 将给定文本追加到文本区的当前文本
		info.append("\n帐户 Two:$" + accounts[1]);
		info.append("\n>=$" + amount + "从" + names[from] + "到" + names[into]);
		while (accounts[from] < amount) {
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			accounts[from] = amount;
			accounts[into] = amount;
			notify();
		}
	}

	private Panel makePanel(TextArea ta, String title) {// 创建面板，按选择的布局方式将组件进行布局
		Panel p = new Panel();
		p.setLayout(new BorderLayout());
		p.add("North", new Label(title));
		p.add("Center", ta);
		return p;
	}

	public static void main(String[] args) {// 本程序的主方法
		DeadLock dl = new DeadLock();
	}
}

class DeadLockThread extends Thread {// 死锁线程
	private DeadLock dl;

	private int id;

	private TextArea display;

	public DeadLockThread(int _id, DeadLock _dl, TextArea _display) {
		dl = _dl;
		id = _id;
		display = _display;
		start();
	}

	public void run() {
		while (true) {
			int amount = (int) (1500 * Math.random());
			display.append("\nThread" + DeadLock.names[id] + "将 $" + amount
					+ "存入" + DeadLock.names[(1 - id)]);
			try {
				sleep(20);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			dl.transfer(id, 1 - id, amount);
		}
	}
}
