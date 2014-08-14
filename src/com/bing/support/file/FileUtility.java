package com.bing.support.file;

import java.io.File;

import android.os.Environment;

public class FileUtility {

	public static final String FRIEND_PATH = Environment
			.getExternalStorageDirectory() + "/friend";
	public static final String FRIEND_PATH_IMG = FRIEND_PATH + "/image";

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	private static void createAll_Path() {
		File file = new File(FRIEND_PATH);
		if (!file.exists()) {
			file.mkdir();
		}
	}

	/**
	 * 创建文件夹
	 * 
	 * @param path
	 */
	public static void createPath() {
		createAll_Path();
		File file = new File(FRIEND_PATH_IMG);
		if (!file.exists()) {
			file.mkdir();
		}
	}

}
