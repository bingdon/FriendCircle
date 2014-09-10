package com.bing.friendplace.adapter;

import java.util.List;

import com.bing.bean.MoodBean;
import com.bing.friendplace.CommentsAdapter;
import com.bing.friendplace.GridAdapter;
import com.bing.friendplace.R;
import com.bing.support.view.ListUtils;
import com.bing.ui.custmeview.BingGridView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class AblumAdapter extends BaseAdapter {
	private List<MoodBean> list;

	private Context context;

	private LayoutInflater inflater;

	int[] location = new int[2];

	private CircleOnClickListener listener;


	public AblumAdapter(List<MoodBean> list, Context context) {

		this.list = list;

		this.inflater = LayoutInflater.from(context);

		this.context = context;

	}

	public void setOnClickLitener(CircleOnClickListener listener) {
		this.listener = listener;
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
		final int adapterPostion = position;
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.ablum_item, parent, false);
			holder.day = (TextView) convertView.findViewById(R.id._day);
			holder.month = (TextView) convertView.findViewById(R.id._month);
			holder.bingGridView = (BingGridView) convertView
					.findViewById(R.id.picGridView1);
			holder.content = (TextView) convertView.findViewById(R.id.content);
			holder.time = (TextView) convertView.findViewById(R.id.time);
			holder.comments = (ListView) convertView
					.findViewById(R.id.comment_list);
			holder.username = (TextView) convertView
					.findViewById(R.id.username);
			holder.commentmenu = (ImageView) convertView
					.findViewById(R.id.comment);
			holder.laundTextView = (TextView) convertView
					.findViewById(R.id.laud_txt);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
			holder.address=(TextView)convertView.findViewById(R.id.address);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.bingGridView.setFocusable(false);
		holder.bingGridView.setFocusableInTouchMode(false);
		holder.comments.setFocusable(false);
		holder.comments.setFocusableInTouchMode(false);
		holder.content.setFocusable(false);
		holder.content.setFocusableInTouchMode(false);

		holder.username
				.setText("" + list.get(position).getUser().getNickname());
		holder.time.setVisibility(View.GONE);
		// holder.time.setText(""
		// + TimeUtility.getListTime(list.get(position).getCreatetime()));
		holder.content.setText("" + list.get(position).getContent());
		holder.address.setText(list.get(position).getAddress());
		holder.gridAdapter = new GridAdapter(list.get(position).getImg(),
				context);
		holder.bingGridView.setAdapter(holder.gridAdapter);
		holder.commentsAdapter = new CommentsAdapter(list.get(position)
				.getComment(), context);
		holder.comments.setAdapter(holder.commentsAdapter);
		ListUtils.setListViewHeightBasedOnChildren(holder.comments);
		holder.laundTextView.setVisibility(View.VISIBLE);
		int likecou = list.get(position).getLaudcount();
		if (list.get(position).isIslaud()) {
			holder.laundTextView.setText(context.getString(R.string.launed)
					+ "(" + likecou + ")");
		} else {
			if (likecou > 0) {
				holder.laundTextView.setText(context.getString(R.string.zan)
						+ "(" + likecou + ")");
			} else {
				holder.laundTextView.setText(R.string.zan);
			}

		}

		holder.day.setText("" + list.get(position).getDay());
		holder.month.setText(list.get(position).getMonth());

		holder.commentmenu.setVisibility(View.INVISIBLE);

		holder.bingGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onPicClick(adapterPostion, position);
				}
			}
		});

		holder.delete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (listener != null) {
					listener.onDeleteClick(adapterPostion);
				}
			}
		});

		return convertView;
	}

	public static class ViewHolder {
		public TextView day;
		public TextView month;
		public TextView username;
		public TextView content;
		public TextView time;
		public BingGridView bingGridView;
		public ListView comments;
		public GridAdapter gridAdapter;
		public CommentsAdapter commentsAdapter;
		public ImageView commentmenu;
		public TextView laundTextView;
		public TextView delete;
		public TextView address;
	}

	public interface CircleOnClickListener {
		public void onPicClick(int position, int picpostion);

		public void onDeleteClick(int position);
	}

}
