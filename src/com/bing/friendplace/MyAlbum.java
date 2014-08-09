package com.bing.friendplace;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bing.bean.CircleBean;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.support.image.LoadImageUtils;
import com.bing.ui.custmeview.BingListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.AdapterView.OnItemClickListener;

public class MyAlbum extends BaseListActivity implements OnRefreshListener,
		IXListViewListener {

	private static final String TAG = MyAlbum.class.getSimpleName();

	private View headView;

	private ImageView userhead;

	private ImageView headbg;

	private CircleBean circleBean;

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.myabulm);
		circleAdapter = new CircleAdapter(list, context);

		headView = getLayoutInflater().inflate(R.layout.user_head, null);
		initHeadView(headView);
		mBingListView.addHeaderView(headView);

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

		OnInitData();

	}

	private void initHeadView(View v) {
		userhead = (ImageView) v.findViewById(R.id.user_head);
		headbg = (ImageView) v.findViewById(R.id.head_bg);
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
		HttpMethod.getuserMoods(G.uid, 0, limit, new ResponseHandler(false));
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
				AppLog.i(TAG, "·µ»Ø:" + response);
				if (JsonUtils.isSuccess(response)) {
					try {

						circleBean = JsonUtils.getCircleBean(response
								.getJSONObject("circle"));
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

}
