package com.bing.friendplace.utils;

import com.bing.friendplace.R;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;

public class NoticeUtils {
	private static NotificationManager notificationManager;

	private static Handler handler = new Handler();

	public static void notice(Context context, String message, int id) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		PendingIntent pendingIntent = null;

		Notification notification = null;

		notification = new NotificationCompat.Builder(context)

		.setTicker(context.getString(R.string.sending))
				.setContentTitle(context.getString(R.string.sending))
				.setContentText(message).setContentIntent(pendingIntent)
				.setOnlyAlertOnce(true).setOngoing(true)
				.setSmallIcon(R.drawable.upload_white).build();
		notificationManager.notify(id, notification);

	}

	public static void showSuccessfulNotification(Context context) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context)
				.setTicker(context.getString(R.string.send_successfully))
				.setContentTitle(context.getString(R.string.send_successfully))
				.setOnlyAlertOnce(true).setAutoCancel(true)
				.setSmallIcon(R.drawable.send_successfully).setOngoing(false);
		Notification notification = builder.build();
		notificationManager.notify(100, notification);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				notificationManager.cancel(100);
			}
		}, 3000);
	}

	public static void showProgressPublish(Context context, int progress,
			int max, int id) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context).setTicker(context.getString(R.string.send_photo))
				.setContentTitle(context.getString(R.string.send_photo))
				.setOnlyAlertOnce(true).setAutoCancel(true)
				.setSmallIcon(R.drawable.upload_white)
				.setProgress(max, progress, false).setOngoing(false);
		Notification notification = builder.build();
		notificationManager.notify(id, notification);

	}

	public static void showFailePublish(Context context) {

		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				context).setTicker(context.getString(R.string.send_failed))
				.setContentTitle(context.getString(R.string.send_failed))
				.setOnlyAlertOnce(true).setAutoCancel(true)
				.setSmallIcon(R.drawable.send_failed).setOngoing(false);

		Notification notification = builder.build();
		notificationManager.notify(100, notification);
		handler.postDelayed(new Runnable() {
			@Override
			public void run() {
				notificationManager.cancel(100);
			}
		}, 3000);

	}

	public static void removeNotice(int id, Context context) {
		if (null == notificationManager) {
			notificationManager = (NotificationManager) context
					.getSystemService(Context.NOTIFICATION_SERVICE);
		}

		notificationManager.cancel(id);
	}

}
