package org.zhc.zplayer;

import org.zhc.zplayer.utils.ResourceManager;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class BigCell extends Region{
  Label text,timeLabel,mv_btn,add_favorite,report;
  ImageView head;
  
  public BigCell(MusicInfo info,int index){
	 setStyle("-fx-background-color:rgb(148,202,113);");
	 setPrefHeight(56);
	 
	 this.head=ResourceManager.getViewOfClasspath("singer.png");
	 this.text=LabelBuilder.create()
		.alignment(Pos.CENTER_LEFT)
		.textFill(Color.WHITESMOKE)
		.text((index>=10?index+"":"0"+index)+"  "+info.getFullname())
		.build();
	 
	 timeLabel=LabelBuilder.create()
		.alignment(Pos.CENTER_LEFT)
		.textFill(Color.WHITESMOKE)
		.text(info.formatDuration())
		.build();
	 
	 mv_btn=LabelBuilder.create()
		.id("mv_btn")
		.prefHeight(24).prefWidth(192/8)
		.build();
	 
	 add_favorite=LabelBuilder.create()
		.id("add_favorite")
		.prefHeight(24).prefWidth(192/8)
		.build();
	 
	 report=LabelBuilder.create()
		.id("report")
		.prefHeight(24).prefWidth(96/4)
		.build();
	 
	 getChildren().addAll(head,text,timeLabel,mv_btn,add_favorite,report);
   }
  
  @Override
  protected void layoutChildren(){
	layoutInArea(head, 8, 4, 48, 48, getBaselineOffset(), HPos.LEFT,VPos.TOP);
	layoutInArea(text, 60, 10, 200, 10, getBaselineOffset(), HPos.LEFT,VPos.TOP);
	layoutInArea(timeLabel, 240, 10, 40, 10, getBaselineOffset(), HPos.LEFT,VPos.TOP);
	layoutInArea(mv_btn, 70, 32, 192/8, 24, getBaselineOffset(), HPos.LEFT,VPos.TOP);
	layoutInArea(add_favorite, 105, 32, 192/8, 24, getBaselineOffset(), HPos.LEFT,VPos.TOP);
	layoutInArea(report, 140, 32, 96/4, 24, getBaselineOffset(), HPos.LEFT,VPos.TOP);
   }
 
}
