package com.bing.friendplace;

import com.bing.bean.CommentBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentsAdapter extends BaseAdapter {

	private CommentBean cBeans[];

	private LayoutInflater inflater;

	public CommentsAdapter(CommentBean cBeans[], Context context) {
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
		if (convertView==null) {
			holder=new ViewHolder();
			convertView=inflater.inflate(R.layout.comment_ada, parent, false);
			holder.name=(TextView)convertView.findViewById(R.id.name);
			holder.content=(TextView)convertView.findViewById(R.id.content);
			convertView.setTag(holder);
			
		} else {
			holder=(ViewHolder)convertView.getTag();
		}
		
		holder.name.setText(cBeans[position].getUser().getUsername()+":");
		holder.content.setText(""+cBeans[position].getContent());
		
		return convertView;
	}

	public class ViewHolder {
		public TextView name;

		public TextView content;
	}

}
