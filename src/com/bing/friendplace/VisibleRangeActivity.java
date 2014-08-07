package com.bing.friendplace;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class VisibleRangeActivity extends BaseActivity implements
		OnCheckedChangeListener {

	private RadioGroup radiogroup;
	public static int type=1;

	private RadioButton mRadioButton1;
	private RadioButton mRadioButton2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_visible_range);
		OnInitView();
	}

	@Override
	protected void OnInitView() {
		// TODO Auto-generated method stub
		super.OnInitView();
		radiogroup = (RadioGroup) findViewById(R.id.radioGroup1);
		radiogroup.setOnCheckedChangeListener(this);
		mRadioButton1=(RadioButton)findViewById(R.id.publicc);
		mRadioButton2=(RadioButton)findViewById(R.id.privatee);
		if (type==0) {
			mRadioButton1.setChecked(false);
			mRadioButton2.setChecked(true);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
		switch (checkedId) {
		case R.id.publicc:
			type=1;
			break;

		case R.id.privatee:
			type=0;
			break;

		default:
			break;
		}
	}
	
	
	
	
	

}
