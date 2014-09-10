package com.bing.friendplace;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Toast;

import com.bing.bean.MoodBean;
import com.bing.friendplace.adapter.MsgAdapter;
import com.bing.friendplace.db.utils.MsgDataMangerUtils;
import com.bing.support.debug.AppLog;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.ui.custmeview.BingListView.IXListViewListener;
import com.loopj.android.http.JsonHttpResponseHandler;

public class MessageActivity extends BaseListActivity implements
		OnRefreshListener, IXListViewListener {

	private static final String TAG = MessageActivity.class.getSimpleName();
	private List<MoodBean> moodBeans = new ArrayList<>();

	private MsgAdapter msgAdapter;

	private MsgDataMangerUtils msgDataMangerUtils;

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		titleTextView.setText(R.string.msg);
		rightTextView.setText(R.string.clearall);

		msgAdapter = new MsgAdapter(context, moodBeans);
		mSwipeRefreshLayout.setOnRefreshListener(this);
		mBingListView.setXListViewListener(this);
		mBingListView.setAdapter(msgAdapter);

		rightTextView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAll();
			}
		});

		registerForContextMenu(mBingListView);
		getLastData();
		OnReshData();
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.message, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.delete:
			deleteMsg(info.position);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	protected void OnReshData() {
		// TODO Auto-generated method stub
		HttpMethod.noticeMood(G.uid, responseHandler);
	}

	@Override
	protected void OnLoadMore() {
		// TODO Auto-generated method stub

	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

		public void onStart() {
			isLoading = true;
			mSwipeRefreshLayout.setRefreshing(true);
		};

		public void onFinish() {
			isLoading = false;
			mSwipeRefreshLayout.setRefreshing(false);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			AppLog.i(TAG, "返回:" + response);
			if (JsonUtils.isSuccess(response)) {
				try {
					JSONArray jsonArray = response.getJSONArray("notices");
					List<MoodBean> mList = new ArrayList<>();
					int length = jsonArray.length();
					for (int i = 0; i < length; i++) {
						MoodBean moodBean = JsonUtils.getMoodBean(jsonArray
								.getJSONObject(i).getJSONObject("mood"));
						mList.add(moodBean);
						AppLog.i(TAG, "用户名:" + moodBean.getUser());
						msgDataMangerUtils.insert(moodBean);
					}

					getLastData();

					// moodBeans.addAll(0, mList);

					msgAdapter.notifyDataSetChanged();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		};

	};

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

	}

	@SuppressWarnings("unchecked")
	private void getLastData() {
		msgDataMangerUtils = new MsgDataMangerUtils(context);
		moodBeans = (List<MoodBean>) msgDataMangerUtils.queryData();

		if (moodBeans == null) {
			moodBeans = new ArrayList<>();
		}
		AppLog.i(TAG, "Sql读取:" + moodBeans.size());
		msgAdapter = new MsgAdapter(context, moodBeans);
		mBingListView.setAdapter(msgAdapter);
		msgAdapter.notifyDataSetChanged();
	}

	private void deleteMsg(int position) {
		long del = msgDataMangerUtils.delete(moodBeans.get(position));
		if (del > 0) {
			moodBeans.remove(position);
			msgAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(context, R.string.deletefailure, Toast.LENGTH_SHORT)
					.show();
		}
	}

	private void deleteAll() {
		long del = msgDataMangerUtils.deleteAll();
		if (del > 0) {
			moodBeans.clear();
			msgAdapter.notifyDataSetChanged();
		} else {
			Toast.makeText(context, R.string.deletefailure, Toast.LENGTH_SHORT)
					.show();
		}
	}

}
