package com.bing.friendplace;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.bing.bean.FriendBean;
import com.bing.friendplace.adapter.ConstactsAdapter;
import com.bing.friendplace.utils.PinyinComparator;
import com.bing.support.debug.G;
import com.bing.support.http.HttpMethod;
import com.bing.support.http.JsonUtils;
import com.bing.ui.custmeview.SideBar;
import com.bing.ui.custmeview.SideBar.OnTouchingLetterChangedListener;
import com.loopj.android.http.JsonHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class ContactsActivity extends BaseActivity {

	private ListView listView;

	private SideBar sideBar;

	private TextView dialog;

	private ConstactsAdapter constactsAdapter;

	private List<FriendBean> list = new ArrayList<>();


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_people_list);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		rightTextView.setText(R.string.done);
		titleTextView.setText(R.string.seccontacts);
		listView = (ListView) findViewById(R.id.constact_list);
		sideBar = (SideBar) findViewById(R.id.sideBar1);
		dialog = (TextView) findViewById(R.id.dialog);
		rightTextView.setOnClickListener(listener);
		sideBar.setTextView(dialog);
		constactsAdapter = new ConstactsAdapter(context, list);
		listView.setAdapter(constactsAdapter);
		listView.setFastScrollEnabled(true);

		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (list.get(position).isSec()) {
					list.get(position).setSec(false);
				}else {
					list.get(position).setSec(true);
				}
				
				constactsAdapter.notifyDataSetChanged();
				
			}
		});
		
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = constactsAdapter.getPositionForSection(s
						.charAt(0));
				if (position != -1) {
					listView.setSelection(position);
				}

			}
		});

		getMyfriend();
	}

	private void getMyfriend() {
		HttpMethod.getFriendList(G.uid, responseHandler);
	}

	private JsonHttpResponseHandler responseHandler = new JsonHttpResponseHandler() {

		@Override
		public void onSuccess(int statusCode, Header[] headers,
				JSONObject response) {
			// TODO Auto-generated method stub
			super.onSuccess(statusCode, headers, response);
			parseJson(response);
		}

		@Override
		public void onFinish() {
			// TODO Auto-generated method stub
			super.onFinish();
		}

		@Override
		public void onStart() {
			// TODO Auto-generated method stub
			super.onStart();
		}

	};

	private void parseJson(JSONObject jsonObject) {
		if (JsonUtils.isSuccess(jsonObject)) {
			JSONArray jsonArray;
			try {
				jsonArray = jsonObject.getJSONArray("friends");
				for (int i = 0; i < jsonArray.length(); i++) {
					FriendBean friendBean = JsonUtils.getFriendBean(jsonArray
							.getJSONObject(i));
					list.add(friendBean);
				}


				sort(list);

				constactsAdapter.notifyDataSetChanged();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	private void sort(List<FriendBean> list) {
		Collections.sort(list, new PinyinComparator());
	}
	
	private OnClickListener listener=new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.right_title_txt:
				doneSec();
				break;

			default:
				break;
			}
		}
	};
	
	
	private void doneSec(){
		Intent intent = new Intent(context, PublishActivity.class);
		intent.putExtra("atpeople", (Serializable)list);
		setResult(RESULT_OK, intent);
		finish();
	}
	

}
