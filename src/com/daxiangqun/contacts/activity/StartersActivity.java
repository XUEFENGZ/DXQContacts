package com.daxiangqun.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.Office;
import com.daxiangqun.contacts.library.RefreshLayout;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.ToastUtils;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class StartersActivity extends Activity {
	private RefreshLayout mRefreshLayout;
	private TextView tv_title;
	private String title = null;
	private int code;
	private ListView mListView;
	private List<Office> officeList = null;
	private MyAdapter adapter;
	private LinearLayout ly_back;
	private SearchBox search;
	private ImageView search_im;
	private DbManager db;
	private List<Office> dbofList = null;
	private boolean isOpenSearch = false;// 默认关闭

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_starters);
		init();
	}

	private void init() {
		mListView = (ListView) findViewById(R.id.starters_lv);
		mRefreshLayout = (RefreshLayout) findViewById(R.id.starters_swipe);
		ly_back = (LinearLayout) findViewById(R.id.starters_back_ly);
		ly_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.starters_title);
		title = getIntent().getStringExtra("title");
		code = getIntent().getIntExtra("code", code);
		String dbName = getIntent().getStringExtra("dbName");
		if (title != null) {
			tv_title.setText(title);
		}
		officeList = new ArrayList<Office>();
		getDbDatas(dbName, code);// 打开数据库
		// --------------------------------------
		mRefreshLayout.setChildView(mListView);
		mRefreshLayout.setColorSchemeResources(R.color.blue);
		// 使用SwipeRefreshLayout的下拉刷新监听
		// use SwipeRefreshLayout OnRefreshListener
		mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (officeList.size() == 0) {
					ToastUtils.showToast(StartersActivity.this, "暂无数据", 0);
					mRefreshLayout.setRefreshing(false);
					return;
				}
				adapter.addData(officeList);// 重新加載
				mListView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mRefreshLayout.setRefreshing(false);
			}
		});
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*if(code==2){
					Intent it = new Intent(StartersActivity.this, DetailsActivity.class);
					it.putExtra("id", officeList.get(position).getId());// ID
					it.putExtra("title", officeList.get(position).getTitle());// 名称
					it.putExtra("attachpath", "");// 头像路径
					it.putExtra("duty", "");// 职务
					it.putExtra("tel", officeList.get(position).getTel());// 电话
					it.putExtra("fax", officeList.get(position).getFax());// 传真
					it.putExtra("phone1", "");// 电话1
					it.putExtra("phone2", "");// 电话2
					it.putExtra("email", officeList.get(position).getEmail());// 邮箱
					it.putExtra("url", officeList.get(position).getUrl());// 网址
					it.putExtra("site", officeList.get(position).getSite());// 地址
					startActivity(it);
				}else {*/
					Intent it = new Intent(StartersActivity.this, PersonnelActivity.class);
					it.putExtra("code", code);
					it.putExtra("officeID", officeList.get(position).getId());// 旅游局ID
					it.putExtra("title", officeList.get(position).getTitle());// 旅游局名字
					it.putExtra("tel", officeList.get(position).getTel());// 电话
					it.putExtra("site", officeList.get(position).getSite());// 地址
					it.putExtra("postcode", officeList.get(position).getPostcode());// 邮编
					it.putExtra("fax", officeList.get(position).getFax());// 传真
					it.putExtra("email", officeList.get(position).getEmail());// 邮箱
					it.putExtra("url", officeList.get(position).getUrl());// 网址
					
					startActivity(it);
//				}
			}
		});
		// --------------------搜索-------------
		search = (SearchBox) findViewById(R.id.starters_searchbox);
		search.enableVoiceRecognition(this);

		search_im = (ImageView) findViewById(R.id.starters_im_searchbox);
		search_im.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isOpenSearch == true) {
					String str = search.getSearchText();
					if (!TextUtils.isEmpty(str)) {
						Message msg = new Message();
						msg.what = 0;
						Bundle bundle = new Bundle();
						bundle.putString("result", str);
						msg.setData(bundle);// mes利用Bundle传递数据
						handler.sendMessage(msg);// 用activity中的handler发送消
					} else {
						ToastUtils.showToast(StartersActivity.this, "没有此联系人", 0);
					}
				} else {
					openSearch();
					isOpenSearch = true;
				}
			}
		});
		dbofList = new ArrayList<Office>();
	}

	private void getDbDatas(String dbName, int code) {
		db = DxqUtils.openDb(dbName);
		try {
			officeList = db.selector(Office.class).where("typeID", "=", code).orderBy("id").findAll();
			DxqUtils.FlLog("officeList=" + officeList.size());
			if (officeList.size() == 0) {
				ToastUtils.showToast(this, "暂无数据", 0);
				return;
			}
			adapter = new MyAdapter(this);
			adapter.addData(officeList);
			mListView.setAdapter(adapter);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	// 适配器
	public class ViewHolder {
		// private ImageView im1;
		private TextView tv1, tv2, tv3, tv_starters_ly;
	}

	class MyAdapter extends BaseAdapter {

		private Context context;
		private LayoutInflater inflater;
		private List<Office> list;
		// private DisplayImageOptions options;

		public MyAdapter(Context context) {
			this.context = context;
			inflater = LayoutInflater.from(context);
			list = new ArrayList<Office>();
			// options = new DisplayImageOptions.Builder()
			// .showImageOnLoading(R.drawable.ic_launcher)
			// .showImageForEmptyUri(R.drawable.ic_launcher)
			// .showImageOnFail(R.drawable.ic_launcher)
			// .cacheInMemory(true)
			// .cacheOnDisk(true)
			// .considerExifParams(true)
			// .displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
			// .build();

		}

		// 把数据添加到适配器中
		public void addData(List<Office> datas) {
			this.list = datas;
		}

		// 把分页的数据绑定到适配器中
		public void bindData(List<Office> datas) {
			if (list != null) // 如果数据不为null那么可以进行分页加载
				list.addAll(datas);
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.starters_items, parent, false);
				// holder.im1 = (ImageView) convertView
				// .findViewById(R.id.im_starters_item);// 图片显示
				holder.tv1 = (TextView) convertView.findViewById(R.id.tv1_starters_item);// 标题
				holder.tv2 = (TextView) convertView.findViewById(R.id.tv2_starters_item);
				holder.tv3 = (TextView) convertView.findViewById(R.id.tv3_starters_item);
				holder.tv_starters_ly = (TextView) convertView.findViewById(R.id.tv_starters_ly);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv1.setText(list.get(position).getTitle());
			holder.tv2.setText(list.get(position).getSite());
			if (code == 2) {
				// holder.tv_starters_ly.setText("");
				// holder.tv3.setText("");
				holder.tv_starters_ly.setVisibility(View.GONE);
				holder.tv3.setVisibility(View.GONE);
			} else {
				holder.tv3.setText(list.get(position).getPostcode());
			}
			// ImageLoader.getInstance().displayImage(list.get(position).getUrl(),
			// holder.im1, options, null);
			convertView.setBackgroundResource(R.drawable.listcall_item_bg);
			return convertView;
		}
	}

	// ----------------------------搜索-------------------------------
	public void openSearch() {
		// --------------------------
		search.revealFromMenuItem(R.id.starters_im_searchbox, this);
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
				// search_im.setVisibility(View.VISIBLE);
			}

			@Override
			public void onSearchTermChanged(String term) {

				try {
					dbofList = db.selector(Office.class).where("title", "LIKE", term + "%").findAll();
				} catch (DbException e) {
					e.printStackTrace();
				}
				ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
				for (int x = 0; x < dbofList.size(); x++) {
					SearchResult option = new SearchResult(dbofList.get(x).getTitle(),
							getResources().getDrawable(R.drawable.ic_history));
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
					dbofList = db.selector(Office.class).where("title", "==", msg.getData().getString("result"))
							.findAll();
				} catch (DbException e) {
					e.printStackTrace();
				}
				if (dbofList != null && dbofList.size() > 0) {
					isOpenSearch = false;
					Intent it = new Intent(StartersActivity.this, PersonnelActivity.class);
					it.putExtra("code", code);
					it.putExtra("officeID", dbofList.get(0).getId());// 旅游局ID
					it.putExtra("site", dbofList.get(0).getSite());// 地址
					it.putExtra("title", dbofList.get(0).getTitle());// 旅游局名字
					it.putExtra("postcode", dbofList.get(0).getPostcode());// 邮编
					startActivity(it);
				}
				break;

			default:
				break;
			}
		};
	};
}
