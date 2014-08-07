package com.bing.bean;


public class FriendBean extends UserBean {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String headimage;
	
	private String viewlevel;
	
	private String pinyin;
	
	private boolean isSec;

	public boolean isSec() {
		return isSec;
	}

	public void setSec(boolean isSec) {
		this.isSec = isSec;
	}

	public String getPinyin() {
		return pinyin;
	}

	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}

	public String getHeadimage() {
		return headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getViewlevel() {
		return viewlevel;
	}

	public void setViewlevel(String viewlevel) {
		this.viewlevel = viewlevel;
	}
	
	

}
