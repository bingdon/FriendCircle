package com.bing.friendplace.db;

import com.bing.friendplace.db.table.MsgTable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FriendDbHelper extends SQLiteOpenHelper {

	private static final String NAME = "Friend.db";
	private static final int VERSION = 1;

	private static final String CREATE_MSG_TABLE_SQL = "CREATE TABLE "
			+ MsgTable.TABLE_NAME + "(" + MsgTable._ID
			+ " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," + MsgTable.NAME
			+ " TEXT," + MsgTable.MOOD_ID + " TEXT," + MsgTable.CONTENT
			+ " TEXT," + MsgTable.CONTENT_PIC_URL + " TEXT,"
			+ MsgTable.HEAD_URL + " TEXT," + MsgTable.TIME + " TEXT" + ");";

	public FriendDbHelper(Context context) {
		super(context, NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL(CREATE_MSG_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

}
