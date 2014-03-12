package org.zhc.zplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.ImageView;
import javafx.scene.image.ImageViewBuilder;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;

import org.zhc.zplayer.utils.ResourceManager;
import org.zhc.zplayer.utils.StringUtils;

public class ListContainer{
   private ListView<MusicInfo> listView;
   //这里不能持有MusicCell引用，否则垃圾回收器不能回收引用
   // List<MusicCell> cells;
   private StringProperty groupName;
   List<MusicInfo> cache;
   
   public ListContainer(String listName,List<MusicInfo> data){
	 this.groupName=new SimpleStringProperty(listName);
	 this.listView=new ListView<MusicInfo>();
	 this.listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
	 this.listView.setItems(FXCollections.observableArrayList(data));
	 this.listView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
	 
	 initCellFactory();
    }
   
   public String getGroup(){
	 return groupName.get();   
    }
   
   public StringProperty groupProperty(){
	  return this.groupName;   
    }
   
   public void setDataGroup(String group){
	 for(MusicInfo tempInfo:listView.getItems()){
	    tempInfo.group=group;  
	  }
    }
   
   public void filterData(List<MusicInfo> srs){
	 if(cache==null){
	   cache=new ArrayList<MusicInfo>();
	   cache.addAll(listView.getItems());
	  }
	 
	 if(srs==null){
	    srs=Collections.emptyList();
	  }
	 
	 listView.getItems().setAll(srs);
    }
   
   public void backData(){
	 if(cache!=null){
	   listView.getItems().setAll(cache);
	   cache.clear();
	   cache=null;
	 }
    }
   
   public void sortData(){
     Comparator<MusicInfo> comparator=new Comparator<MusicInfo>(){
	   public int compare(MusicInfo one, MusicInfo two){
	      return StringUtils.compare(one.getFullname(),two.getFullname());
		}
	  };
	 
	 Collections.sort(listView.getItems(),comparator);
    }
   
   void initCellFactory(){
	 listView.setCellFactory(new Callback<ListView<MusicInfo>, ListCell<MusicInfo>>(){
		public ListCell<MusicInfo> call(ListView<MusicInfo> view){
		  return new MusicCell();
		 }
	  });
   }
   
   public ListView<MusicInfo> getView(){
	 return listView;   
    }
   
   //此方法触发ListView重新render可见的ListCell(调用其updateItem方法),
   //而又不能持有所有负责render的ListCell(因为它会动态的重用,释放和创建),这样垃圾回收器不能回收不再使用的ListCell引用
   //因为无法根据Item获得render它的的ListCell(既不能调用其updateItem方法)
   public void updateBigCell(){
	   MusicInfo info=listView.getItems().remove(0);
	   listView.getItems().add(0, info);
//	   listView.requestLayout();    此方法不能触发重新render
	   listView.scrollTo(MusicInfo.myindex);
	   System.gc();
    }
   
//   public void cancleBigCell(){
//	  for(MusicCell cell:cells){
//		 if("big-cell".equals(cell.getId())){
//			cell.setId(null);
//			cell.updateItem(cell.getItem(), false);
//			break;
//		   }
//		}
//	 
//	  hasBigCell=false; 
//    }
   
   
  final class MusicCell extends ListCell<MusicInfo> implements EventHandler<MouseEvent>{
	  
	public MusicCell(){
	   setOnMouseClicked(this);
	   setOnMouseEntered(this);
	   setOnMouseExited(this);

//	    cells.add(this);
	  }
		 
     public void updateItem(MusicInfo info,boolean empty){
		super.updateItem(info, empty);

		if(empty){
		  setText(null);
		  setGraphic(null);
		}else{
		  //当当前的歌曲为正在播放的歌曲时
		  MusicInfo index=PlayAccordion.playIndex.get();
		  if(getGroup().equals(index.group)&&getItem()==index){
			setId("big-cell");
//			setGraphic(getInfo(info));
			setGraphic(getInfo2(info));
		  }else{
			setGraphic(getSimpleInfo(info));
		   }
		 }
	   }
	   
     public void handle(MouseEvent event){
	   if(event.getEventType()==MouseEvent.MOUSE_CLICKED){
		  MusicTips.getMusicTips().detach();
		  if(event.getClickCount()==2){
			PlayAccordion.playIndex.set(getItem());
			MusicInfo.myindex=getIndex();
//			updateItem(getItem(),false);
			   
			updateBigCell();
		   }
		}else if(event.getEventType()==MouseEvent.MOUSE_ENTERED){
		   MusicTips.getMusicTips().attach(getItem(),this);
		}else if(event.getEventType()==MouseEvent.MOUSE_EXITED){
		   MusicTips.getMusicTips().detach();
		}
	   }
		
     Node getSimpleInfo(MusicInfo info){
	    int my_index=getIndex()+1;
		Group renVal=new Group();
		Label text=LabelBuilder.create()
		  .alignment(Pos.CENTER_LEFT).layoutY(10)
		  .prefHeight(23+6)
		  .text("  "+(my_index>=10?my_index+"":"0"+my_index)+"  "+info.getFullname())
		  .build();
		  
		Label timeLabel=LabelBuilder.create()
			.alignment(Pos.CENTER_LEFT)
			.layoutX(230).layoutY(10)
			.prefHeight(23+6)
			.text(info.formatDuration())
			.build();
		  
		renVal.getChildren().addAll(text,timeLabel);
		
		if(ResourceManager.getRandom(30)<8){
		   Label mv_btn=LabelBuilder.create()
			 .id("mv_btn")
			 .layoutX(200).layoutY(15)
			 .prefHeight(24).prefWidth(192/8)
			 .build();
		   
		   renVal.getChildren().add(1, mv_btn);
		 }
		  
		return renVal;
	  }
		
	  Node getInfo(MusicInfo info){
		 int my_index=getIndex()+1;
		 Group renVal=new Group();
		 renVal.setStyle("-fx-background-color:rgb(156,202,116);");
		 
		 ImageView head=ImageViewBuilder.create()
			.image(ResourceManager.loadClasspathImage("singer.png"))
			.layoutX(10).layoutY(5)
			.build();
		 
		 Label text=LabelBuilder.create()
			.alignment(Pos.CENTER_LEFT)
			.layoutX(60).layoutY(10)
			.text((my_index>=10?my_index+"":"0"+my_index)+"  "+info.getFullname())
			.build();
		 
		 Label timeLabel=LabelBuilder.create()
			.alignment(Pos.CENTER_LEFT)
			.text(info.formatDuration())
			.layoutX(240).layoutY(10)
			.build();
		 
		 Label mv_btn=LabelBuilder.create()
			.id("mv_btn")
			.layoutX(70).layoutY(32)
			.prefHeight(24).prefWidth(192/8)
			.build();
		 
		 Label add_favorite=LabelBuilder.create()
			.id("add_favorite")
			.layoutX(105).layoutY(32)
			.prefHeight(24).prefWidth(192/8)
			.build();
		 
		 Label report=LabelBuilder.create()
			.id("report")
			.layoutX(140).layoutY(32)
			.prefHeight(24).prefWidth(96/4)
			.build(); 
		
		 renVal.getChildren().addAll(head,text,timeLabel,mv_btn,add_favorite,report);
		 
		 return renVal;
	   }
	  
	  Node getInfo2(MusicInfo info){
		int my_index=getIndex()+1;
		return new BigCell(info, my_index);
	  }
		
	}
   
}
