package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.MoodBean;
import com.bing.support.debug.AppLog;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class FriendPlaceActivity extends BaseListActivity {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	private View headView;

	private List<MoodBean> list = new ArrayList<>();

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.title_activity_friend_place);
		rightImageView.setImageResource(R.drawable.publish_state);
		headView = getLayoutInflater().inflate(R.layout.user_head, null);

		mBingListView.addHeaderView(headView);

		OnReshData();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friend_place, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void OnReshData() {
		// TODO Auto-generated method stub
		HttpMethod.getOurMood("6", responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

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
			parseJson(response);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	};

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
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		AppLog.i(TAG, "大小:"
				+ list.get(1).getUser().getUsername());

	}

}
