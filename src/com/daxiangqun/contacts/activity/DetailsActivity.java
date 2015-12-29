package com.daxiangqun.contacts.activity;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.contacts.db.Recent;
import com.daxiangqun.contacts.library.RoundedImageView;
import com.daxiangqun.contacts.progessbar.MyProgessBar;
import com.daxiangqun.listener.SavePhoneListener;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.SaveOnePhoneCode;
import com.daxiangqun.utils.SavePhoneCode;
import com.daxiangqun.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DetailsActivity extends Activity implements OnClickListener, SavePhoneListener {
	private RoundedImageView im_attachpath;
	private ImageView im_tel, im_phone1, im_phone2, im_sms1, im_sms2, im_copy;
	private TextView tv_title, tv_name, tv_duty, tv_tel, tv_fax, tv_phone1, tv_phone2, tv_email, tv_url, tv_site;
	private String id, title, attachpath, duty, tel, fax, phone1, phone2, email, url, site;
	private LinearLayout ly_back;
	private DisplayImageOptions options;
	private DbManager db = null;
	private RelativeLayout ly_phone2, ly_email, ly_url, ly_address, ly_phone1;
	public SavePhoneListener listener;
	private Dialog progressDialog;// 进度条

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_details);
		initDatas();// 初始化
		getIntentDatas();// 获取传递数据
		creatSaveDb();
		// getCallTime();
		listener = this;
	}

	private void initDatas() {
		im_attachpath = (RoundedImageView) findViewById(R.id.dt_duty_im);
		im_tel = (ImageView) findViewById(R.id.dt_tel_im);
		im_tel.setOnClickListener(this);
		im_phone1 = (ImageView) findViewById(R.id.dt_phone1_im);
		im_phone1.setOnClickListener(this);
		im_phone2 = (ImageView) findViewById(R.id.dt_phone2_im);
		im_phone2.setOnClickListener(this);
		im_sms1 = (ImageView) findViewById(R.id.dt_phone1_imsms);
		im_sms1.setOnClickListener(this);
		im_sms2 = (ImageView) findViewById(R.id.dt_phone2_imsms);
		im_sms2.setOnClickListener(this);
		im_copy = (ImageView) findViewById(R.id.dt_title_imcopy);
		im_copy.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.dt_title_tv);// 大标题
		tv_name = (TextView) findViewById(R.id.dt_name_tv);// 名字
		tv_duty = (TextView) findViewById(R.id.dt_duty_tv2);// 职务
		tv_tel = (TextView) findViewById(R.id.dt_tel_tv);//
		tv_fax = (TextView) findViewById(R.id.dt_fax_tv);//
		tv_phone1 = (TextView) findViewById(R.id.dt_phone1_tv);//
		tv_phone2 = (TextView) findViewById(R.id.dt_phone2_tv);//
		tv_email = (TextView) findViewById(R.id.dt_email_tv);//
		tv_url = (TextView) findViewById(R.id.dt_url_tv);//
		tv_site = (TextView) findViewById(R.id.dt_address_tv);//

		ly_back = (LinearLayout) findViewById(R.id.dt_back_ly);
		ly_back.setOnClickListener(this);
		options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.icon)
				.showImageForEmptyUri(R.drawable.icon).showImageOnFail(R.drawable.icon).cacheInMemory(true)
				.cacheOnDisk(true).considerExifParams(true).displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
				.build();

		// ------------ly---------------
		ly_phone2 = (RelativeLayout) findViewById(R.id.det_ly_phone2);
		ly_email = (RelativeLayout) findViewById(R.id.det_ly_email);
		ly_url = (RelativeLayout) findViewById(R.id.det_ly_url);
		ly_address = (RelativeLayout) findViewById(R.id.det_ly_address);
		ly_phone1 = (RelativeLayout) findViewById(R.id.det_ly_phone1);
	}

	private void getIntentDatas() {
		id = getIntent().getStringExtra("id");
		title = getIntent().getStringExtra("title");
		attachpath = getIntent().getStringExtra("attachpath");
		duty = getIntent().getStringExtra("duty");
		tel = getIntent().getStringExtra("tel");
		fax = getIntent().getStringExtra("fax");
		phone1 = getIntent().getStringExtra("phone1");
		phone2 = getIntent().getStringExtra("phone2");
		email = getIntent().getStringExtra("email");
		url = getIntent().getStringExtra("url");
		site = getIntent().getStringExtra("site");

		tv_title.setText(title);
		tv_name.setText(title);
		tv_duty.setText(duty);
		tv_tel.setText(tel);
		tv_fax.setText(fax);
		// tv_phone1.setText(phone1);
		isShowLinearLayout(phone1, tv_phone1, ly_phone1);
		isShowLinearLayout(phone2, tv_phone2, ly_phone2);
		isShowLinearLayout(email, tv_email, ly_email);
		isShowLinearLayout(url, tv_url, ly_url);
		isShowLinearLayout(site, tv_site, ly_address);
		ImageLoader.getInstance().displayImage(attachpath, im_attachpath, options, null);
	}

	private void isShowLinearLayout(String name, TextView tv, RelativeLayout ly) {
		if (TextUtils.isEmpty(name)) {
			ly.setVisibility(View.GONE);
		} else {
			tv.setText(name);
		}
	}

	@Override
	public void onClick(View v) {
		if (db == null) {
			creatSaveDb();
		}
		switch (v.getId()) {
		case R.id.dt_back_ly:
			finish();
			break;
		case R.id.dt_tel_im:
			if (tel != null && !tel.equals("")) {
				Intent it_tel = new Intent();
				it_tel.setAction(Intent.ACTION_CALL);
				it_tel.setData(Uri.parse("tel:" + tel));// 指定电话号码
				startActivity(it_tel);
				Recent recent = new Recent();
				recent.setUserid(id);
				recent.setTitle(title);
				recent.setTel(tel);
				recent.setDuty(duty);
				recent.setCalltime(getCallTime());
				recent.setAttachpath(attachpath);
				try {
					db.saveOrUpdate(recent);
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else {
				ToastUtils.showToast(this, "电话号为空", 0);
			}
			break;
		case R.id.dt_phone1_im:
			if (phone1 != null && !phone1.equals("") && DxqUtils.isMobileNO(phone1)) {
				Intent it_phone1 = new Intent();
				it_phone1.setAction(Intent.ACTION_CALL);
				it_phone1.setData(Uri.parse("tel:" + phone1));// 指定电话号码
				startActivity(it_phone1);
				Recent recent1 = new Recent();
				recent1.setUserid(id);
				recent1.setTitle(title);
				recent1.setTel(phone1);
				recent1.setDuty(duty);
				recent1.setCalltime(getCallTime());
				recent1.setAttachpath(attachpath);
				try {
					db.saveOrUpdate(recent1);
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else {
				ToastUtils.showToast(this, "手机号格式不对", 0);
			}
			break;
		case R.id.dt_phone2_im:

			if (phone2 != null && !phone2.equals("") && DxqUtils.isMobileNO(phone2)) {
				Intent it_phone2 = new Intent();
				it_phone2.setAction(Intent.ACTION_CALL);
				it_phone2.setData(Uri.parse("tel:" + phone2));// 指定电话号码
				startActivity(it_phone2);
				Recent recent2 = new Recent();
				recent2.setUserid(id);
				recent2.setTitle(title);
				recent2.setTel(phone2);
				recent2.setDuty(duty);
				recent2.setCalltime(getCallTime());
				recent2.setAttachpath(attachpath);
				try {
					db.saveOrUpdate(recent2);
				} catch (DbException e) {
					e.printStackTrace();
				}
			} else {
				ToastUtils.showToast(this, "手机号格式不对", 0);
			}
			break;
		case R.id.dt_phone1_imsms:
			if (phone1 != null && !phone1.equals("") && DxqUtils.isMobileNO(phone1)) {
				Uri smsToUri = Uri.parse("smsto:" + phone1);
				Intent phone1_sms = new Intent(Intent.ACTION_SENDTO, smsToUri);
				startActivity(phone1_sms);
			} else {
				ToastUtils.showToast(this, "手机号格式不对", 0);
			}
			break;
		case R.id.dt_phone2_imsms:
			if (phone2 != null && !phone2.equals("") && DxqUtils.isMobileNO(phone2)) {
				Uri smsToUri2 = Uri.parse("smsto:" + phone2);
				Intent phone2_sms = new Intent(Intent.ACTION_SENDTO, smsToUri2);
				startActivity(phone2_sms);
			} else {
				ToastUtils.showToast(this, "手机号格式不对", 0);
			}
			break;
		case R.id.dt_title_imcopy:
			startProgressDialog();
			copyOnePhone();
			break;
		default:
			break;
		}
	}

	private void creatSaveDb() {
		x.view().inject(this);
		db = DxqUtils.openDb("recent");
	}

	private String getCallTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	@Override
	public void onSavePhoneListener(int code) {
		stopProgressDialog();
		if (code == 0) {
			ToastUtils.showToast(DetailsActivity.this, "保存成功", 0);
		}
		if (code == 1) {
			ToastUtils.showToast(DetailsActivity.this, "保存失败", 0);
		}
	}

	// show（）一个ProgressBar
	public void startProgressDialog() {
		if (progressDialog == null) {
			progressDialog = MyProgessBar.createLoadingDialog(DetailsActivity.this, null);
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

	private void copyOnePhone() {
		new Thread(new Runnable() {
			public void run() {
				SaveOnePhoneCode.CopyOnePhone(DetailsActivity.this, listener, tel, title, phone1, phone2, email);
			}
		}).start();
	}
}
