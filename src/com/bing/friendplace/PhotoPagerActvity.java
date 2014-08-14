package com.bing.friendplace;

import java.util.HashSet;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import com.bing.app.FriendApp;
import com.bing.bean.MoodBean;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PhotoPagerActvity extends BaseActivity implements
		OnPageChangeListener {

	private ViewPager pager;

	private TextView postionTextView;
	private TextView sumTextView;
	private ProgressBar loadingBar;
	private String[] imgs;
	private HashSet<ViewGroup> unRecycledViews = new HashSet<ViewGroup>();
	private boolean loads2f = false;
	private PhotoPagerAdapter mAdapter;
	private DisplayImageOptions options;
	private View actionbar;
	private MoodBean mood;
	private TextView zan_num;
	private TextView comment_num;
	private TextView zan;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_photo_pager);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();

		actionbar = findViewById(R.id.actionbar);
		actionbar.setBackgroundColor(getResources().getColor(
				R.color.actionbar_tran_color));

		zan_num = (TextView) findViewById(R.id.zan_num);
		comment_num = (TextView) findViewById(R.id.comment_num);
		zan = (TextView) findViewById(R.id.zan_txt);

		options = new DisplayImageOptions.Builder()
				.showImageForEmptyUri(R.drawable.pic_empty)
				.showImageOnFail(R.drawable.pic_failure).cacheInMemory(true)
				.cacheOnDisc(true).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		pager = (ViewPager) findViewById(R.id.photo_pager);
		sumTextView = (TextView) findViewById(R.id.sum_textV);
		postionTextView = (TextView) findViewById(R.id.index_textV);
		loadingBar = (ProgressBar) findViewById(R.id.pic_progressBar);

		initData();

	}

	private void initData() {
		mood = (MoodBean) getIntent().getSerializableExtra("mood");
		zan_num.setText("" + mood.getLaudcount());
		comment_num.setText("" + mood.getComment().length);
		imgs = mood.getImg();
		sumTextView.setText("" + imgs.length);
		int postion = getIntent().getIntExtra("picpostion", 0);
		postionTextView.setText("" + (postion + 1));

		if (mood.isIslaud()) {
			zan.setText(R.string.launed);
			zan.setTextColor(getResources().getColor(R.color.deepskyblue));
		}

		mAdapter = new PhotoPagerAdapter(imgs);
		pager.setAdapter(mAdapter);

		pager.setOnPageChangeListener(this);
		pager.setCurrentItem(postion);

		findViewById(R.id.comment_lay).setOnClickListener(listener);
		findViewById(R.id.comment_txt).setOnClickListener(listener);
		zan.setOnClickListener(listener);
	}

	public class PhotoPagerAdapter extends PagerAdapter {

		private String[] imgs;

		public PhotoPagerAdapter(String[] imgs) {
			this.imgs = imgs;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imgs.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0.equals(arg1);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			if (!loads2f) {
				if (object instanceof ViewGroup) {
					((ViewPager) container).removeView((View) object);
					unRecycledViews.remove(object);

				}
			}

		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View imageLayout = getLayoutInflater().inflate(
					R.layout.photo_pager_, container, false);
			assert imageLayout != null;

			handelImg(imageLayout, imgs, position);
			// container.addView(imageLayout, position);
			((ViewPager) container).addView(imageLayout, 0);
			unRecycledViews.add((ViewGroup) imageLayout);

			return imageLayout;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position,
				Object object) {
			// TODO Auto-generated method stub
			super.setPrimaryItem(container, position, object);
			View imageLayout = (View) object;
			if (imageLayout == null) {
				return;
			}

			ImageView imageView = (ImageView) imageLayout
					.findViewById(R.id.photoView1);

			if (imageView.getDrawable() != null) {
				return;
			}

			handelImg(imageLayout, imgs, position);
		}

		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return super.saveState();
		}

	}

	private void handelImg(View imageLayout, String[] imgs, int position) {
		final PhotoView photoView = (PhotoView) imageLayout
				.findViewById(R.id.photoView1);

		photoView.setOnPhotoTapListener(new OnPhotoTapListener() {

			@Override
			public void onPhotoTap(View view, float x, float y) {
				// TODO Auto-generated method stub
				if (photoView == null
						|| (!(photoView.getDrawable() instanceof BitmapDrawable))) {
					context.finish();
					return;
				}
			}
		});

		FriendApp.imageLoader.displayImage(
				HttpMethod.IMAG_URL + imgs[position], photoView, options,
				new SimpleImageLoadingListener() {

					@Override
					public void onLoadingStarted(String imageUri, View view) {
						// TODO Auto-generated method stub
						super.onLoadingStarted(imageUri, view);
						loadingBar.setVisibility(View.VISIBLE);
					}

					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						// TODO Auto-generated method stub
						super.onLoadingFailed(imageUri, view, failReason);
						loadingBar.setVisibility(View.GONE);
						loads2f = true;
					}

					@Override
					public void onLoadingComplete(String imageUri, View view,
							Bitmap loadedImage) {
						// TODO Auto-generated method stub
						super.onLoadingComplete(imageUri, view, loadedImage);
						loadingBar.setVisibility(View.GONE);
					}

					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						// TODO Auto-generated method stub
						super.onLoadingCancelled(imageUri, view);
						loadingBar.setVisibility(View.GONE);
					}

				});

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		postionTextView.setText("" + (arg0 + 1));
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.comment_lay:
				loopMoodInfo(mood);
				break;

			case R.id.zan_txt:
				laun();
				break;

			case R.id.comment_txt:
				loopComment();
				break;
			default:
				break;
			}
		}

	};

	private void laun() {
		HttpMethod.postMoodLand(mood.getId(), G.uid,
				new JsonHttpResponseHandler());
		mood.setIslaud(true);
		zan.setText(R.string.launed);
		zan.setTextColor(getResources().getColor(R.color.deepskyblue));
	}

	private void loopMoodInfo(MoodBean moodBean) {
		Intent intent = new Intent();
		intent.putExtra("moodBean", moodBean);
		intent.setClass(context, MoodInfoActivity.class);
		startActivity(intent);
	}

	private void loopComment() {
		Intent intent = new Intent();
		intent.putExtra("id", mood.getId());
		intent.setClass(context, CommentActivity.class);
		startActivity(intent);
	}

}
