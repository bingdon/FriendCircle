package com.bing.support.image;

import com.bing.app.FriendApp;

import android.widget.ImageView;

public class LoadImageUtils {

	public static void loadOriginalImg(ImageView imageView, String url) {
		FriendApp.imageLoader.displayImage(url, imageView, FriendApp.options);
	}

}
