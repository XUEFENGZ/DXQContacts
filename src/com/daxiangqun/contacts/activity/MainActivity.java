package com.daxiangqun.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.fragment.FragmentTabAdapter;
import com.daxiangqun.fragment.HomePageFragment;
import com.daxiangqun.fragment.RecentCallsFragment;
import com.daxiangqun.fragment.SetFragment;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.ToastUtils;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity {

	private RadioGroup rgs;
	public List<Fragment> fragments = new ArrayList<Fragment>();
	private RadioButton rb1;
	private SearchBox search;
	private ImageView search_im;
	private TextView tv_title;
	public static boolean isBebug = true;
	private List<PersonContacts> list = null;
	private List<PersonContacts> personList;
	private boolean isOpenSearch = false;
	private DbManager db;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv_title = (TextView) findViewById(R.id.tv_title);
		fragments.add(new HomePageFragment());
		fragments.add(new Fragment());
		fragments.add(new RecentCallsFragment());
		fragments.add(new Fragment());
		fragments.add(new SetFragment());
		rgs = (RadioGroup) this.findViewById(R.id.main_radio);
		rb1 = (RadioButton) findViewById(R.id.radiobutton1);
		rb1.setFocusable(true);
		FragmentTabAdapter tabAdapter = new FragmentTabAdapter(this, fragments, R.id.main_content, rgs, tv_title);
		tabAdapter.showTab(0);

		search = (SearchBox) findViewById(R.id.searchbox);
		search.enableVoiceRecognition(this);

		search_im = (ImageView) findViewById(R.id.im_searchbox);
		search_im.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if(isOpenSearch==true){
					String str = search.getSearchText();
					if(!TextUtils.isEmpty(str)){
						Message msg = new Message();
						msg.what = 0;
						Bundle bundle = new Bundle();
						bundle.putString("result", str);
						msg.setData(bundle);// mes利用Bundle传递数据
						handler.sendMessage(msg);// 用activity中的handler发送消
					}else {
						ToastUtils.showToast(MainActivity.this, "没有此联系人", 0);
					}
				}else {
					openSearch();
					isOpenSearch = true;
				}
			}
		});
		list = new ArrayList<PersonContacts>();
	}

	// ----------------------------搜索-------------------------------
	public void openSearch() {

		db = DxqUtils.openDb("personnel");
		// --------------------------
		search.revealFromMenuItem(R.id.im_searchbox, this);
		search.setMenuListener(new SearchBox.MenuListener() {

			@Override
			public void onMenuClick() {

			}

		});
		search.setSearchListener(new SearchBox.SearchListener() {

			@Override
			public void onSearchOpened() {

			}

			@Override
			public void onSearchClosed() {
				closeSearch();
				isOpenSearch = false;
				search.clearResults();
			}

			@Override
			public void onSearchTermChanged(String term) {
				try {
					list = db.selector(PersonContacts.class).where("title", "LIKE", term + "%").findAll();
				} catch (DbException e) {
					e.printStackTrace();
				}

				ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
				for (int x = 0; x < list.size(); x++) {
					SearchResult option = new SearchResult(
							list.get(x).getTitle() + "-" + list.get(x).getOfficename() + "-" + list.get(x).getDuty(),
							getResources().getDrawable(R.drawable.ic_history));
					// search.addSearchable(option);
					searchResults.add(option);
				}
				search.setSearchables(searchResults);
			}

			@Override
			public void onSearch(String searchTerm) {

			}

			@Override
			public void onResultClick(SearchResult result) {
				Message msg = new Message();
				msg.what = 0;
				Bundle bundle = new Bundle();
				bundle.putString("result", result.title);
				msg.setData(bundle);// mes利用Bundle传递数据
				handler.sendMessage(msg);// 用activity中的handler发送消

			}

			@Override
			public void onSearchCleared() {

			}

		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SearchBox.VOICE_RECOGNITION_CODE && resultCode == RESULT_OK) {
			ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
			search.populateEditText(matches.get(0));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	protected void closeSearch() {
		search.hideCircularly(this);
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				try {
					personList = new ArrayList<PersonContacts>();
					String result = msg.getData().getString("result");
					String[] strResult = result.split("-");
					if (strResult.length == 1) {
						personList = db.selector(PersonContacts.class).where("title", "==", strResult[0]).findAll();
					}
					if (strResult.length == 2) {
						personList = db.selector(PersonContacts.class).where("title", "==", strResult[0])
								.and("officename", "=", strResult[1]).or("duty", "=", strResult[1]).findAll();
					}
					if (strResult.length == 3) {
						personList = db.selector(PersonContacts.class).where("title", "==", strResult[0])
								.and("officename", "=", strResult[1]).and("duty", "=", strResult[2]).findAll();
					}
					if (personList != null && personList.size() > 0) {
						isOpenSearch = false;
						Intent it = new Intent(MainActivity.this, DetailsActivity.class);
						it.putExtra("id", personList.get(0).getId());// ID
						it.putExtra("title", personList.get(0).getTitle());// 名称
						it.putExtra("attachpath", personList.get(0).getAttachpath());// 头像路径
						it.putExtra("duty", personList.get(0).getDuty());// 职务
						it.putExtra("tel", personList.get(0).getTel());// 电话
						it.putExtra("fax", personList.get(0).getFax());// 传真
						it.putExtra("phone1", personList.get(0).getPhone1());// 电话1
						it.putExtra("phone2", personList.get(0).getPhone2());// 电话2
						it.putExtra("email", personList.get(0).getEmail());// 邮箱
						it.putExtra("url", personList.get(0).getUrl());// 网址
						it.putExtra("site", personList.get(0).getSite());// 地址
						startActivity(it);
					}

				} catch (DbException e) {
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		};
	};

	// -------------------------end--------------------------------
	// --------------按两次返回键退出程序------------------------------------------
	// 定义一个变量，来标识是否退出
	private static boolean isExit = false;

	static Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		DxqUtils.FlLog("Android返回键");
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			exit();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private void exit() {
		if (!isExit) {
			isExit = true;
			Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			// 利用handler延迟发送更改状态信息
			mHandler.sendEmptyMessageDelayed(0, 2000);
		} else {
			finish();
			System.exit(0);
		}
	}

}
