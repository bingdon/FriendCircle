package com.bing.support.image;

import java.io.ByteArrayOutputStream;

import com.bing.friendplace.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

public class PhotoUtils {
	public static Bitmap getNoCutSmallBitmap(String filePath) {

		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 400, 480);

		options.inJustDecodeBounds = false;

		Bitmap mBitmap = BitmapFactory.decodeFile(filePath, options);

		return mBitmap;
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public static Uri imageFileUri;

	// 图片上传选择途径
	public static void MyDialog(final Activity context) {
		final CharSequence[] items = { context.getString(R.string.photo),
				context.getString(R.string.takepic) };
		AlertDialog dlg = new AlertDialog.Builder(context)
				.setTitle(context.getString(R.string.secphoto))
				.setItems(items, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// 这里item是根据选择的方式，
						// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
						if (item == 1) {
							Intent getImageByCamera = new Intent(
									"android.media.action.IMAGE_CAPTURE");
							context.startActivityForResult(getImageByCamera, 1);
						} else {
							Intent getImage = new Intent(
									Intent.ACTION_GET_CONTENT);
							getImage.addCategory(Intent.CATEGORY_OPENABLE);
							getImage.setType("image/jpeg");
							context.startActivityForResult(getImage, 0);
						}
					}
				}).create();
		dlg.show();
	}
	
	
	public static String getPicPathFromUri(Uri uri, Activity activity) {
		String value = uri.getPath();

		if (value.startsWith("/external")) {
			String[] proj = { MediaStore.Images.Media.DATA };
			Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		} else {
			return value;
		}
	}

	
	// 图片上传选择途径
		public static void secPic(final Activity context) {
			final CharSequence[] items = { "相册", "拍照" };
			AlertDialog dlg = new AlertDialog.Builder(context).setTitle("选择图片")
					.setItems(items, new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int item) {
							// 这里item是根据选择的方式，
							// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
							if (item == 1) {
								if (SdUtils.ExistSDCard()) {
									try {
										imageFileUri = context
												.getContentResolver()
												.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
														new ContentValues());
									} catch (Exception e) {
										// TODO: handle exception
										e.printStackTrace();
									}

								} else {
									imageFileUri = context
											.getContentResolver()
											.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI,
													new ContentValues());
								}

								if (imageFileUri != null) {
									Intent getImageByCamera = new Intent(
											"android.media.action.IMAGE_CAPTURE");

									getImageByCamera
											.putExtra(
													android.provider.MediaStore.EXTRA_OUTPUT,
													imageFileUri);

									context.startActivityForResult(
											getImageByCamera, 1);
								} else {
									Toast.makeText(
											context,
											context.getResources().getString(
													R.string.cant_insert_album),
											Toast.LENGTH_SHORT).show();
								}

							} else {
								Intent getImage = new Intent(
										Intent.ACTION_GET_CONTENT);
								getImage.addCategory(Intent.CATEGORY_OPENABLE);
								getImage.setType("image/jpeg");
								context.startActivityForResult(getImage, 0);
							}
						}
					}).create();
			dlg.show();
		}
	
		
		/**
		 * 把bitmap转换成String
		 * 
		 * @param filePath
		 * @return
		 */
		public static synchronized String bitmapNCutToString(String filePath) {

			Bitmap bm = getNoCutSmallBitmap(filePath);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 40, baos);
			byte[] b = baos.toByteArray();

			return Base64.encodeToString(b, Base64.DEFAULT);

		}
		

}
