package com.bing.support.http;

import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.CommentBean;
import com.bing.bean.FriendBean;
import com.bing.bean.MoodBean;
import com.bing.bean.UserBean;
import com.bing.support.debug.AppLog;
import com.google.gson.Gson;

public class JsonUtils {

	private static final String TAG = JsonUtils.class.getSimpleName();

	public static boolean isSuccess(JSONObject jsonObject) {
		try {
			int result = jsonObject.getInt("result");
			if (result == 1) {
				return true;
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;

	}

	public static MoodBean getMoodBean(JSONObject object) {
		MoodBean moodBean = new MoodBean();
		try {
			moodBean = new Gson().fromJson(object.toString(), MoodBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(TAG, "解析出错");
		}
		return moodBean;
	}

	public static CommentBean getCommentBean(JSONObject object) {
		CommentBean commentBean = new CommentBean();
		try {
			commentBean = new Gson().fromJson(object.toString(),
					CommentBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(TAG, "解析出错");
		}
		return commentBean;
	}

	public static UserBean getUserBean(JSONObject object) {
		UserBean userBean = new UserBean();
		try {
			userBean = new Gson().fromJson(object.toString(), UserBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(TAG, "解析出错");
		}
		return userBean;
	}

	public static FriendBean getFriendBean(JSONObject object) {
		FriendBean friendBean = new FriendBean();
		try {
			friendBean = new Gson().fromJson(object.toString(),
					FriendBean.class);
		} catch (Exception e) {
			// TODO: handle exception
			AppLog.e(TAG, "解析出错");
		}
		return friendBean;
	}

}
