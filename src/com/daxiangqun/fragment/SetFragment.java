package com.daxiangqun.fragment;

import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.activity.ContactActivity;
import com.daxiangqun.contacts.activity.LoginActivity;
import com.daxiangqun.contacts.activity.SetCallActivity;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.contacts.def.DxqDef;
import com.daxiangqun.contacts.progessbar.MyProgessBar;
import com.daxiangqun.listener.SavePhoneListener;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.PreferencesUtil;
import com.daxiangqun.utils.SavePhoneCode;
import com.daxiangqun.utils.ToastUtils;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class SetFragment extends BaseFragment implements OnClickListener, SavePhoneListener {
	private LinearLayout ly_sms, ly_call, ly_delete, ly_updata, ly_copy, ly_exit;
	private Dialog progressDialog;// 进度条
	public SavePhoneListener listener;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_set, container, false);
		ly_sms = (LinearLayout) view.findViewById(R.id.ly_set_sms);
		ly_call = (LinearLayout) view.findViewById(R.id.ly_set_call);
		ly_delete = (LinearLayout) view.findViewById(R.id.ly_set_delete);
		ly_updata = (LinearLayout) view.findViewById(R.id.ly_set_updata);
		ly_copy = (LinearLayout) view.findViewById(R.id.ly_set_copy);
		ly_exit = (LinearLayout) view.findViewById(R.id.ly_set_exit);
		ly_sms.setOnClickListener(this);
		ly_call.setOnClickListener(this);
		ly_delete.setOnClickListener(this);
		ly_updata.setOnClickListener(this);
		ly_copy.setOnClickListener(this);
		ly_exit.setOnClickListener(this);
		listener = this;
		return view;
	}

	@Override
	public View initView(LayoutInflater inflater) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ly_set_sms:
			Intent it_call = new Intent(getActivity(), SetCallActivity.class);
			startActivity(it_call);
			break;
		case R.id.ly_set_call:
			Intent it = new Intent(getActivity(), ContactActivity.class);
			startActivity(it);
			break;
		case R.id.ly_set_delete:
			// DxqUtils.delFile("dxqContacts");//删除本地数据
			PreferencesUtil pUtil2 = new PreferencesUtil(getActivity(), DxqDef.PreferencesName);
			pUtil2.clearData();// 删除登录缓存
			ToastUtils.showToast(getActivity(), "缓存已清除", 0);
			break;
		case R.id.ly_set_updata:
			ToastUtils.showToast(getActivity(), "已是最新版本", 0);
			break;
		case R.id.ly_set_copy:
			startProgressDialog();
			copyAllPhone();
			break;
		case R.id.ly_set_exit:
			PreferencesUtil pUtil = new PreferencesUtil(getActivity(), DxqDef.PreferencesName);
			pUtil.clearData();
			Intent intent = new Intent(getActivity(), LoginActivity.class);
			startActivity(intent);
			getActivity().finish();
			break;

		default:
			break;
		}

	}

	private void copyAllPhone() {
		new Thread(new Runnable() {
			public void run() {
				DbManager db = DxqUtils.openDb("personnel");
				try {
					List<PersonContacts> contactsList = db.selector(PersonContacts.class).orderBy("id").findAll();
					SavePhoneCode.CopyAll2Phone(contactsList, getActivity(), listener);
				} catch (DbException e) {
					e.printStackTrace();
					stopProgressDialog();
					ToastUtils.showToast(getActivity(), "数据错误", 0);
				}
			}
		}).start();

	}

	@Override
	public void onSavePhoneListener(int code) {
		stopProgressDialog();
		if (code == 0) {
			ToastUtils.showToast(getActivity(), "操作完成", 0);
		}
		if (code == 1) {
			ToastUtils.showToast(getActivity(), "操作失败", 0);
		}

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		stopProgressDialog();
	}

	// show（）一个ProgressBar
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = MyProgessBar.createLoadingDialog(getActivity(), null);
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
