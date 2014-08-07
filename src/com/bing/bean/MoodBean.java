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
	
	private String id;
	
	private String img[];
	
	private CommentBean[] comment;
	
	private UserBean user;
	
	private int viewlevel;
	
	private int collectcount;
	
	private int laudcount;
	
	private int type;

	public int getViewlevel() {
		return viewlevel;
	}

	public void setViewlevel(int viewlevel) {
		this.viewlevel = viewlevel;
	}

	public int getCollectcount() {
		return collectcount;
	}

	public void setCollectcount(int collectcount) {
		this.collectcount = collectcount;
	}

	public int getLaudcount() {
		return laudcount;
	}

	public void setLaudcount(int laudcount) {
		this.laudcount = laudcount;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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
