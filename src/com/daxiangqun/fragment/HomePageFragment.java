package com.daxiangqun.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.activity.PersonnelActivity;
import com.daxiangqun.contacts.activity.StartersActivity;
import com.daxiangqun.contacts.db.Office;
import com.daxiangqun.contacts.def.DxqDef;
import com.daxiangqun.contacts.jsonstring.doHttpRequestDb;
import com.daxiangqun.contacts.progessbar.MyProgessBar;
import com.daxiangqun.listener.DxqHttpListener;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.ToastUtils;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class HomePageFragment extends BaseFragment implements OnClickListener, DxqHttpListener {
	private Dialog progressDialog;// 进度条
	private LinearLayout ly_home;
	private ImageView im1, im2, im3, im4, im5, im6;
	private DxqHttpListener onDxqHttpListener;
	public static final int THREAD_ONE = 0;
	public static final int THREAD_TWO = 1;
	public static final int THREAD_THREE = 2;
	public static final int THREAD_FOUR = 3;
	public static final int THREAD_FIVE = 4;
	public static final int THREAD_END = 5;
	private List<Office> officeList;
	private DbManager db;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_home_page, container, false);
		x.view().inject(this, view);
		ly_home = (LinearLayout) view.findViewById(R.id.ly_home);
		im1 = (ImageView) view.findViewById(R.id.im1_home);
		im2 = (ImageView) view.findViewById(R.id.im2_home);
		im3 = (ImageView) view.findViewById(R.id.im3_home);
		im4 = (ImageView) view.findViewById(R.id.im4_home);
		im5 = (ImageView) view.findViewById(R.id.im5_home);
		im6 = (ImageView) view.findViewById(R.id.im6_home);
		im1.setOnClickListener(this);
		im2.setOnClickListener(this);
		im3.setOnClickListener(this);
		im4.setOnClickListener(this);
		im5.setOnClickListener(this);
		im6.setOnClickListener(this);
		onDxqHttpListener = this;
		officeList = new ArrayList<Office>();
		startProgressDialog();
		getPraseJson(DxqDef.PersonnelUrl, "personnel", THREAD_ONE, null);
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

	// show（）一个ProgressBar
	public void startProgressDialog() {
		if (progressDialog == null) {
			ly_home.setVisibility(View.GONE);
			progressDialog = MyProgessBar.createLoadingDialog(getActivity(), null);
		}
		progressDialog.show();
	}

	// dismiss()一个ProgressBar
	public void stopProgressDialog() {
		if (progressDialog != null) {
			ly_home.setVisibility(View.VISIBLE);
			progressDialog.dismiss();
			progressDialog = null;
		}
	}

	private void getPraseJson(String url, final String dbName, final int code, Object object) {
		if (!DxqUtils.isNetAvailable(getActivity())) {
			ToastUtils.showToast(getActivity(), "网络出错,请设置网络", 0);
			stopProgressDialog();
			return;
		}
		DxqUtils.FlLog("url=" + url);
		//DxqUtils.creatFile("dxqContacts");
		RequestParams params = new RequestParams(url);
		x.http().post(params, new CommonCallback<JSONArray>() {

			@Override
			public void onCancelled(CancelledException arg0) {
				ToastUtils.showToast(getActivity(), "取消网络", 0);
				stopProgressDialog();
			}

			@Override
			public void onError(Throwable arg0, boolean arg1) {
				ToastUtils.showToast(getActivity(), "初始化失败", 0);
				stopProgressDialog();
			}

			@Override
			public void onFinished() {
				// stopProgressDialog();
			}

			@Override
			public void onSuccess(JSONArray jsonArray) {
				
				db = DxqUtils.openDb(dbName);
				if (code == THREAD_ONE) {// 联系人
					doHttpRequestDb.doPersonnelDb(jsonArray, db, onDxqHttpListener);
					// stopProgressDialog();
				}
				if (code == THREAD_TWO) {
					doHttpRequestDb.doSectionDb(jsonArray, db, onDxqHttpListener);
				}
				if (code == THREAD_THREE) {
					doHttpRequestDb.doOfficeDb(jsonArray, db, onDxqHttpListener);
				}
				if (code == THREAD_FOUR) {
					doHttpRequestDb.doCityDb(jsonArray, db, onDxqHttpListener);
				}
				if (code == THREAD_FIVE) {
					doHttpRequestDb.doProvinceDb(jsonArray, db, onDxqHttpListener);
				}

			}
		});
	}

	@Override
	public void onCompleted(int code) {
		if (code == THREAD_TWO) {
			mHandler.sendEmptyMessage(THREAD_TWO);
		}
		if (code == THREAD_THREE) {
			mHandler.sendEmptyMessage(THREAD_THREE);
		}
		if (code == THREAD_FOUR) {
			mHandler.sendEmptyMessage(THREAD_FOUR);
		}
		if (code == THREAD_FIVE) {
			mHandler.sendEmptyMessage(THREAD_FIVE);
		}
		if (code == THREAD_END) {
			stopProgressDialog();// 关闭进度条
			ToastUtils.showToast(getActivity(), "初始化成功", 0);
		}
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case THREAD_TWO:
				getPraseJson(DxqDef.SectionUrl, "section", THREAD_TWO, null);// 部门
				break;
			case THREAD_THREE:
				getPraseJson(DxqDef.OfficeUrl, "office", THREAD_THREE, null);// 局
				break;
			case THREAD_FOUR:
				getPraseJson(DxqDef.CityUrl, "city", THREAD_FOUR, null);// 城市
				break;
			case THREAD_FIVE:
				getPraseJson(DxqDef.ProvinceUrl, "province", THREAD_FIVE, null);// 省份
				break;
			default:
				break;
			}
		};
	};

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.im1_home:
			DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName("office")
					.setDbDir(new File(DxqDef.DBPATH)).setDbVersion(1)
					.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
						@Override
						public void onUpgrade(final DbManager db, final int oldVersion, final int newVersion) {

						}
					});
			DbManager db = x.getDb(daoConfig);
			try {
				officeList = db.selector(Office.class).where("typeID", "=", 1).orderBy("id").findAll();
			} catch (DbException e) {
				e.printStackTrace();
			}
			Intent it = new Intent(getActivity(), PersonnelActivity.class);
			it.putExtra("code", 1);
			it.putExtra("officeID", officeList.get(0).getId());// 旅游局ID
			it.putExtra("site", officeList.get(0).getSite());// 地址
			it.putExtra("title", "国家旅游局");// 旅游局名字
			it.putExtra("postcode", officeList.get(0).getPostcode());// 邮编
			startActivity(it);
			// Intent it = new Intent(getActivity(), StartersActivity.class);
			// it.putExtra("title", "国家旅游局");
			// it.putExtra("code", 1);
			// it.putExtra("dbName", "office");
			// startActivity(it);
			break;
		case R.id.im2_home:
			Intent it2 = new Intent(getActivity(), StartersActivity.class);
			it2.putExtra("title", "国家旅游局驻外办事处");
			it2.putExtra("code", 2);
			it2.putExtra("dbName", "office");
			startActivity(it2);
			break;
		case R.id.im3_home:
			Intent it3 = new Intent(getActivity(), StartersActivity.class);
			it3.putExtra("title", "省，自治区，及直辖市旅游局");
			it3.putExtra("code", 3);
			it3.putExtra("dbName", "office");
			startActivity(it3);
			break;
		case R.id.im4_home:
			Intent it4 = new Intent(getActivity(), StartersActivity.class);
			it4.putExtra("title", "计划单列市旅游局");
			it4.putExtra("code", 4);
			it4.putExtra("dbName", "office");
			startActivity(it4);
			break;
		case R.id.im5_home:
			Intent it5 = new Intent(getActivity(), StartersActivity.class);
			it5.putExtra("title", "副省级城市旅游局");
			it5.putExtra("code", 5);
			it5.putExtra("dbName", "office");
			startActivity(it5);
			break;
		case R.id.im6_home:
			Intent it6 = new Intent(getActivity(), StartersActivity.class);
			it6.putExtra("title", "其他");
			it6.putExtra("code", 6);
			it6.putExtra("dbName", "office");
			startActivity(it6);
			break;

		default:
			break;
		}

	}
}
