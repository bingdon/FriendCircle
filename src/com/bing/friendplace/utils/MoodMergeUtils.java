package com.bing.friendplace.utils;

import java.util.ArrayList;
import java.util.List;

import com.bing.bean.MoodBean;

public class MoodMergeUtils {

	public static synchronized void mergeMood(List<MoodBean> realList,
			List<MoodBean> templeList) {
		
		List<MoodBean> list = new ArrayList<>();
		int length = templeList.size();

		if (realList.size() == 0) {
			realList.addAll(templeList);
			return;
		}

		MoodBean moodBean = realList.get(0);

		for (int i = 0; i < length; i++) {
			if (moodBean.getCreatetime().equals(
					templeList.get(i).getCreatetime())) {
				break;
			} else {
				list.add(templeList.get(i));
			}
		}

		realList.addAll(0, list);

	}

}
