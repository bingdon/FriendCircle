package com.bing.friendplace;

import com.bing.ui.custmeview.BingListView;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Window;

public abstract class BaseListActivity extends BaseActivity {

	protected SwipeRefreshLayout mSwipeRefreshLayout;
	
	protected BingListView mBingListView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_base_list);
		OnInitView();
	}
	
	
	protected abstract void OnReshData();
	
	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		mSwipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.list_swipe);
		mBingListView=(BingListView)findViewById(R.id.m_listview);
	}
	
}
