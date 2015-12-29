package com.daxiangqun.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.contacts.def.DxqDef;
import com.daxiangqun.contacts.progessbar.MyProgessBar;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.PreferencesUtil;
import com.daxiangqun.utils.ToastUtils;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class SetCallActivity extends Activity {
	private LinearLayout ly_back;
	private EditText et;
	private ImageView im_post;
	private Dialog progressDialog;// 进度条
	private DbManager db;
	private List<PersonContacts> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_call);
		ly_back = (LinearLayout) findViewById(R.id.setcall_back_ly);
		et = (EditText) findViewById(R.id.setcall_et);
		im_post = (ImageView) findViewById(R.id.setcall_im_post);
		ly_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		im_post.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				doHttpPostSMS();
			}
		});
		list = new ArrayList<PersonContacts>();
	}

	protected void doHttpPostSMS() {
		String etString = et.getText().toString();
		if (TextUtils.isEmpty(etString)) {
			ToastUtils.showToast(SetCallActivity.this, "请填写留言内容", 0);
			return;
		}
		if (!DxqUtils.isNetAvailable(this)) {
			ToastUtils.showToast(this, "网络出错,请设置网络", 0);
			return;
		}
		startProgressDialog();
		PreferencesUtil preferencesUtil = new PreferencesUtil(SetCallActivity.this, DxqDef.PreferencesName);
		String savePhone = preferencesUtil.getString("phone");
		RequestParams params = new RequestParams(DxqDef.SETCALL);
		String name = null;
		db = DxqUtils.openDb("personnel");
		try {
			list = db.selector(PersonContacts.class).where("tel", "=", savePhone)// 类别ID
					.or("phone1", "=", savePhone).or("phone2", "=", savePhone).findAll();
			name = list.get(0).getTitle();
		} catch (DbException e1) {
			e1.printStackTrace();
		}
		params.addBodyParameter("name", name);
		params.addBodyParameter("phone", savePhone);
		params.addBodyParameter("content", etString);
		x.http().post(params, new CommonCallback<String>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				ToastUtils.showToast(SetCallActivity.this, "取消网络", 0);
				stopProgressDialog();
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtils.showToast(SetCallActivity.this, "留言出错", 0);
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
						ToastUtils.showToast(SetCallActivity.this, "留言成功", 0);
					} else {
						ToastUtils.showToast(SetCallActivity.this, "留言失败,请再次提交", 0);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}

	// show（）一个ProgressBar
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = MyProgessBar.createLoadingDialog(SetCallActivity.this, null);
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
