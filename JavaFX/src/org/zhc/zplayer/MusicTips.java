package org.zhc.zplayer;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.stage.Popup;
import javafx.stage.Screen;

import org.zhc.zplayer.utils.Locals;
import org.zhc.zplayer.utils.ResourceManager;

public final class MusicTips{
   private static MusicTips tips;
   static boolean contextmenushow;
   
   static{
	contextmenushow=false;
	tips=new MusicTips();
   }
   
   Popup popup;
   Group content;
   ImageView pbg;
	  
   public MusicTips(){
	 popup=new Popup();
	 popup.setAutoHide(true);
	 popup.setAutoFix(true);
	 popup.setHideOnEscape(false);
		
	 initLayout();
   }
   
   public static MusicTips getMusicTips(){
	  return tips;
    }
	  
   private void initLayout(){
	 content=new Group();
	 
	 pbg=ImageViewBuilder.create()
		.fitHeight(102).fitWidth(257)
		.build();
		
	 ImageView head=ImageViewBuilder.create()
		.id("tips_image")
		.image(ResourceManager.loadClasspathImage("singer.png"))
		.layoutX(15).layoutY(25)
		.build();
			 
	 Label text=LabelBuilder.create()
		.alignment(Pos.CENTER_LEFT)
		.layoutX(70).layoutY(20)
		.build();
		
	 Label infomation=LabelBuilder.create()
		.layoutX(70).layoutY(55)
		.build();
		
	 content.getChildren().addAll(pbg,head,text,infomation);
	 popup.getContent().add(content);
	}
	  
	void attach(MusicInfo info,Node target){
	  if(info==null) return ;
		
	  if(contextmenushow||popup.isShowing()) return ;
		
	  Label text=(Label)content.getChildren().get(2);
	  text.setText(info.getFullname());
		
	  Label information=(Label)content.getChildren().get(3);
	  information.setText("大小:"+info.getFileSize()+"\t文件格式:"+info.getFormat()
				+"\n码率:000 Kbps  播放次数:10");
		
	  Screen screen=Screen.getPrimary();
	  double shiftTop=32.0-15;
	  if(Locals.getRightScreen(target)+260>screen.getBounds().getWidth()){
		  pbg.setImage(ResourceManager.loadClasspathImage("tips_left.png"));
		  popup.show(ZPlayer.stage, Locals.getLeftScreen(target)-255,
				   Locals.getTopScreen(target)-shiftTop);
	  }else{
		  pbg.setImage(ResourceManager.loadClasspathImage("tips_right.png"));
		  double right=Locals.getRightScreen(target);
		   //当ListView有Slider时
		  if(target.getLayoutBounds().getWidth()==284.0){
			 right+=13.0;
		    }
		   
		  //这里把stage改为Node也是一样的,但歌曲分组(实际即为TitledPane)可能被删除
		  //而popup持有了MusicCell引用(getOwnerNode()),则其不能被垃圾回收
		  popup.show(ZPlayer.stage, right,Locals.getTopScreen(target)-shiftTop);
	   }
	 }
	  
	void detach(){
	  if(!popup.isShowing()) return ;
		
	  popup.hide();
	}
	  
}