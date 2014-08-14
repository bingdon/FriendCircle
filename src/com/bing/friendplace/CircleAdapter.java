package com.bing.friendplace;

import java.util.List;

import com.bing.bean.MoodBean;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.image.LoadImageUtils;
import com.bing.support.time.TimeUtility;
import com.bing.support.view.ListUtils;
import com.bing.ui.custmeview.BingGridView;

import android.content.Context;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class CircleAdapter extends BaseAdapter {

	private List<MoodBean> list;

	private Context context;

	private LayoutInflater inflater;

	private PopupWindow popupWindow;

	private View popView;

	private ImageView comment;

	private ImageView laun;

	int[] location = new int[2];

	private CircleOnClickListener listener;

	private int realPostion = 0;

	public CircleAdapter(List<MoodBean> list, Context context) {

		this.list = list;

		this.inflater = LayoutInflater.from(context);

		this.context = context;

		popView = inflater.inflate(R.layout.pop_view, null);
		comment = (ImageView) popView.findViewById(R.id.comment);
		laun = (ImageView) popView.findViewById(R.id.laun);
		popupWindow = new PopupWindow(popView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.setFocusable(false);
		comment.setFocusable(false);
		laun.setFocusable(false);

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
			holder.commentmenu = (ImageView) convertView
					.findViewById(R.id.comment);
			holder.laundTextView = (TextView) convertView
					.findViewById(R.id.laud_txt);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
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
		holder.time.setText(""
				+ TimeUtility.getListTime(list.get(position).getCreatetime()));
		if (list.get(position).getUser().getUid().equals(G.uid)) {
			holder.delete.setVisibility(View.VISIBLE);
		} else {
			holder.delete.setVisibility(View.GONE);
		}
		holder.content.setText("" + list.get(position).getContent());
		holder.gridAdapter = new GridAdapter(list.get(position).getImg(),
				context);
		holder.bingGridView.setAdapter(holder.gridAdapter);
		holder.commentsAdapter = new CommentsAdapter(list.get(position)
				.getComment(), context);
		holder.comments.setAdapter(holder.commentsAdapter);
		ListUtils.setListViewHeightBasedOnChildren(holder.comments);

		if (list.get(position).isIslaud()) {
			holder.laundTextView.setVisibility(View.VISIBLE);
		} else {
			holder.laundTextView.setVisibility(View.INVISIBLE);
		}

		LoadImageUtils.loadOriginalImg(holder.userhead, HttpMethod.IMAG_URL
				+ list.get(position).getUser().getHeadimage());

		holder.commentmenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int width = popView.getWidth();
				realPostion = adapterPostion;
				if (width == 0) {
					width = 254;
				}

				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
					v.getLocationOnScreen(location);
					popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
							location[0] - width, location[1]);
				} else {
					v.getLocationOnScreen(location);
					popupWindow.showAtLocation(v, Gravity.NO_GRAVITY,
							location[0] - width, location[1]);
				}
			}
		});

		convertView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (popupWindow.isShowing()) {
					popupWindow.dismiss();
					return true;
				} else {
					return false;
				}
			}
		});

		comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onCommentClick(realPostion);
				}
			}
		});

		laun.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
				if (listener != null) {
					listener.onLaunClick(realPostion);
				}
			}
		});

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
		public ImageView userhead;
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
	}

	public interface CircleOnClickListener {
		public void onPicClick(int position, int picpostion);

		public void onCommentClick(int position);

		public void onLaunClick(int position);

		public void onDeleteClick(int position);
	}

}
