package com.bing.friendplace;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bing.bean.CommentBean;
import com.bing.bean.MoodBean;
import com.bing.bean.UserBean;
import com.bing.friendplace.CircleAdapter.ViewHolder;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.time.TimeUtility;
import com.bing.ui.custmeview.BingGridView;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MoodInfoActivity extends BaseActivity {

	private ViewHolder holder;

	private PopupWindow popupWindow;

	private View popView;

	private ImageView comment;

	private ImageView laun;

	// 评论View
	private View sendView;
	// 评论编辑
	private EditText sendEditText;
	// 评论按钮
	private Button sendButton;

	int[] location = new int[2];

	MoodBean moodBean;

	View convertView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mood_info);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();

		sendView = findViewById(R.id.send_v);
		initSendView(sendView);

		holder = new ViewHolder();
		holder.userhead = (ImageView) findViewById(R.id.userhead);
		holder.bingGridView = (BingGridView) findViewById(R.id.picGridView1);
		holder.content = (TextView) findViewById(R.id.content);
		holder.time = (TextView) findViewById(R.id.time);
		holder.comments = (ListView) findViewById(R.id.comment_list);
		holder.username = (TextView) findViewById(R.id.username);
		holder.commentmenu = (ImageView) findViewById(R.id.comment);
		holder.laundTextView = (TextView) findViewById(R.id.laud_txt);

		convertView=findViewById(R.id.convertView);
		
		popView = getLayoutInflater().inflate(R.layout.pop_view, null);
		comment = (ImageView) popView.findViewById(R.id.comment);
		laun = (ImageView) popView.findViewById(R.id.laun);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(false);
		comment.setFocusable(false);
		laun.setFocusable(false);

		// holder.comments.setOnClickListener(listener);
		comment.setOnClickListener(listener);
		laun.setOnClickListener(listener);
		holder.commentmenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(v);
			}
		});
		
		
		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
					return true;
				} else {
					return false;
				}
			}
		});

		initData();
	}

	private void initData() {
		moodBean = (MoodBean) getIntent().getSerializableExtra("moodBean");

		if (moodBean != null) {
			LoadImageUtils.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
					+ moodBean.getUser().getHeadimage());
			holder.username.setText("" + moodBean.getUser().getUsername());
			holder.time.setText(""
					+ TimeUtility.getListTime(moodBean.getCreatetime()));
			holder.content.setText("" + moodBean.getContent());
			holder.gridAdapter = new GridAdapter(moodBean.getImg(), context);
			holder.bingGridView.setAdapter(holder.gridAdapter);
			holder.commentsAdapter = new CommentsAdapter(moodBean.getComment(),
					context);
			holder.comments.setAdapter(holder.commentsAdapter);

			if (moodBean.isIslaud()) {
				holder.laundTextView.setVisibility(View.VISIBLE);
			} else {
				holder.laundTextView.setVisibility(View.INVISIBLE);
			}

		}

		getMoodInfo(moodBean.getId());

	}

	private void initSendView(View v) {
		sendButton = (Button) v.findViewById(R.id.send_msg_btn);
		sendEditText = (EditText) v.findViewById(R.id.send_msg_editText);
		sendButton.setOnClickListener(listener);
	}

	private void getMoodInfo(String moodid) {
		HttpMethod.getMoodInfo(moodid, G.uid, responseHandler);
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

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.comment:
				popupWindow.dismiss();
				sendView.setVisibility(View.VISIBLE);
				break;

			case R.id.laun:
				laun();
				break;

			case R.id.send_msg_btn:
				comment2Mood();
				break;

			default:
				break;
			}

		}
	};

	private void laun() {
		popupWindow.dismiss();
		HttpMethod.postMoodLand(moodBean.getId(), G.uid,
				new JsonHttpResponseHandler());
		moodBean.setIslaud(true);
		holder.laundTextView.setVisibility(View.VISIBLE);
	}

	private void showMenu(View v) {

		int width = popView.getWidth();
		if (width == 0) {
			width = 254;
		}

		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
			v.getLocationOnScreen(location);
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
					- width, location[1]);
		} else {
			v.getLocationOnScreen(location);
			popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0]
					- width, location[1]);
		}
	}

	private void comment2Mood() {
		sendEditText.setError(null);
		String content = sendEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			sendEditText.setError(getString(R.string.nullcontentnotice));
			sendEditText.requestFocus();
			return;
		}
		sendEditText.setText("");
		HttpMethod.postMoodComment(moodBean.getId(), content, G.uid,
				new JsonHttpResponseHandler());
		CommentBean commentBean;
		commentBean = new CommentBean();
		commentBean.setContent(content);
		UserBean userBean = new UserBean();
		userBean.setUid(G.uid);
		userBean.setUsername("ben");
		commentBean.setUser(userBean);
		CommentBean[] commentBeans = moodBean.getComment();
		CommentBean[] commentBeans2 = new CommentBean[commentBeans.length + 1];
		System.arraycopy(commentBeans, 0, commentBeans2, 0, commentBeans.length);
		commentBeans2[commentBeans2.length - 1] = commentBean;
		sendView.setVisibility(View.GONE);
		moodBean.setComment(commentBeans2);
		holder.commentsAdapter = new CommentsAdapter(moodBean.getComment(),
				context);
		holder.comments.setAdapter(holder.commentsAdapter);
		holder.commentsAdapter.notifyDataSetChanged();

	}

}
