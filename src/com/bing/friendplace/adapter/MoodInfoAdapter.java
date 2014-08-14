package com.bing.friendplace.adapter;

import com.bing.bean.CommentBean;
import com.bing.friendplace.R;
import com.bing.support.http.HttpMethod;
import com.bing.support.image.LoadImageUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MoodInfoAdapter extends BaseAdapter {
	private CommentBean cBeans[];

	private LayoutInflater inflater;

	public MoodInfoAdapter(CommentBean cBeans[], Context context) {
		this.cBeans = cBeans;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return cBeans.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return cBeans[position];
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
			convertView = inflater
					.inflate(R.layout.comment_item, parent, false);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.name.setText(cBeans[position].getUser().getNickname());
		holder.content.setText("" + cBeans[position].getContent());
		LoadImageUtils.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
				+ cBeans[position].getUser().getHeadimage());

		return convertView;
	}

	public class ViewHolder {
		public TextView name;

		public ImageView userhead;

		public TextView content;
	}

}
