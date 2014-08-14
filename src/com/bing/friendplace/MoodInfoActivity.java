package com.bing.friendplace;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.CommentBean;
import com.bing.bean.MoodBean;
import com.bing.bean.UserBean;
import com.bing.friendplace.CircleAdapter.ViewHolder;
import com.bing.friendplace.adapter.MoodInfoAdapter;
import com.bing.friendplace.adapter.PicAdapter;
import com.bing.friendplace.constant.ConstantS;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.time.TimeUtility;
import com.bing.ui.custmeview.BingGridView;
import com.bing.ui.custmeview.HorizontalListView;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.R.array;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MoodInfoActivity extends BaseActivity implements
		OnItemClickListener {

	private static final String TAG = MoodInfoActivity.class.getSimpleName();

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

	View laundView;

	HorizontalListView gallery;

	MoodInfoAdapter mInfoAdapter;

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
		holder.delete = (TextView) findViewById(R.id.delete);
		laundView = findViewById(R.id.laun_list);

		convertView = findViewById(R.id.convertView);

		popView = getLayoutInflater().inflate(R.layout.pop_view, null);
		comment = (ImageView) popView.findViewById(R.id.comment);
		laun = (ImageView) popView.findViewById(R.id.laun);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(false);
		comment.setFocusable(false);
		laun.setFocusable(false);

		comment.setOnClickListener(listener);
		laun.setOnClickListener(listener);
		holder.commentmenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showMenu(v);
			}
		});

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				delMoodDialog();
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

		holder.comments.setOnItemClickListener(this);

		initData();
	}

	private void initData() {
		moodBean = (MoodBean) getIntent().getSerializableExtra("moodBean");

		if (moodBean != null) {
			LoadImageUtils.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
					+ moodBean.getUser().getHeadimage());
			holder.username.setText("" + moodBean.getUser().getNickname());
			holder.time.setText(""
					+ TimeUtility.getListTime(moodBean.getCreatetime()));
			holder.content.setText("" + moodBean.getContent());
			holder.gridAdapter = new GridAdapter(moodBean.getImg(), context);
			holder.bingGridView.setAdapter(holder.gridAdapter);
			mInfoAdapter = new MoodInfoAdapter(moodBean.getComment(), context);
			holder.comments.setAdapter(mInfoAdapter);

			if (moodBean.isIslaud()) {
				holder.laundTextView.setVisibility(View.VISIBLE);
			} else {
				holder.laundTextView.setVisibility(View.INVISIBLE);
			}

			if (!moodBean.getUid().equals(G.uid)) {
				holder.delete.setVisibility(View.GONE);
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
			AppLog.i(TAG, "心情:" + response);
			if (JsonUtils.isSuccess(response)) {
				parseJson(response);
			}
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

	private void parseJson(JSONObject object) {
		try {
			JSONObject mood = object.getJSONObject("mood");
			MoodBean mBean = JsonUtils.getMoodBean(mood);
			if (mBean != null) {
				moodBean = mBean;
				LoadImageUtils
						.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
								+ moodBean.getUser().getHeadimage());
				holder.username.setText("" + moodBean.getUser().getNickname());
				holder.time.setText(""
						+ TimeUtility.getListTime(moodBean.getCreatetime()));
				holder.content.setText("" + moodBean.getContent());
				holder.gridAdapter = new GridAdapter(moodBean.getImg(), context);
				holder.bingGridView.setAdapter(holder.gridAdapter);
				AppLog.i(TAG, "赞:" + moodBean.getLaudusers());
				gallery = (HorizontalListView) laundView
						.findViewById(R.id.horizontalListView1);
				if (moodBean.getLaudusers() != null) {
					int launlength = moodBean.getLaudusers().length;
					if (launlength > 0) {
						laundView.setVisibility(View.VISIBLE);
					}
					String imgs[] = new String[launlength];
					for (int i = 0; i < launlength; i++) {
						imgs[i] = moodBean.getLaudusers()[i].getHeadimage();
					}

					PicAdapter picAdapter = new PicAdapter(imgs, context);
					gallery.setAdapter(picAdapter);
				}

				mInfoAdapter = new MoodInfoAdapter(moodBean.getComment(),
						context);
				holder.comments.setAdapter(mInfoAdapter);

				if (moodBean.isIslaud()) {
					holder.laundTextView.setVisibility(View.VISIBLE);
				} else {
					holder.laundTextView.setVisibility(View.INVISIBLE);
				}

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

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
		userBean.setNickname("ben");
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

	protected void delMoodDialog() {
		new AlertDialog.Builder(context)
				.setTitle(getString(R.string.notice))
				.setMessage(R.string.delete_msg)
				.setPositiveButton(getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								HttpMethod.delMood(moodBean.getId(),
										new JsonHttpResponseHandler());
								sendDeleteMood(moodBean);
								finish();
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

	private void sendDeleteMood(MoodBean moodBean) {
		Intent intent = new Intent();
		intent.putExtra("moodid", moodBean.getId());
		intent.setAction(ConstantS.ACTION_DEL_MOOD);
		sendBroadcast(intent);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

		if (moodBean.getComment()[position].getUser().getUid().equals(G.uid)) {
			delCommentDialog(position);
		}

	}

	protected void delCommentDialog(final int position) {
		new AlertDialog.Builder(context)
				.setTitle(getString(R.string.notice))
				.setMessage(R.string.delete_comment_notice)
				.setPositiveButton(getString(android.R.string.ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								HttpMethod.delMoodComment(
										moodBean.getComment()[position].getId(),
										new JsonHttpResponseHandler());
								delcomment(position);
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

	private void delcomment(int position) {
		int length=moodBean.getComment().length;
		List<CommentBean> list=new ArrayList<>();
		for (int i = 0; i <length; i++) {
			list.add(moodBean.getComment()[i]);
		}
		list.remove(position);
		moodBean.setComment((CommentBean[]) list.toArray(new CommentBean[list
				.size()]));
		mInfoAdapter = new MoodInfoAdapter(moodBean.getComment(), context);
		holder.comments.setAdapter(mInfoAdapter);
	}

}
