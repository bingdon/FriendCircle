package com.bing.friendplace;

import com.bing.bean.MoodBean;
import com.bing.friendplace.CircleAdapter.ViewHolder;
import com.bing.support.time.TimeUtility;
import com.bing.ui.custmeview.BingGridView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MoodInfoActivity extends BaseActivity {

	private ViewHolder holder;

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
		holder = new ViewHolder();
		holder.userhead = (ImageView) findViewById(R.id.userhead);
		holder.bingGridView = (BingGridView) findViewById(R.id.picGridView1);
		holder.content = (TextView) findViewById(R.id.content);
		holder.time = (TextView) findViewById(R.id.time);
		holder.comments = (ListView) findViewById(R.id.comment_list);
		holder.username = (TextView) findViewById(R.id.username);
		initData();
	}

	private void initData() {
		MoodBean moodBean = (MoodBean) getIntent().getSerializableExtra(
				"moodBean");
		holder.username.setText("" + moodBean.getUser().getUsername());
		holder.time.setText(""
				+ TimeUtility.getListTime(moodBean.getCreatetime()));
		holder.content.setText("" + moodBean.getContent());
		holder.gridAdapter = new GridAdapter(moodBean.getImg(), context);
		holder.bingGridView.setAdapter(holder.gridAdapter);
		holder.commentsAdapter = new CommentsAdapter(moodBean.getComment(),
				context);
		holder.comments.setAdapter(holder.commentsAdapter);
	}

}
