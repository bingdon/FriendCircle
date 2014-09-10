package com.bing.bean;

import java.io.Serializable;

public class CommentBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2811114415691404360L;
	
	
	private String id;
	
	private String content;
	
	private String createtime;
	
	private UserBean user;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return ""+content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCreatetime() {
		return ""+createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public UserBean getUser() {
		if (user==null) {
			user=new UserBean();
		}
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}
	
	
}
