package inf.impl;

import model.ActionEvent;
import model.Blog;

public class BlogEventListener extends BlogBaseEventListener{

	@Override
	public void comment(ActionEvent<Blog> e) {
		
		Blog s = e.getSource();
		
		System.out.println(s);
	}

}
