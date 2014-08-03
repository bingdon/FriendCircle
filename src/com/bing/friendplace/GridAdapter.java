package com.bing.friendplace;


import com.bing.app.FriendApp;
import com.bing.support.http.HttpMethod;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridAdapter extends BaseAdapter {

	private String imgs[];

	private LayoutInflater inflater;

	public GridAdapter(String imgs[], Context context) {
		this.imgs = imgs;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return imgs[position];
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ImageView imageView;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.pic_grid, parent, false);
			imageView = (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(imageView);
		} else {
			imageView = (ImageView) convertView.getTag();
		}

		FriendApp.imageLoader.displayImage(HttpMethod.IMAG_URL+imgs[position], imageView,
				FriendApp.options);
		return convertView;
	}

}
