package com.bing.bean;

import java.io.Serializable;

public class MoodBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8365280700828200117L;

	private String createtime;
	
	private String content;
	
	private String uid;
	
	private String img[];
	
	private CommentBean[] comment;
	
	private UserBean user;
	
	

	public UserBean getUser() {
		return user;
	}

	public void setUser(UserBean user) {
		this.user = user;
	}

	public String getCreatetime() {
		return createtime;
	}

	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String[] getImg() {
		return img;
	}

	public void setImg(String[] img) {
		this.img = img;
	}

	public CommentBean[] getComment() {
		return comment;
	}

	public void setComment(CommentBean[] comment) {
		this.comment = comment;
	}
	
}
