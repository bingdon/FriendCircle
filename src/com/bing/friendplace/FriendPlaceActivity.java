package com.bing.friendplace;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bing.bean.CircleBean;
import com.bing.bean.CommentBean;
import com.bing.bean.UserBean;
import com.bing.friendplace.CircleAdapter.CircleOnClickListener;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.file.FileUtility;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.image.PhotoUtils;
import com.bing.ui.custmeview.BingListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FriendPlaceActivity extends BaseListActivity implements
		OnRefreshListener, CircleOnClickListener, IXListViewListener {

	private static final String TAG = FriendPlaceActivity.class.getSimpleName();

	private View headView;

	private ImageView userhead;

	private ImageView headbg;

	private String moodid = "";

	private TextView noticeNum;

	private TextView noticetitle;

	// 评论View
	private View sendView;
	// 评论编辑
	private EditText sendEditText;
	// 评论按钮
	private Button sendButton;

	private TextView today_scan_count;

	private TextView total_scan_count;

	private CircleBean circleBean;

	private int postion = 0;

	private int noticecount = 0;

	private Bitmap headbg_bmp;

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.title_activity_friend_place);
		rightImageView.setImageResource(R.drawable.publish_state);
		headView = getLayoutInflater().inflate(R.layout.user_head, null);
		initHeadView(headView);
		mBingListView.addHeaderView(headView);

		noticeNum = (TextView) headView.findViewById(R.id.notice_count);
		noticetitle = (TextView) headView.findViewById(R.id.title);
		sendView = findViewById(R.id.send_v);
		initSendView(sendView);
		circleAdapter = new CircleAdapter(list, context);
		mBingListView.setAdapter(circleAdapter);

		mSwipeRefreshLayout.setOnRefreshListener(this);

		mBingListView.setXListViewListener(this);

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
		findViewById(R.id.notice_fra).setVisibility(View.VISIBLE);
		findViewById(R.id.cir_visit).setVisibility(View.VISIBLE);
		userhead.setOnClickListener(listener);
		circleAdapter.setOnClickLitener(this);
		initData();
	}

	private void initData() {
		OnReshData();
		getNoticeCount();
		getCirInfo();
	}

	private void initHeadView(View v) {
		today_scan_count = (TextView) v.findViewById(R.id.today_scan_count);
		total_scan_count = (TextView) v.findViewById(R.id.total_scan_count);
		userhead = (ImageView) v.findViewById(R.id.user_head);
		headbg = (ImageView) v.findViewById(R.id.head_bg);

		headbg.setOnClickListener(listener);

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
		HttpMethod.getCircleMoods(G.uid, 0, limit, new ResponseHandler(true));
	}

	@Override
	protected void OnLoadMore() {
		// TODO Auto-generated method stub
		HttpMethod.getCircleMoods(G.uid, first, limit, new ResponseHandler(
				false));
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			OnReshData();
		}

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
				noticecount = 0;
				noticeNum.setVisibility(View.GONE);
				noticetitle.setVisibility(View.VISIBLE);
				startActivity(new Intent(context, MessageActivity.class));
				break;

			case R.id.send_msg_btn:
				comment2Mood();
				break;

			case R.id.user_head:
				loopMyAblum();
				break;

			case R.id.head_bg:
				PhotoUtils.changeBgDialog(context);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onPicClick(int position, int picpostion) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("picpostion", picpostion);
		// intent.putExtra("imgs", list.get(position).getImg());
		intent.putExtra("mood", list.get(position));
		intent.setClass(context, PhotoPagerActvity.class);
		startActivity(intent);
	}

	@Override
	public void onCommentClick(int position) {
		// TODO Auto-generated method stub
		moodid = list.get(position).getId();
		this.postion = position;
		sendView.setVisibility(View.VISIBLE);
	}

	@Override
	public void onLaunClick(int position) {
		// TODO Auto-generated method stub
		HttpMethod.postMoodLand(list.get(position).getId(), G.uid,
				new JsonHttpResponseHandler());
		list.get(position).setIslaud(true);
		circleAdapter.notifyDataSetChanged();
	}

	private void comment2Mood() {
		sendEditText.setError(null);
		String content = sendEditText.getText().toString();
		if (TextUtils.isEmpty(content)) {
			sendEditText.setError(getString(R.string.nullcontentnotice));
			sendEditText.requestFocus();
			return;
		}
		HttpMethod.postMoodComment(moodid, content, G.uid, new SendCommend(
				content));
	}

	public class SendCommend extends JsonHttpResponseHandler {
		private CommentBean commentBean;

		public SendCommend(String content) {
			commentBean = new CommentBean();
			commentBean.setContent(content);
			UserBean userBean = new UserBean();
			userBean.setUid(G.uid);
			userBean.setUsername("ben");
			commentBean.setUser(userBean);
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			sendEditText.setText("");
			CommentBean[] commentBeans = list.get(postion).getComment();
			CommentBean[] commentBeans2 = new CommentBean[commentBeans.length + 1];
			System.arraycopy(commentBeans, 0, commentBeans2, 0,
					commentBeans.length);
			commentBeans2[commentBeans2.length - 1] = commentBean;
			sendView.setVisibility(View.GONE);
			list.get(postion).setComment(commentBeans2);
			circleAdapter.notifyDataSetChanged();
		}

	}

	private void getNoticeCount() {

		JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				AppLog.i(TAG, "返回:" + response);
				if (JsonUtils.isSuccess(response)) {
					try {
						noticecount = response.getInt("count");
						if (noticecount > 0) {
							noticeNum.setText("" + noticecount);
							noticeNum.setVisibility(View.VISIBLE);
							noticetitle.setVisibility(View.GONE);
						}
					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}

		};

		HttpMethod.noticeMoodCount(G.uid, responseHandler);
	}

	private void getCirInfo() {
		JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				AppLog.i(TAG, "返回:" + response);
				if (JsonUtils.isSuccess(response)) {
					try {

						String visitDay = response.getString("visitDay");
						String visitAll = response.getString("visitAll");
						circleBean = JsonUtils.getCircleBean(response
								.getJSONObject("circle"));
						today_scan_count.setText("" + visitDay);
						total_scan_count.setText("" + visitAll);
						headbg_bmp = PhotoUtils.getListHeadBg(circleBean
								.getUsername());
						if (headbg_bmp != null) {
							headbg.setImageBitmap(headbg_bmp);
						} else {

							LoadImageUtils.loadOriginalImg(
									headbg,
									HttpMethod.IMAG_URL
											+ circleBean.getHeadimage());
							LoadImageUtils.getOriginalImg(HttpMethod.IMAG_URL
									+ circleBean.getHeadimage(),
									circleBean.getUsername());
						}
						
						LoadImageUtils.loadOriginalImg(
								userhead,
								HttpMethod.IMAG_URL
										+ circleBean.getHeadimage());

					} catch (Exception e) {
						// TODO: handle exception
					}

				}

			}

		};
		HttpMethod.getFriendList(G.uid, responseHandler);
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			OnLoadMore();
		}
	}

	private void loopMyAblum() {
		startActivity(new Intent(context, MyAlbum.class));
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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
				headbg_bmp = PhotoUtils.getScaledBitmap(str, 600);
				headbg.setImageBitmap(headbg_bmp);
				saveCurrent_ResultBitmap(headbg_bmp);
				new PostBg(G.uid, str).start();;
				AppLog.i(TAG, "路径:" + str);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				String path = PhotoUtils.getPicPathFromUri(
						PhotoUtils.imageFileUri, this);
				headbg_bmp = PhotoUtils.getScaledBitmap(path, 600);
				headbg.setImageBitmap(headbg_bmp);
				saveCurrent_ResultBitmap(headbg_bmp);
				new PostBg(G.uid, path).start();
				AppLog.i(TAG, "路径:" + path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	/**
	 * 保存此次背景
	 * 
	 * @param result
	 */
	private void saveCurrent_ResultBitmap(Bitmap bitmap) {
		AppLog.i(TAG, "开始保存");
		File file = new File(FileUtility.FRIEND_PATH_IMG,
				circleBean.getUsername() + "bg" + ".jpg");
		BufferedOutputStream bos;
		try {
			bos = new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(CompressFormat.JPEG, 100, bos);
			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AppLog.i(TAG, "保存成功");
	}

	
	
	
	
	private Handler postBgHandler=new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
			case 0:
				String str=msg.obj.toString();
				HttpMethod.updateCircle("6", str,
						postbgseHandler);
				
				break;

			default:
				break;
			}
			return false;
		}
	});

	@Override
	public void onDeleteClick(int position) {
		// TODO Auto-generated method stub
		delMoodDialog(position);
	}

	
	
	
}
