package inf.impl;

import model.ActionEvent;
import model.Blog;
import model.EnumEventSourceSign;

public abstract class BlogBaseEventListener extends BasicActionListener{
	public final EnumEventSourceSign getSourceSign() {  
		return EnumEventSourceSign.blog;
	}
	
	public abstract void comment(ActionEvent<Blog> e);  
}
