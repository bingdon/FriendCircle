package com.bing.friendplace;

import java.util.List;

import com.bing.bean.MoodBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class CircleAdapter extends BaseAdapter {

	private List<MoodBean> list;

	private Context context;

	private LayoutInflater inflater;

	public CircleAdapter(List<MoodBean> list,Context context) {

		this.list=list;
		
		this.inflater=LayoutInflater.from(context);
		
		this.context=context;
		
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
		
		if (convertView==null) {
			
		} else {

		}
		
		return convertView;
	}

}
