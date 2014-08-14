package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.MoodBean;
import com.bing.friendplace.adapter.AblumAdapter;
import com.bing.friendplace.constant.ConstantS;
import com.bing.friendplace.utils.MoodMergeUtils;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.PhotoUtils;
import com.bing.ui.custmeview.BingListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

public abstract class BaseListActivity extends BaseActivity {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	protected SwipeRefreshLayout mSwipeRefreshLayout;

	protected BingListView mBingListView;

	protected List<MoodBean> list = new ArrayList<>();

	protected CircleAdapter circleAdapter;

	protected AblumAdapter ablumAdapter;

	protected boolean isLoading = false;

	protected static final int limit = 10;

	protected int first = 0;

	private String lastjson = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_base_list);
		OnInitView();
		initFilter();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(delReceiver);
	}
	

	protected abstract void OnReshData();

	protected abstract void OnLoadMore();

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.list_swipe);
		mBingListView = (BingListView) findViewById(R.id.m_listview);
	}

	public class ResponseHandler extends JsonHttpResponseHandler {

		boolean isresh;

		public ResponseHandler(boolean isresh) {
			this.isresh = isresh;
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			AppLog.i(TAG, "返回结果:" + response);
			if (isresh) {
				if (lastjson.equals(response.toString())) {
					Toast.makeText(context, R.string.nonewmsg,
							Toast.LENGTH_LONG).show();
				} else {
					parseNewJson(response);
				}
			} else {
				parseJson(response);
			}

		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
			isLoading = false;
			mSwipeRefreshLayout.setRefreshing(false);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			isLoading = true;
			mSwipeRefreshLayout.setRefreshing(true);
		}

	}

	private void parseJson(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray jsonArray = response.getJSONArray("moods");
				int length = jsonArray.length();
				for (int i = 0; i < length; i++) {
					MoodBean moodBean = JsonUtils.getMoodBean(jsonArray
							.getJSONObject(i));

					list.add(moodBean);
				}
				first = list.size();
				arrangeList();
				if (circleAdapter != null) {
					circleAdapter.notifyDataSetChanged();
				} else {
					ablumAdapter.notifyDataSetChanged();
				}

				lastjson = response.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void parseNewJson(JSONObject response) {
		if (JsonUtils.isSuccess(response)) {
			try {
				JSONArray jsonArray = response.getJSONArray("moods");
				int length = jsonArray.length();
				List<MoodBean> moodBeans = new ArrayList<>();
				for (int i = 0; i < length; i++) {
					MoodBean moodBean = JsonUtils.getMoodBean(jsonArray
							.getJSONObject(i));

					moodBeans.add(moodBean);
				}

				MoodMergeUtils.mergeMood(list, moodBeans);
				arrangeList();
				first = list.size();
				if (circleAdapter != null) {
					circleAdapter.notifyDataSetChanged();
				} else {
					ablumAdapter.notifyDataSetChanged();
				}
				lastjson = response.toString();
				AppLog.i(TAG, "返回数量:" + first);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	protected void loopMoodInfo(MoodBean moodBean) {
		Intent intent = new Intent();
		intent.putExtra("moodBean", moodBean);
		intent.setClass(context, MoodInfoActivity.class);
		startActivity(intent);
	}

	protected void arrangeList() {

	}

	protected JsonHttpResponseHandler postbgseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			AppLog.i(TAG, "返回:" + response);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	};

	protected void delMoodDialog(final int position) {
		new AlertDialog.Builder(context)
				.setTitle(getString(R.string.notice))
				.setMessage(R.string.delete_msg)
				.setPositiveButton(getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								HttpMethod.delMood(list.get(position).getId(),
										new JsonHttpResponseHandler());
								list.remove(position);
								if (circleAdapter != null) {
									circleAdapter.notifyDataSetChanged();
								} else {
									ablumAdapter.notifyDataSetChanged();
								}

							}
						})
				.setNegativeButton(getString(android.R.string.cancel),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						}).show();
	}

	
	
	public class PostBg extends Thread {
		String uid;
		String path;

		public PostBg(String uid, String path) {
			this.uid = uid;
			this.path = path;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			Message msg=new Message();
			msg.what=0;
			msg.obj=PhotoUtils.bitmapNCutToString(path);
			postBgHandler.sendMessage(msg);
		}
	}
	
	
	
	private Handler postBgHandler=new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				String str=msg.obj.toString();
				HttpMethod.updateCircle(G.uid, str,
						postbgseHandler);
				
				break;

			default:
				break;
			}
			return false;
		}
	});
	
	
	
	private void initFilter(){
		IntentFilter filter=new IntentFilter();
		filter.addAction(ConstantS.ACTION_DEL_MOOD);
		registerReceiver(delReceiver, filter);
	}
	
	
	private BroadcastReceiver delReceiver=new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			if (action.equals(ConstantS.ACTION_DEL_MOOD)) {
				String id=intent.getStringExtra("moodid");
				delMoodbyid(id);
			}
		}
	};
	
	
	private void delMoodbyid(String id){
		if (list==null||list.size()==0) {
			return;
		}
		
		int length=list.size();
		for (int i = 0; i < length; i++) {
			if (id.equals(list.get(i).getId())) {
				
				list.remove(i);
				if (circleAdapter != null) {
					circleAdapter.notifyDataSetChanged();
				} else if(ablumAdapter != null){
					ablumAdapter.notifyDataSetChanged();
				}
				return;
			}
		}
		
		
	}
	
	
}
