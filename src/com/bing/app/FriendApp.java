package com.bing.app;

import com.bing.friendplace.R;
import com.bing.support.file.FileUtility;
import com.bing.support.image.SdUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.Context;

public class FriendApp extends Application {

	private static FriendApp instance;
	public static ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this;
		initImageLoader(this);
		if (SdUtils.ExistSDCard()) {
			FileUtility.createPath();
		}
	}
	
	
	public static FriendApp getInstance() {
		return instance;
	}
	
	
	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				// .memoryCache(new LruMemoryCache(maxSize))
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				// .writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
		
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_loading_large_)
		.showImageForEmptyUri(R.drawable.ic_empty_)
		.showImageOnFail(R.drawable.ic_empty_).cacheInMemory(true)
		.cacheOnDisc(true).considerExifParams(true).build();
		
		
	}
	
}
