package com.daxiangqun.contacts.activity;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.def.DxqDef;
import com.daxiangqun.contacts.progessbar.MyProgessBar;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.PreferencesUtil;
import com.daxiangqun.utils.ToastUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by Administrator on 2015/12/2.
 */
public class LoginActivity extends Activity implements View.OnClickListener {
	private ImageView im_user, im_code, im_bind;
	private EditText et_phone, et_code;
	private String phoneCode;
	private String successCode = null;
	private String successPhoneCode = null;
	private Dialog progressDialog;// 进度条
	//private LinearLayout login_ly1, login_ly2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		// ------------
//		login_ly1 = (LinearLayout) findViewById(R.id.login_ly1);
//		login_ly2 = (LinearLayout) findViewById(R.id.login_ly2);
		doIsLogin();
		im_user = (ImageView) findViewById(R.id.im_login_user);
		// im_user.setOnClickListener(this);
		im_code = (ImageView) findViewById(R.id.im_login_get);
		im_code.setOnClickListener(this);
		im_bind = (ImageView) findViewById(R.id.im_login_bind);
		im_bind.setOnClickListener(this);
		et_phone = (EditText) findViewById(R.id.et_login_phone);
		et_code = (EditText) findViewById(R.id.et_login_code);
		x.view().inject(this);

	}

	private void doIsLogin() {
		if (!DxqUtils.isNetAvailable(this)) {
			ToastUtils.showToast(this, "网络出错,请设置网络", 0);
			return;
		}
		ToastUtils.showToast(this, "正在自动登录...", 0);
//		login_ly1.setVisibility(View.GONE);
//		login_ly2.setVisibility(View.GONE);
		startProgressDialog();
		PreferencesUtil preferencesUtil = new PreferencesUtil(LoginActivity.this,
				DxqDef.PreferencesName);
		String savePhone = preferencesUtil.getString("phone");
		String saveCode = preferencesUtil.getString("code");
		if(TextUtils.isEmpty(savePhone)||TextUtils.isEmpty(saveCode)){
//			login_ly1.setVisibility(View.VISIBLE);
//			login_ly2.setVisibility(View.VISIBLE);
			stopProgressDialog();
			ToastUtils.showToast(this, "请重新登录", 0);
			return;
		}
		RequestParams params = new RequestParams(DxqDef.ISLOGIN);
		params.addBodyParameter("phone", savePhone);
		params.addBodyParameter("code", saveCode);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				ToastUtils.showToast(LoginActivity.this, "取消网络", 0);
				stopProgressDialog();
//				login_ly1.setVisibility(View.VISIBLE);
//				login_ly2.setVisibility(View.VISIBLE);
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtils.showToast(LoginActivity.this, "自动登录失败", 0);
				stopProgressDialog();
//				login_ly1.setVisibility(View.VISIBLE);
//				login_ly2.setVisibility(View.VISIBLE);
			}

			@Override
			public void onFinished() {
				stopProgressDialog();
			}

			@Override
			public void onSuccess(String arg0) {
				try {
					JSONObject object = new JSONObject(arg0);
					if (object.getString("success").equals("true")) {
						Intent intent = new Intent(LoginActivity.this, MainActivity.class);
						startActivity(intent);
						finish();
					} else {
						ToastUtils.showToast(LoginActivity.this, "请输入账号验证登录", 0);
//						login_ly1.setVisibility(View.VISIBLE);
//						login_ly2.setVisibility(View.VISIBLE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im_login_get:
			getPhoneBindCode();
			break;
		case R.id.im_login_bind:
			doPhoneBindCodeLogin();
			break;
		}
	}

	private void getPhoneBindCode() {
		phoneCode = et_phone.getText().toString();
		if (phoneCode != null && !phoneCode.equals("")) {
			if (DxqUtils.isMobileNO(phoneCode)) {
				getCodeHttp();
			} else {
				ToastUtils.showToast(this, "手机号码格式错误", 0);
			}
		} else {
			ToastUtils.showToast(this, "请输入电话号码", 0);
		}
	}

	private void getCodeHttp() {
		if (!DxqUtils.isNetAvailable(this)) {
			ToastUtils.showToast(this, "网络出错,请设置网络", 0);
			return;
		}
		startProgressDialog();
		RequestParams params = new RequestParams(DxqDef.SetSMSUrl);
		params.addBodyParameter("phone", phoneCode);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				ToastUtils.showToast(LoginActivity.this, "取消网络", 0);
				stopProgressDialog();
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtils.showToast(LoginActivity.this, "网络失败", 0);
				stopProgressDialog();
			}

			@Override
			public void onFinished() {
				stopProgressDialog();
			}

			@Override
			public void onSuccess(String arg0) {
				// {"success":true,"yzm":"126957","jurisdictionID":"1","phone":"18614044202"}
				try {
					JSONObject object = new JSONObject(arg0);
					if (object.getString("success").equals("true")) {
						successCode = object.getString("yzm");
						successPhoneCode = object.getString("phone");
						ToastUtils.showToast(LoginActivity.this, "验证码已发送", 0);
						DxqUtils.FlLog(arg0);
					} else {
						ToastUtils.showToast(LoginActivity.this, "权限无效,获取验证码失败", 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

		});
	}

	private void doPhoneBindCodeLogin() {
		if (!DxqUtils.isNetAvailable(this)) {
			ToastUtils.showToast(this, "网络出错,请设置网络", 0);
			return;
		}
		final String bindCode = et_code.getText().toString();
		phoneCode = et_phone.getText().toString();
		if (!phoneCode.equals(successPhoneCode) || !bindCode.equals(successCode)) {
			ToastUtils.showToast(LoginActivity.this, "验证码输入错误", 0);
			return;
		}
		startProgressDialog();
		if (bindCode != null && !bindCode.equals("")) {
			RequestParams params = new RequestParams(DxqDef.BindSMSUrl);
			params.addBodyParameter("code", bindCode);
			x.http().post(params, new CommonCallback<String>() {

				@Override
				public void onCancelled(CancelledException arg0) {
					ToastUtils.showToast(LoginActivity.this, "取消网络", 0);
					stopProgressDialog();
				}

				@Override
				public void onError(Throwable arg0, boolean arg1) {
					ToastUtils.showToast(LoginActivity.this, "获取失败", 0);
					stopProgressDialog();
				}

				@Override
				public void onFinished() {
					stopProgressDialog();
				}

				@Override
				public void onSuccess(String arg0) {
					try {
						JSONObject object = new JSONObject(arg0);
						if (object.getString("success").equals("true")) {
							PreferencesUtil preferencesUtil = new PreferencesUtil(LoginActivity.this,
									DxqDef.PreferencesName);
							preferencesUtil.putString("phone", phoneCode);
							preferencesUtil.putString("code", bindCode);
							Intent intent = new Intent(LoginActivity.this, MainActivity.class);
							startActivity(intent);
							finish();
						} else {
							ToastUtils.showToast(LoginActivity.this, "失败,请重新绑定", 0);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			ToastUtils.showToast(this, "请输入验证码", 0);
		}
	}

	// show（）一个ProgressBar
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = MyProgessBar.createLoadingDialog(LoginActivity.this, null);
		}
		progressDialog.show();
	}

	// dismiss()一个ProgressBar
	public void stopProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			progressDialog = null;
		}
	}
}
