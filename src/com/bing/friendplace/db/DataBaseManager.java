package com.bing.friendplace.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DataBaseManager {

	private static SQLiteDatabase db;

	public static SQLiteDatabase getSqLiteDatabase(Context context) {
		if (db == null) {
			FriendDbHelper friendDbHelper = new FriendDbHelper(context);
			db = friendDbHelper.getWritableDatabase();
		}

		return db;

	}

}
