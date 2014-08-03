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

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendPlaceActivity extends BaseListActivity implements OnRefreshListener {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	private View headView;

	private List<MoodBean> list = new ArrayList<>();
	
	private String moodid="";

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.title_activity_friend_place);
		rightImageView.setImageResource(R.drawable.publish_state);
		headView = getLayoutInflater().inflate(R.layout.user_head, null);

		mBingListView.addHeaderView(headView);

		circleAdapter=new CircleAdapter(list, context);
		mBingListView.setAdapter(circleAdapter);
		OnReshData();
		
		mSwipeRefreshLayout.setOnRefreshListener(this);
		
		
		mBingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position>0) {
					loopMoodInfo(list.get(position-1));
				}
				
			}
		});
		
		
		rightImageView.setOnClickListener(listener);
		

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
		HttpMethod.getCircleMoods("6", responseHandler);
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
			mSwipeRefreshLayout.setRefreshing(false);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			mSwipeRefreshLayout.setRefreshing(true);
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
				
				circleAdapter.notifyDataSetChanged();
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

//		AppLog.i(TAG, "大小:"
//				+ list.get(1).getUser().getUsername());

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		
	}

	
	
	private void loopMoodInfo(MoodBean moodBean){
		Intent intent=new Intent();
		intent.putExtra("moodid", moodid);
		intent.putExtra("moodBean", moodBean);
		intent.setClass(context, MoodInfoActivity.class);
		startActivity(intent);
	}
	
	private OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_title_img:
				startActivity(new Intent(context, PublishActivity.class));
				break;

			default:
				break;
			}
		}
	};
	
}
