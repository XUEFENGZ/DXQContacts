package com.daxiangqun.contacts.activity;

import com.daxiangqun.contacts.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;

public class WelcomeActivity extends Activity{
	private ImageView mStart_im;
	private TextView mStart_tv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mStart_im = (ImageView) findViewById(R.id.activity_wl_start_im);
		mStart_tv = (TextView) findViewById(R.id.activity_wl_start_tv);
		mStart_im.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setIntent();
			}
		});
		appendAnimation();//设置一个时间，如果60S内无点击事件，自动跳转
	}
	private void appendAnimation() {
		AlphaAnimation ani = new AlphaAnimation(0.0f, 1.0f);
		ani.setRepeatMode(Animation.REVERSE);
		ani.setRepeatCount(0);
		ani.setDuration(60000);// 20s
		mStart_tv.setAnimation(ani);
		ani.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}
			@Override
			public void onAnimationEnd(Animation animation) {
				setIntent();
			}
		});
	}
	private void setIntent() {
			Intent it_start = new Intent(WelcomeActivity.this,LoginActivity.class);
			startActivity(it_start);
			this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
