package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import com.bing.friendplace.adapter.PublishGridAdapter;
import com.bing.friendplace.adapter.PublishGridAdapter.PicClickListener;
import com.bing.support.debug.AppLog;
import com.bing.support.image.PhotoUtils;
import com.bing.ui.custmeview.BingGridView;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.GridView;

public class PublishActivity extends BaseActivity implements PicClickListener {

	private static final String TAG=PublishActivity.class.getSimpleName();
	private List<String> list=new ArrayList<>();
	private PublishGridAdapter publishGridAdapter;
	private GridView picBingGridView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_publish);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		list.add("ling");
		titleTextView.setText(R.string.publish);
		picBingGridView=(GridView)findViewById(R.id.pic_grid);
		publishGridAdapter=new PublishGridAdapter(context, list);
		picBingGridView.setAdapter(publishGridAdapter);
		publishGridAdapter.setPicClickListerter(this);
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub

		if (resultCode != RESULT_OK) {
			return;
		}

		if (requestCode == 0) {
			try {

				final String str;
				Uri localUri = data.getData();
				String[] arrayOfString = new String[1];
				arrayOfString[0] = "_data";
				Cursor localCursor = getContentResolver().query(localUri,
						arrayOfString, null, null, null);
				if (localCursor == null)
					return;
				localCursor.moveToFirst();
				str = localCursor.getString(localCursor
						.getColumnIndex(arrayOfString[0]));
				localCursor.close();
				AppLog.i(TAG, "Â·¾¶:"+str);
				list.add(0,str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				AppLog.i(TAG, "Â·¾¶:"+path);
				list.add(0,path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		publishGridAdapter.notifyDataSetChanged();

		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onPicClick(int position) {
		// TODO Auto-generated method stub
		if (position == list.size() - 1 && position < 10) {
			PhotoUtils.secPic(context);
		}
	}
	
}
