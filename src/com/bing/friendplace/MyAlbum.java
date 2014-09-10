package com.bing.friendplace;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bing.bean.CircleBean;
import com.bing.friendplace.adapter.AblumAdapter;
import com.bing.friendplace.adapter.AblumAdapter.CircleOnClickListener;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.image.PhotoUtils;
import com.bing.support.time.BingDateUtils;
import com.bing.ui.custmeview.BingListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class MyAlbum extends BaseListActivity implements OnRefreshListener,
		IXListViewListener, CircleOnClickListener {

	private static final String TAG = MyAlbum.class.getSimpleName();

	private View headView;

	private ImageView userhead;

	private ImageView headbg;

	private CircleBean circleBean;

	private Bitmap headbg_bmp;

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.myabulm);
		ablumAdapter = new AblumAdapter(list, context);

		headView = getLayoutInflater().inflate(R.layout.user_head, null);
		initHeadView(headView);
		mBingListView.addHeaderView(headView);

		mBingListView.setAdapter(ablumAdapter);

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

		ablumAdapter.setOnClickLitener(this);

		OnInitData();

	}

	private void initHeadView(View v) {
		userhead = (ImageView) v.findViewById(R.id.user_head);
		headbg = (ImageView) v.findViewById(R.id.head_bg);
		v.findViewById(R.id.today).setVisibility(View.VISIBLE);
		v.findViewById(R.id.today).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(context, PublishActivity.class));
			}
		});
		headbg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PhotoUtils.changeBgDialog(context);
			}
		});
	}

	@Override
	protected void OnInitData() {
		// TODO Auto-generated method stub
		super.OnInitData();
		OnReshData();
		getCirInfo();
	}

	@Override
	protected void OnReshData() {
		// TODO Auto-generated method stub
		HttpMethod.getuserMoods(G.uid, 0, limit, new ResponseHandler(true));
	}

	@Override
	protected void OnLoadMore() {
		// TODO Auto-generated method stub
		HttpMethod
				.getuserMoods(G.uid, first, limit, new ResponseHandler(false));
	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			OnReshData();
		}
	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		if (!isLoading) {
			OnLoadMore();
		}
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

						circleBean = JsonUtils.getCircleBean(response
								.getJSONObject("circle"));

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

						LoadImageUtils
								.loadOriginalImg(userhead, HttpMethod.IMAG_URL
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
	protected void arrangeList() {
		// TODO Auto-generated method stub
		super.arrangeList();
		int length = list.size();
		for (int i = 0; i < length; i++) {
			String createtime = list.get(i).getCreatetime();
			String day = "" + BingDateUtils.getDay(createtime);
			String month = BingDateUtils.getMonth(createtime) + "月";
			if (i == 0) {
				list.get(i).setDay(day);
				list.get(i).setMonth(month);
			} else {
				String lastcreatetime = list.get(i - 1).getCreatetime();
				String lastday = "" + BingDateUtils.getDay(lastcreatetime);
				String lastmonth = BingDateUtils.getMonth(lastcreatetime) + "月";
				if (lastday.equals(day) && lastmonth.equals(month)) {
					list.get(i).setDay("");
					list.get(i).setMonth("");
				} else {
					list.get(i).setDay(day);
					list.get(i).setMonth(month);
				}
			}
		}

	}

	@Override
	public void onPicClick(int position, int picpostion) {
		// TODO Auto-generated method stub
		Intent intent = new Intent();
		intent.putExtra("picpostion", picpostion);
		intent.putExtra("mood", list.get(position));
		intent.setClass(context, PhotoPagerActvity.class);
		startActivity(intent);
	}

	@Override
	public void onDeleteClick(int position) {
		// TODO Auto-generated method stub
		delMoodDialog(position);
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
				PhotoUtils.saveCurrent_ResultBitmap(headbg_bmp,
						circleBean.getUsername());
				new PostBg(G.uid, str).start();
				;
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
				PhotoUtils.saveCurrent_ResultBitmap(headbg_bmp,
						circleBean.getUsername());
				new PostBg(G.uid, path).start();
				AppLog.i(TAG, "路径:" + path);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

}
