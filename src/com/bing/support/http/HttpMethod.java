package com.bing.support.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpMethod {

	private static final String BASE_URL = "http://121.199.30.37:8888/CircleFriends";

	private static final String BASE_URL_ = BASE_URL + "/api/";

	private static final String GET_CIRCLE_INIO = BASE_URL_ + "myCircles";

	private static final String GET_OUR_MOOOD = BASE_URL_ + "userMoods";

	public static final String IMAG_URL = BASE_URL + "/upload";

	private static final String CIRCLE_MOOD = BASE_URL_ + "userCircleMoods";

	private static final String POST_MOOD_URL = BASE_URL_ + "postMood";

	private static final String GET_MY_FRIENDS = BASE_URL_ + "myCircles";

	private static final String POST_PIC_TO_MOOD = BASE_URL_ + "postMoodImg";

	private static final String POST_COMMENT_TO_MOOD = BASE_URL_
			+ "postMoodComment";

	private static final String POST_LAUN_TO_MOOD = BASE_URL_ + "postMoodLand";

	private static final String GET_NOTICE_COUNT = BASE_URL_
			+ "noticeMoodCount";

	private static final String GET_NOTICE_MOOD = BASE_URL_ + "noticeMood";

	private static final String GET_MOOD_INFO = BASE_URL_ + "moodInfo";

	private static final String GET_MY_MOOD = BASE_URL_ + "userMoods";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void getMyCircle(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_CIRCLE_INIO, params, responseHandler);
	}

	public static void getOurMood(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_OUR_MOOOD, params, responseHandler);
	}

	public static void getCircleMoods(String uid, int first, int limit,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("first", first);
		params.put("limit", limit);
		client.post(CIRCLE_MOOD, params, responseHandler);
	}

	/**
	 * ��������
	 * 
	 * @param uid
	 * @param context
	 *            ����
	 * @param viewlevel
	 *            �Ƿ�ɼ� 1Ϊ���� 0Ϊ˽��
	 * @param noticeuids
	 *            ��ʾ�û�ID �����","����
	 * @param address           
	 *            ʵʱλ��
	 * @param responseHandler
	 */
	public static void PostMood2Circle(String uid, String content,
			String viewlevel, String noticeuids,String address,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("content", content);
		params.put("viewlevel", viewlevel);
		params.put("noticeuids", noticeuids);
		params.put("address", address);
		client.post(POST_MOOD_URL, params, responseHandler);
	}

	/**
	 * ��������б�
	 * 
	 * @param uid
	 * @param responseHandler
	 */
	public static void getFriendList(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_MY_FRIENDS, params, responseHandler);
	}

	/**
	 * ���������ͼƬ
	 * 
	 * @param moodid
	 *            ����ID
	 * @param moodpicStr
	 *            ͼ��׫base64�ַ���
	 * @param handler
	 */
	public static void postMoodImg(String moodid, String moodpicStr,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("moodid", moodid);
		params.put("moodpicStr", moodpicStr);
		client.post(POST_PIC_TO_MOOD, params, handler);
	}

	/**
	 * ��������
	 * 
	 * @param moodid
	 * @param content
	 * @param uid
	 * @param handler
	 */
	public static void postMoodComment(String moodid, String content,
			String uid, AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("moodid", moodid);
		params.put("uid", uid);
		params.put("content", content);
		client.post(POST_COMMENT_TO_MOOD, params, handler);
	}

	/**
	 * ������
	 * 
	 * @param moodid
	 * @param uid
	 * @param handler
	 */
	public static void postMoodLand(String moodid, String uid,
			AsyncHttpResponseHandler handler) {
		RequestParams params = new RequestParams();
		params.put("moodid", moodid);
		params.put("uid", uid);
		client.post(POST_LAUN_TO_MOOD, params, handler);
	}

	/**
	 * �����������
	 * 
	 * @param uid
	 * @param responseHandler
	 */
	public static void noticeMoodCount(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_NOTICE_COUNT, params, responseHandler);
	}

	/**
	 * ��ȡ��ʾ����
	 * 
	 * @param uid
	 * @param responseHandler
	 */
	public static void noticeMood(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_NOTICE_MOOD, params, responseHandler);
	}

	public static void getMoodInfo(String moodid, String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("moodid", moodid);
		params.put("uid", uid);
		client.post(GET_MOOD_INFO, params, responseHandler);
	}

	/**
	 * ��ȡ��������
	 * @param uid
	 * @param first
	 * @param limit
	 * @param responseHandler
	 */
	public static void getuserMoods(String uid, int first, int limit,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		params.put("first", first);
		params.put("limit", limit);
		client.post(GET_MY_MOOD, params, responseHandler);
	}

}
