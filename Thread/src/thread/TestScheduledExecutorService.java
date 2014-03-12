package thread;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class TestScheduledExecutorService {
	private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

	public void beepForAnHour() {
		final Runnable beeper = new Runnable() {
			public void run() {
				System.out.println("beep");
			}
		};
		final ScheduledFuture<?> beeperHandle = scheduler.scheduleAtFixedRate(beeper, 1, 1, TimeUnit.SECONDS);
		scheduler.schedule(new Runnable() {
			public void run() {
				System.out.println("peeb");
			}
		}, 10, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) {
		TestScheduledExecutorService tses = new TestScheduledExecutorService();
		tses.beepForAnHour();
	}
}
