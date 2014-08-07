package com.bing.friendplace.adapter;

import java.util.List;

import com.bing.bean.FriendBean;
import com.bing.friendplace.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class ConstactsAdapter extends BaseAdapter implements SectionIndexer {


	private List<FriendBean> list;
	private LayoutInflater inflater;

	public ConstactsAdapter(Context context, List<FriendBean> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
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
			convertView = inflater.inflate(R.layout.friend_item, parent, false);
			holder.secBox = (CheckBox) convertView.findViewById(R.id.isat);
			holder.userhead = (ImageView) convertView
					.findViewById(R.id.userhead);
			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.username.setText("" + list.get(position).getUsername());
		if (list.get(position).isSec()) {
			holder.secBox.setChecked(true);
		}else {
			holder.secBox.setChecked(false);
		}

		return convertView;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPositionForSection(int section) {
		// TODO Auto-generated method stub
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getUsername();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		return list.get(position).getUsername().charAt(0);
	}

	public class ViewHolder {
		public TextView username;

		public ImageView userhead;

		public CheckBox secBox;
	}

}
