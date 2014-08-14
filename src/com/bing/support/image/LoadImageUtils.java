package com.bing.support.image;

import com.bing.app.FriendApp;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class LoadImageUtils {

	public static void loadOriginalImg(ImageView imageView, String url) {
		FriendApp.imageLoader.displayImage(url, imageView, FriendApp.options);
	}

	public static void getOriginalImg(String url,final String username){
		FriendApp.imageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				PhotoUtils.saveCurrent_ResultBitmap(loadedImage, username);
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
