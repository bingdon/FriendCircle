package com.bing.bean;

import java.io.Serializable;

public class UserBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7932248789954130375L;

	private String uid;

	private String username;

	private String headimage;

	private String nickname;

	public String getNickname() {
		return "" + nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getHeadimage() {
		return "" + headimage;
	}

	public void setHeadimage(String headimage) {
		this.headimage = headimage;
	}

	public String getUid() {
		return ""+uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return "" + username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String info = "uid=" + uid + ",username=" + username + ",headimage="
				+ headimage + ",nickname=" + nickname;
		return info;
	}

}
