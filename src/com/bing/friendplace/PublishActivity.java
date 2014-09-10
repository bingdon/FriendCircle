package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.bing.bean.FriendBean;
import com.bing.bean.MoodBean;
import com.bing.friendplace.adapter.PublishGridAdapter;
import com.bing.friendplace.adapter.PublishGridAdapter.PicClickListener;
import com.bing.friendplace.constant.ConstantS;
import com.bing.friendplace.utils.NoticeUtils;
import com.bing.friendplace.utils.VisibleParseUtils;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.PhotoUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View.OnClickListener;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class PublishActivity extends BaseActivity implements PicClickListener {

	private static final String TAG = PublishActivity.class.getSimpleName();
	private List<String> list = new ArrayList<>();
	private PublishGridAdapter publishGridAdapter;
	private GridView picBingGridView;
	private TextView addressTextView;
	private TextView visible_txt;
	private EditText editText;
	private TextView at_one;

	private String content = "";
	private String uids = "";

	private String address = "";

	private LocationClient mLocationClient;
	private MyLocationListener mMyLocationListener;
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor = "gcj02";
	private CheckBox posBox;
	private boolean isLoaction = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		list.add("ling");
		titleTextView.setText(R.string.publish);
		rightTextView.setText(R.string.send);
		findViewById(R.id.position_line).setOnClickListener(listener);
		posBox = (CheckBox) findViewById(R.id.position_check);
		editText = (EditText) findViewById(R.id.content);
		picBingGridView = (GridView) findViewById(R.id.pic_grid);
		at_one = (TextView) findViewById(R.id.at_one);
		addressTextView = (TextView) findViewById(R.id.position);
		visible_txt = (TextView) findViewById(R.id.visible_txt);
		publishGridAdapter = new PublishGridAdapter(context, list);
		picBingGridView.setAdapter(publishGridAdapter);
		publishGridAdapter.setPicClickListerter(this);
		rightTextView.setOnClickListener(listener);
		findViewById(R.id.visible_line).setOnClickListener(listener);
		findViewById(R.id.at_line).setOnClickListener(listener);
		posBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				isLoaction = isChecked;
			}
		});
		InitLocation();
	}

	@Override
	protected void OnInitData() {
		// TODO Auto-generated method stub
		super.OnInitData();
		visible_txt.setText(""
				+ VisibleParseUtils.getVisible(VisibleRangeActivity.type));

		Intent intent = getIntent();
		handelIntent(intent);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		OnInitData();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		mLocationClient.stop();
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
				AppLog.i(TAG, "路径:" + str);
				list.add(0, str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				AppLog.i(TAG, "路径:" + path);
				list.add(0, path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 2) {
			try {
				Bundle extras = data.getExtras();
				@SuppressWarnings("unchecked")
				List<FriendBean> list = (List<FriendBean>) extras
						.getSerializable("atpeople");
				AppLog.i(TAG, "大小:" + list.size());
				showPeople(list);
			} catch (Exception e) {
				// TODO: handle exception
				AppLog.w(TAG, "" + e.getLocalizedMessage());
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

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_title_txt:
				publishMood();
				break;

			case R.id.visible_line:
				loopVisibleActivity();
				break;

			case R.id.at_line:
				loopSecPeopleActivity();
				break;

			case R.id.position_line:

				setPosition();
				break;
			default:
				break;
			}
		}
	};

	private void setPosition() {
		if (isLoaction) {
			posBox.setChecked(false);
		} else {
			posBox.setChecked(true);
		}
	}

	private void publishMood() {
		content = editText.getText().toString();
		editText.setError(null);
		if (TextUtils.isEmpty(content)) {
			editText.setError(getString(R.string.nullcontentnotice));
			editText.requestFocus();
			return;
		}
		HttpMethod.PostMood2Circle(G.uid, content, ""
				+ VisibleRangeActivity.type, uids, address, responseHandler);
	}

	private void loopVisibleActivity() {
		startActivity(new Intent(context, VisibleRangeActivity.class));
	}

	private void loopSecPeopleActivity() {
		startActivityForResult(new Intent(context, ContactsActivity.class), 2);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
		public void onStart() {
			super.onStart();
			NoticeUtils.notice(context, getString(R.string.publish_mmod),
					ConstantS.PUBLISH_MOOD);
			finish();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				String responseString, Throwable throwable) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, responseString, throwable);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
			NoticeUtils.showFailePublish(context);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			AppLog.i(TAG, "返回:" + response);
			parseJson(response);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		};

	};

	private void parseJson(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONObject jsonObject = response.getJSONObject("mood");
				MoodBean moodBean = JsonUtils.getMoodBean(jsonObject);
				postPic(moodBean.getId());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
				NoticeUtils.showFailePublish(context);
			}
		} else {
			NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
			NoticeUtils.showFailePublish(context);
		}
	}

	/**
	 * 发送照片
	 * 
	 * @param moodid
	 */
	private void postPic(final String moodid) {
		if (list.size() < 2) {
			NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
			NoticeUtils.showSuccessfulNotification(context);
			return;
		}

		NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
		NoticeUtils.showProgressPublish(context, 0, list.size() - 1,
				ConstantS.PUBLISH_MOOD);
		try {
			HttpMethod.postMoodImg(moodid, PhotoUtils.bitmapNCutToString(list
					.get(0)), new UpPicHandler(0, moodid));
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		// new Thread(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		//
		//
		//
		// }
		// }).start();

	}

	private class UpPicHandler extends JsonHttpResponseHandler {

		int position;
		String moodid;

		public UpPicHandler(int position, String moodid) {
			this.position = position;
			this.moodid = moodid;
		}

		@Override
		public void onProgress(int bytesWritten, int totalSize) {
			// TODO Auto-generated method stub
			super.onProgress(bytesWritten, totalSize);
			NoticeUtils.showProgressPublish(context, totalSize * bytesWritten
					+ position, list.size() - 1, ConstantS.PUBLISH_MOOD);
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
			NoticeUtils.showFailePublish(context);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			AppLog.i(TAG, "返回图片:" + response);
			NoticeUtils.showProgressPublish(context, position + 1,
					list.size() - 1, ConstantS.PUBLISH_MOOD);
			if (position < list.size() - 2) {
				position++;
				try {
					HttpMethod.postMoodImg(moodid,
							PhotoUtils.bitmapNCutToString(list.get(position)),
							new UpPicHandler(position, moodid));
				} catch (Exception e) {
					// TODO: handle exception
				}
				
			} else {
				NoticeUtils.removeNotice(ConstantS.PUBLISH_MOOD, context);
				NoticeUtils.showSuccessfulNotification(context);
			}
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	}

	private void showPeople(List<FriendBean> list) {
		String name = "";
		uids = "";
		for (int i = 0; i < list.size(); i++) {
			FriendBean friendBean = list.get(i);
			if (friendBean.isSec()) {
				name = name + " " + friendBean.getNickname();
				uids = uids + "," + friendBean.getUid();
			}

		}

		at_one.setText(name);

	}

	private void handelIntent(Intent intent) {
		String action = intent.getAction();
		String type = intent.getType();
		if (Intent.ACTION_SEND.equals(action) && type != null) {
			if ("text/plain".equals(type)) {
				handleSendText(intent); // Handle text being sent
			} else if (type.startsWith("image/")) {
				handleSendImage(intent); // Handle single image being sent
			}
		} else if (Intent.ACTION_SEND_MULTIPLE.equals(action) && type != null) {
			if (type.startsWith("image/")) {
				handleSendMultipleImages(intent); // Handle multiple images
													// being sent
			}
		} else {
			// Handle other intents, such as being started from the home screen
		}
	}

	private void handleSendMultipleImages(Intent intent) {
		// TODO Auto-generated method stub

		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (!TextUtils.isEmpty(sharedText)) {
			editText.setText(sharedText);
		}

		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			String path = PhotoUtils.getPicPathFromUri(imageUri, this);
			AppLog.i(TAG, "路径:" + path);
			list.add(0, path);
			publishGridAdapter.notifyDataSetChanged();
		}

	}

	private void handleSendImage(Intent intent) {
		// TODO Auto-generated method stub
		Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
		if (imageUri != null) {
			// Update UI to reflect image being shared
			String path = PhotoUtils.getPicPathFromUri(imageUri, this);
			AppLog.i(TAG, "路径:" + path);
			list.add(0, path);
			publishGridAdapter.notifyDataSetChanged();
		}
	}

	private void handleSendText(Intent intent) {
		// TODO Auto-generated method stub
		String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
		if (!TextUtils.isEmpty(sharedText)) {
			editText.setText(sharedText);
		}
	}

	/**
	 * 初始化监听
	 */
	private void InitLocation() {
		mLocationClient = new LocationClient(context);
		mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);// 设置定位模式
		option.setCoorType(tempcoor);// 返回的定位结果是百度经纬度，默认值gcj02
		option.setIsNeedAddress(true);
		int span = 1000;
		option.setScanSpan(span);// 设置发起定位请求的间隔时间为5000ms
		mLocationClient.setLocOption(option);
		mLocationClient.start();
	}

	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation arg0) {
			// TODO Auto-generated method stub
			AppLog.i(TAG, "地址:" + arg0.getAddrStr()+"是否定位:"+isLoaction);
			if (isLoaction) {
				address = arg0.getAddrStr();
				if (!TextUtils.isEmpty(address)) {
					addressTextView.setText("" + address);
				}else {
					addressTextView.setText(getString(R.string.loacting));
				}
			} else {
				address = "";
				addressTextView.setText("" + address);
			}

		}

	}

}
