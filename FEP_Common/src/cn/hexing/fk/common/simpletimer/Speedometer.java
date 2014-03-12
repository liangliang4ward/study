/**
 * 速度计。默认测量每分钟流量。
 * 测速当前平均每分钟流量
 */
package cn.hexing.fk.common.simpletimer;


/**
 */
public class Speedometer implements ITimerFunctor {
	private int pointCount = 60;		//默认每分钟测速。 3600，每小时测速。
	//内部属性
	private int[] mpoints;
	private final Object lock = new Object();
	private int curPosition = 0;
	
	public Speedometer(){
		mpoints = new int[this.pointCount+1];
		for(int i=0; i<mpoints.length; i++ )
			mpoints[i] = 0;
		TimerScheduler.getScheduler().addTimer(new TimerData(this,0,60));
	}
	
	public Speedometer(int pointCount){
		if( pointCount <1 )
			pointCount = 60;
		this.pointCount = pointCount;
		mpoints = new int[this.pointCount+1];
		for(int i=0; i<mpoints.length; i++ )
			mpoints[i] = 0;
		TimerScheduler.getScheduler().addTimer(new TimerData(this,0,60));
	}
	
	private void moveNext(){
		curPosition++;
		if( curPosition > pointCount )
			curPosition = 0;
		mpoints[curPosition] = 0;
	}
	
	public void onTimer(int id) {
		synchronized(lock){
			moveNext();
		}
	}
	
	public void add(int flow){
		synchronized(lock){
			mpoints[curPosition] += flow;
		}
	}
	
	public int getSpeed(){
		int speed = 0;
		synchronized( lock ){
			for(int i=0; i<mpoints.length; i++ ){
				if( i != curPosition )
					speed += mpoints[i];
			}
		}
		return speed;
	}
	public int getSpeed1(){
		int speed = 0;
		speed=mpoints[curPosition];
		return speed;
	}

}
