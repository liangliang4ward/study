package cn.hexing.fk.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *@filename	Counter.java
 *TODO
 */
public class Counter {
	private static final Log log=LogFactory.getLog(Counter.class);
	public static final long DEFAULT_LIMIT_VAL=1000;
	private long count;		//计数
	private long limit;		//统计触发阈值
	private long time;		//上次统计时间
	private long guard;		
	private long speed;		//每分钟处理数量
	private String name;
	
	public Counter(){
		this(DEFAULT_LIMIT_VAL,"");
	}
	
	public Counter(long limit,String name){
		this.time=System.currentTimeMillis();
		this.count=0;
		this.limit=limit;
		this.guard=0;
		this.speed=0;
		this.name=name;
	}
	
	public synchronized void add(){
		try{
			count++;
			guard++;
			if(guard>=limit){					
				speed=guard*60000L/(System.currentTimeMillis()-time);
				time=System.currentTimeMillis();
				guard=0;
				log.info(" counter--"+name+"'s speed is:"+speed+"/min , sum is "+count);
			}
		}catch(Exception e){
			//如果越界报错则清零处理
			this.count=0;
			this.guard=0;
		}
	}
	
	public synchronized void add(long cc){
		try{
		count+=cc;
		guard+=cc;
		if(guard>=limit){
			long speed=guard*60000L/(System.currentTimeMillis()-time);
			time=System.currentTimeMillis();
			guard=0;
			log.info(" counter--"+name+"'s speed is:"+speed+"/min , sum is "+count);
		}
		}catch(Exception e){
			//
		}
	}

	/**
	 * @return 返回 speed。
	 */
	public long getSpeed() {
		return speed;
	}

	/**
	 * @param speed 要设置的 speed。
	 */
	public void setSpeed(long speed) {
		this.speed = speed;
	}

	/**
	 * @return 返回 count。
	 */
	public long getCount() {
		return count;
	}

	/**
	 * @param count 要设置的 count。
	 */
	public void setCount(long count) {
		this.count = count;
	}
}
