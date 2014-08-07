package com.bing.friendplace.utils;

import com.bing.app.FriendApp;
import com.bing.friendplace.R;

public class VisibleParseUtils {

	private static final String PUBLIC = FriendApp.getInstance().getString(
			R.string.publicc);

	private static final String PRIVATE = FriendApp.getInstance().getString(
			R.string.privatee);

	public static String getVisible(int type) {
		String typestr = PUBLIC;
		switch (type) {
		case 0:
			typestr = PRIVATE;
			break;

		case 1:
			typestr = PUBLIC;
			break;

		default:
			break;
		}

		return typestr;

	}

}
