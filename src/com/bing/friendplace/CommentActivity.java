package com.bing.friendplace;

import org.apache.http.Header;
import org.json.JSONObject;

import com.bing.friendplace.constant.ConstantS;
import com.bing.friendplace.utils.NoticeUtils;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

public class CommentActivity extends BaseActivity {

	private EditText editText;
	private String moodid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_comment);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		moodid = getIntent().getStringExtra("id");
		editText = (EditText) findViewById(R.id.comment_edit);
		titleTextView.setText(R.string.comment);
		rightTextView.setText(R.string.submit);
		rightTextView.setOnClickListener(listener);
	}

	private OnClickListener listener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_title_txt:
				comment();
				break;

			default:
				break;
			}
		}
	};

	private void comment() {
		String content = editText.getText().toString();
		editText.setError(null);
		if (TextUtils.isEmpty(content)) {
			editText.setError(getString(R.string.nullcontentnotice));
			editText.requestFocus();
			return;
		}

		HttpMethod.postMoodComment(moodid, content, G.uid, responseHandler);

	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
			NoticeUtils.notice(context, getString(R.string.submit),
					ConstantS.PUBLISH_COMMENT);
			finish();
		}

		@Override
		public void onFailure(int statusCode, Header[] headers,
				Throwable throwable, JSONObject errorResponse) {
			// TODO Auto-generated method stub
			super.onFailure(statusCode, headers, throwable, errorResponse);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			NoticeUtils.showFailePublish(context);
		}

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			NoticeUtils.removeNotice(ConstantS.PUBLISH_COMMENT, context);
			if (JsonUtils.isSuccess(response)) {
				NoticeUtils.showSuccessfulNotification(context);
			} else {
				NoticeUtils.showFailePublish(context);
			}
		}

	};

}
