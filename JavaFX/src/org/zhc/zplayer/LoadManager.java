package org.zhc.zplayer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import org.zhc.zplayer.utils.Threads;

public class LoadManager{
   LoadOver callback;
   Map<String, List<MusicInfo>> maps;
   
   public LoadManager(LoadOver over,Map<String, List<MusicInfo>> maps){
	 this.callback=over;
	 this.maps=maps;
    }
   
   public void startWork(){
	 Loader.count.set(0);
	 for(List<MusicInfo> tempList:maps.values()){
		for(MusicInfo tempMusic:tempList){
		  new Loader(tempMusic).parse();
		}
      }
	 
	 new Thread(){
	  public void run(){
		int items=getItems();
		AppConfig.debug("items = "+items);
		
		while(Loader.count.get()!=items){
		   Threads.sleeps(100,TimeUnit.MILLISECONDS);
		   AppConfig.debug("count = "+Loader.count.get());
		 }
		
		AppConfig.debug("load all musics over!");
		if(callback!=null)
          callback.loadOver(); 
	  }
	}.start();
   }
   
    int getItems(){
	  int result=0;
	  for(List<MusicInfo> tempList:maps.values()){
		  result+=tempList.size(); 
	   }
		
	  return result;
	}

   static interface LoadOver{
	 public void loadOver();
   }
   
   private static class Loader implements Runnable{
	static AtomicInteger count=new AtomicInteger(0);
	MediaPlayer player;
	MusicInfo info;
	int tryCount=0;
		  
	public Loader(MusicInfo inf){
	  this.info=inf;
	  this.player=new MediaPlayer(new Media(info.url));
	 }
	
	void parse(){
	  this.player.setOnReady(this);
	  this.player.setOnError(new Runnable(){
		public void run(){
	      System.err.println("Error = "+info.getFullname());
	      if(tryCount>=1){
	    	count.incrementAndGet();
	    	return ;
	      }
	      
	      tryCount++;
	      parse();
	      
		}
	 });
	}
		
	 public void run(){
	   AppConfig.debug("do run = "+info.getFullname());
	   info.time=player.getMedia().getDuration();
	   AppConfig.debug(count.incrementAndGet());
	 }
	
   }
	
}
