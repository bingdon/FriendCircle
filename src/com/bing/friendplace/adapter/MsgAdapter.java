package com.bing.friendplace.adapter;

import java.util.List;

import com.bing.bean.MoodBean;
import com.bing.friendplace.R;
import com.bing.support.debug.AppLog;
import com.bing.support.http.HttpMethod;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.time.TimeUtility;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MsgAdapter extends BaseAdapter {

	private List<MoodBean> list;

	private Context context;

	private LayoutInflater inflater;

	public MsgAdapter(Context context, List<MoodBean> list) {
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
			convertView = inflater.inflate(R.layout.msg_ada, parent, false);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.contentpic = (ImageView) convertView
					.findViewById(R.id.content_pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		AppLog.i(MsgAdapter.class.getSimpleName(), "name"
				+ list.get(position).getUser().getNickname());

		LoadImageUtils.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
				+ list.get(position).getUser().getHeadimage());
		holder.username
				.setText("" + list.get(position).getUser().getNickname());
		holder.content.setText("" + list.get(position).getContent());
		holder.time.setText(""
				+ TimeUtility.getListTime(list.get(position).getCreatetime()));
		try {
			if (!TextUtils.isEmpty(list.get(position).getImg()[0])) {
				LoadImageUtils.loadOriginalImg(holder.contentpic,
						HttpMethod.IMAG_URL + list.get(position).getImg()[0]);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return convertView;
	}

	public class ViewHolder {

		public ImageView userhead;

		public TextView username;

		public TextView content;

		public TextView time;

		public ImageView contentpic;

	}
}
