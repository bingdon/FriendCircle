package com.bing.friendplace.db.utils;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bing.bean.MoodBean;
import com.bing.bean.UserBean;
import com.bing.friendplace.db.DataBaseManager;
import com.bing.friendplace.db.table.MsgTable;

public class MsgDataMangerUtils implements MsgInterface {

	private SQLiteDatabase db;

	public MsgDataMangerUtils(Context context) {
		db = DataBaseManager.getSqLiteDatabase(context);
	}

	@Override
	public long delete() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long deleteAll() {
		// TODO Auto-generated method stub
		long del = -1;

		try {
			del = db.delete(MsgTable.TABLE_NAME, null, null);
		} catch (Exception e) {
			// TODO: handle exception
		}

		return del;
	}

	@Override
	public Object queryData() {
		// TODO Auto-generated method stub
		List<MoodBean> moodBeans = new ArrayList<>();
		Cursor cursor = null;
		try {
			// ������ݿ����,������ݿⲻ�����򴴽�
			// ��ѯ��������,��ȡ�α�
			cursor = db.query(MsgTable.TABLE_NAME, null, null, null, null,
					null, "_ID desc");
			// ��ȡmoodid�е�����
			int _idIndex = cursor.getColumnIndex(MsgTable._ID);

			int idIndex = cursor.getColumnIndex(MsgTable.MOOD_ID);
			// ��ȡname�е�����
			int nameIndex = cursor.getColumnIndex(MsgTable.NAME);

			int contentIndex = cursor.getColumnIndex(MsgTable.CONTENT);

			int contentpicIndex = cursor
					.getColumnIndex(MsgTable.CONTENT_PIC_URL);

			int timeIndex = cursor.getColumnIndex(MsgTable.TIME);

			int headurlIndex = cursor.getColumnIndex(MsgTable.HEAD_URL);

			// ������ѯ���������������ȡ����
			for (cursor.moveToFirst(); !(cursor.isAfterLast()); cursor
					.moveToNext()) {
				MoodBean moodBean = new MoodBean();
				moodBean.set_id(cursor.getInt(_idIndex));
				moodBean.setId(cursor.getString(idIndex));
				UserBean userBean = new UserBean();
				userBean.setNickname(cursor.getString(nameIndex));
				userBean.setHeadimage(cursor.getString(headurlIndex));
				moodBean.setUser(userBean);
				moodBean.setCreatetime(cursor.getString(timeIndex));
				String str[] = new String[1];
				str[0] = cursor.getString(contentpicIndex);
				moodBean.setImg(str);
				moodBean.setContent(cursor.getString(contentIndex));
				moodBeans.add(moodBean);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				// �ر��α�
				cursor.close();
			}

		}

		return moodBeans;
	}

	@Override
	public Object queryData(int pagersize) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		if (db != null && db.isOpen()) {
			// �ر����ݿ����
			db.close();
			db = null;
		}
	}

	@Override
	public long insert(MoodBean moodBean) {
		// TODO Auto-generated method stub
		long id = -1;
		try {
			ContentValues values = new ContentValues();
			values.put(MsgTable.NAME, moodBean.getUser().getNickname());
			values.put(MsgTable.CONTENT, moodBean.getContent());
			try {
				values.put(MsgTable.CONTENT_PIC_URL, moodBean.getImg()[0]);
			} catch (Exception e) {
				// TODO: handle exception
			}
			values.put(MsgTable.HEAD_URL, moodBean.getUser().getHeadimage());
			values.put(MsgTable.MOOD_ID, moodBean.getId());
			values.put(MsgTable.TIME, moodBean.getCreatetime());
			id = db.insert(MsgTable.TABLE_NAME, null, values);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}

	@Override
	public long delete(MoodBean moodBean) {
		// TODO Auto-generated method stub
		int del = -1;
		try {
			String where = MsgTable._ID + " = ?";
			String[] whereArgs = { String.valueOf(moodBean.get_id()) };
			del = db.delete(MsgTable.TABLE_NAME, where, whereArgs);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (del<0) {
			try {
				String where = MsgTable.MOOD_ID + " = ?";
				String[] whereArgs = { String.valueOf(moodBean.getId()) };
				del = db.delete(MsgTable.TABLE_NAME, where, whereArgs);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return del;
	}

}
