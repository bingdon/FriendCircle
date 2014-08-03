package com.bing.friendplace;

import java.util.List;

import com.bing.bean.MoodBean;
import com.bing.support.time.TimeUtility;
import com.bing.ui.custmeview.BingGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class CircleAdapter extends BaseAdapter {

	private List<MoodBean> list;

	private Context context;

	private LayoutInflater inflater;

	public CircleAdapter(List<MoodBean> list, Context context) {

		this.list = list;

		this.inflater = LayoutInflater.from(context);

		this.context = context;

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.circle_ada, parent, false);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.bingGridView = (BingGridView) convertView
					.findViewById(R.id.picGridView1);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.comments = (ListView) convertView
					.findViewById(R.id.comment_list);
			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.bingGridView.setFocusable(false);
		holder.bingGridView.setFocusableInTouchMode(false);
		holder.comments.setFocusable(false);
		holder.comments.setFocusableInTouchMode(false);
		
		holder.username
				.setText("" + list.get(position).getUser().getUsername());
		holder.time.setText(""
				+ TimeUtility.getListTime(list.get(position).getCreatetime()));
		holder.content.setText("" + list.get(position).getContent());
		holder.gridAdapter = new GridAdapter(list.get(position).getImg(),
				context);
		holder.bingGridView.setAdapter(holder.gridAdapter);
		holder.commentsAdapter = new CommentsAdapter(list.get(position)
				.getComment(), context);
		holder.comments.setAdapter(holder.commentsAdapter);
		return convertView;
	}

	public static class ViewHolder {
		public ImageView userhead;
		public TextView username;
		public TextView content;
		public TextView time;
		public BingGridView bingGridView;
		public ListView comments;
		public GridAdapter gridAdapter;
		public CommentsAdapter commentsAdapter;
	}
}
