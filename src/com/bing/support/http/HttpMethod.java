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

	public static final String CIRCLE_MOOD=BASE_URL_+"userCircleMoods";
	
	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void getMyCircle(String uid,
			AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("uid", uid);
		client.post(GET_CIRCLE_INIO, params, responseHandler);
	}

	public static void getOurMood(String uid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params=new RequestParams();
		params.put("uid", uid);
		client.post(GET_OUR_MOOOD, params, responseHandler);
	}

	public static void getCircleMoods(String uid, AsyncHttpResponseHandler responseHandler) {
		RequestParams params=new RequestParams();
		params.put("uid", uid);
		client.post(CIRCLE_MOOD, params, responseHandler);
	}
	
}
