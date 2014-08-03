package com.bing.friendplace.adapter;

import java.util.List;

import com.bing.friendplace.R;
import com.bing.support.debug.AppLog;
import com.bing.support.image.PhotoUtils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class PublishGridAdapter extends BaseAdapter {

	private List<String> list;
	private LayoutInflater inflater;
	private PicClickListener picClickListener;

	public PublishGridAdapter(Context context, List<String> list) {
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	public void setPicClickListerter(PicClickListener picClickListener) {
		this.picClickListener = picClickListener;
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
		final int picPosition = position;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.pic_grid_, parent, false);
			holder.pic = (ImageView) convertView.findViewById(R.id.pic);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		if (list.get(position).equals("ling")) {
			holder.pic.setImageResource(R.drawable.ic_fabu_tianjia);
			if (picPosition > 10) {
				holder.pic.setVisibility(View.GONE);
			} else {
				holder.pic.setVisibility(View.VISIBLE);
			}

		} else {
			AppLog.i(PublishGridAdapter.class.getSimpleName(),
					"Â·¾¶:" + list.get(position));
			holder.pic.setImageBitmap(PhotoUtils.getNoCutSmallBitmap(list
					.get(position)));
		}

		holder.pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (picClickListener != null) {
					picClickListener.onPicClick(picPosition);
				}

			}
		});

		return convertView;
	}

	public class ViewHolder {
		public ImageView pic;
	}

	public interface PicClickListener {

		public void onPicClick(int position);

	}

}
