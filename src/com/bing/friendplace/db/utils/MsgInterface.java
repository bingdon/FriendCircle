package com.bing.friendplace.db.utils;

import com.bing.bean.MoodBean;

public interface MsgInterface extends BaseDateInterface{

	public long insert(MoodBean moodBean);
	
	public long delete(MoodBean moodBean);
	
	
	
}
