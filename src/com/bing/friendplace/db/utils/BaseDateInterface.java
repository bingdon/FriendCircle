package com.bing.friendplace.db.utils;

public interface BaseDateInterface {

	public long delete();

	public long deleteAll();

	public Object queryData();

	public Object queryData(int pagersize);

	public void close();

}
