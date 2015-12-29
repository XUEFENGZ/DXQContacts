package com.daxiangqun.contacts.activity;

import com.daxiangqun.contacts.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;

public class ContactActivity extends Activity {
	private LinearLayout ly_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact);
		ly_back = (LinearLayout) findViewById(R.id.con_back_ly);
		ly_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
