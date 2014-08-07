package com.bing.friendplace;

import android.os.Bundle;
import android.widget.ListView;

public class MessageActivity extends BaseActivity {

	private ListView listView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_msg);
		OnInitView();
	}
	
	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		listView=(ListView)findViewById(R.id.listView1);
		titleTextView.setText(R.string.msg);
		rightTextView.setText(R.string.clearall);
	}
	
}
