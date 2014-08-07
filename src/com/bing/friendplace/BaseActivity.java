package com.bing.friendplace;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class BaseActivity extends Activity {

	protected TextView leftTextView;
	
	protected TextView titleTextView;
	
	protected TextView rightTextView;
	
	protected ImageView rightImageView;
	
	protected Activity context;
	
	
	protected void OnInitView(){
		
		context=this;
		
		leftTextView=(TextView)findViewById(R.id.left_txt);
		rightTextView=(TextView)findViewById(R.id.right_title_txt);
		rightImageView=(ImageView)findViewById(R.id.right_title_img);
		titleTextView=(TextView)findViewById(R.id.title_txt);
		
		leftTextView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
	
	protected void OnInitData(){
		
	}
	
	
	
}
