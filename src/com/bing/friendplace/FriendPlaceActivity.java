package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.CommentBean;
import com.bing.bean.MoodBean;
import com.bing.bean.UserBean;
import com.bing.friendplace.CircleAdapter.CircleOnClickListener;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class FriendPlaceActivity extends BaseListActivity implements
		OnRefreshListener, CircleOnClickListener {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	private View headView;

	private List<MoodBean> list = new ArrayList<>();

	private String moodid = "";

	private TextView noticeNum;

	private String lastjson = "";

	// ����View
	private View sendView;
	// ���۱༭
	private EditText sendEditText;
	// ���۰�ť
	private Button sendButton;

	private int postion = 0;

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.title_activity_friend_place);
		rightImageView.setImageResource(R.drawable.publish_state);
		headView = getLayoutInflater().inflate(R.layout.user_head, null);

		mBingListView.addHeaderView(headView);

		noticeNum = (TextView) headView.findViewById(R.id.notice_count);
		sendView = findViewById(R.id.send_v);
		initSendView(sendView);
		circleAdapter = new CircleAdapter(list, context);
		mBingListView.setAdapter(circleAdapter);
		OnReshData();

		mSwipeRefreshLayout.setOnRefreshListener(this);

		mBingListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position > 0) {
					loopMoodInfo(list.get(position - 1));
				}

			}
		});

		rightImageView.setOnClickListener(listener);
		findViewById(R.id.notice_fra).setOnClickListener(listener);
		circleAdapter.setOnClickLitener(this);

	}

	private void initSendView(View v) {
		sendButton = (Button) v.findViewById(R.id.send_msg_btn);
		sendEditText = (EditText) v.findViewById(R.id.send_msg_editText);
		sendButton.setOnClickListener(listener);
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
		HttpMethod.getCircleMoods(G.uid, 0, 10, responseHandler);
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
			AppLog.i(TAG, "���ؽ��:" + response);
			if (lastjson.equals(response.toString())) {
				Toast.makeText(context, R.string.nonewmsg, Toast.LENGTH_LONG)
						.show();
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
				lastjson = response.toString();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			OnReshData();
		}

	}

	private void loopMoodInfo(MoodBean moodBean) {
		Intent intent = new Intent();
		intent.putExtra("moodid", moodid);
		intent.putExtra("moodBean", moodBean);
		intent.setClass(context, MoodInfoActivity.class);
		startActivity(intent);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_title_img:
				startActivity(new Intent(context, PublishActivity.class));
				break;

			case R.id.notice_fra:
				noticeNum.setVisibility(View.GONE);
				startActivity(new Intent(context, MessageActivity.class));
				break;

			case R.id.send_msg_btn:

				break;

			default:
				break;
			}
		}
	};

	@Override
	public void onPicClick(int position) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onCommentClick(int position) {
		// TODO Auto-generated method stub
		moodid = list.get(position).getId();
		this.postion = position;
		comment2Mood();
	}

	@Override
	public void onLaunClick(int position) {
		// TODO Auto-generated method stub

	}

	private void comment2Mood() {
		sendEditText.setError(null);
		String content = sendEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			sendEditText.setError(getString(R.string.nullcontentnotice));
			return;
		}
		HttpMethod.postMoodComment(moodid, content, G.uid, new SendCommend(
				content));
	}

	public class SendCommend extends JsonHttpResponseHandler {
		private CommentBean commentBean;

		public SendCommend(String content) {
			commentBean=new CommentBean();
			commentBean.setContent(content);
			UserBean userBean=new UserBean();
			userBean.setUid(G.uid);
			userBean.setUsername("ben");
			commentBean.setUser(userBean);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			CommentBean [] commentBeans=list.get(postion).getComment();
			CommentBean[] commentBeans2=new CommentBean[commentBeans.length];
			System.arraycopy(commentBeans, 0, commentBeans2, 0, commentBeans.length);
			commentBeans2[commentBeans2.length-1]=commentBean;
			list.get(postion).setComment(commentBeans2);
			circleAdapter.notifyDataSetChanged();
		}

	}

}
