package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.MoodBean;
import com.bing.friendplace.utils.MoodMergeUtils;
import com.bing.support.debug.AppLog;
import com.bing.support.http.JsonUtils;
import com.bing.ui.custmeview.BingListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.Toast;

public abstract class BaseListActivity extends BaseActivity {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	protected SwipeRefreshLayout mSwipeRefreshLayout;

	protected BingListView mBingListView;

	protected List<MoodBean> list = new ArrayList<>();

	protected CircleAdapter circleAdapter;

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
				circleAdapter.notifyDataSetChanged();
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

				first = list.size();
				circleAdapter.notifyDataSetChanged();
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

}
