package com.daxiangqun.contacts.activity;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.contacts.db.Section;
import com.daxiangqun.contacts.library.RefreshLayout;
import com.daxiangqun.contacts.library.RoundedImageView;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PersonnelActivity extends Activity {

	private RefreshLayout mRefreshLayout;
	private TextView tv_title;
	private String title = null;
	private int code;
	private String officeID;
	private String site;
	private String postcode;
	private String fax;
	private String email;
	private String url;
	private String tel;
	private ExpandableListView expandableListView;
	private MyAdapter adapter;
	private LinearLayout ly_back;
	private List<Section> sectionList;
	private List<PersonContacts> contactsList;
	private List<Section> arrayList;

	// ----
	private SearchBox search;
	private ImageView search_im;
	private List<PersonContacts> personList;
	private DbManager db;
	private boolean isOpenSearch = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personnel);
		init();
	}

	private void init() {
		expandableListView = (ExpandableListView) findViewById(R.id.ps_lv);
		expandableListView.setGroupIndicator(null); // 去掉默认带的箭头
		mRefreshLayout = (RefreshLayout) findViewById(R.id.ps_swipe);
		ly_back = (LinearLayout) findViewById(R.id.ps_back_ly);
		ly_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv_title = (TextView) findViewById(R.id.ps_title);
		getIntentParam();
		String dbName = "section";// 部门
		if (title != null) {
			tv_title.setText(title);
		}
		sectionList = new ArrayList<Section>();
		contactsList = new ArrayList<PersonContacts>();
		arrayList = new ArrayList<Section>();

		getDbDatas(dbName, code);// 打开部门数据库

		// --------------------------------------
		setLisener();
		// ---------------------------------------
		setHeadView();
		// ---------------------
		search = (SearchBox) findViewById(R.id.ps_searchbox);
		search.enableVoiceRecognition(this);

		search_im = (ImageView) findViewById(R.id.ps_im_searchbox);
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
						ToastUtils.showToast(PersonnelActivity.this, "没有此联系人", 0);
					}
				} else {
					openSearch();
					isOpenSearch = true;
				}
			}
		});
		personList = new ArrayList<PersonContacts>();

	}

	private void getIntentParam() {
		title = getIntent().getStringExtra("title");
		code = getIntent().getIntExtra("code", code);
		tel = getIntent().getStringExtra("tel");
		officeID = getIntent().getStringExtra("officeID");
		site = getIntent().getStringExtra("site");
		postcode = getIntent().getStringExtra("postcode");
		fax = getIntent().getStringExtra("fax");
		email = getIntent().getStringExtra("email");
		url = getIntent().getStringExtra("url");
		DxqUtils.FlLog("PersonnelActivity：" + officeID + title);
	}

	private void setHeadView() {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View headview = inflater.inflate(R.layout.ps_head_item, null);
		TextView tvhead_title = (TextView) headview.findViewById(R.id.ps_head_tvtitle);
		TextView tvhead_tel = (TextView) headview.findViewById(R.id.ps_head_tvtel);
		TextView tvhead_address = (TextView) headview.findViewById(R.id.ps_head_tvaddress);
		TextView tvhead_id = (TextView) headview.findViewById(R.id.ps_head_tvid);
		TextView tvhead_fax = (TextView) headview.findViewById(R.id.ps_head_tvfax);
		TextView tvhead_email = (TextView) headview.findViewById(R.id.ps_head_tvemail);
		TextView tvhead_url = (TextView) headview.findViewById(R.id.ps_head_tvurl);
		LinearLayout lyhead_tel = (LinearLayout) headview.findViewById(R.id.ps_head_lytel);
		LinearLayout lyhead_address = (LinearLayout) headview.findViewById(R.id.ps_head_lyaddress);
		LinearLayout lyhead_id = (LinearLayout) headview.findViewById(R.id.ps_head_lyid);
		LinearLayout lyhead_fax = (LinearLayout) headview.findViewById(R.id.ps_head_lyfax);
		LinearLayout lyhead_email = (LinearLayout) headview.findViewById(R.id.ps_head_lyemail);
		LinearLayout lyhead_url = (LinearLayout) headview.findViewById(R.id.ps_head_lyurl);
		tvhead_title.setText(title);
		setHeadViewParame(tvhead_tel, lyhead_tel, tel);
		setHeadViewParame(tvhead_address, lyhead_address, site);
		setHeadViewParame(tvhead_id, lyhead_id, postcode);
		setHeadViewParame(tvhead_fax, lyhead_fax, fax);
		setHeadViewParame(tvhead_email, lyhead_email, email);
		setHeadViewParame(tvhead_url, lyhead_url, url);
		expandableListView.addHeaderView(headview);
	}

	private void setHeadViewParame(TextView tvhead, LinearLayout lyhead, String param) {
		if (!TextUtils.isEmpty(param)&&!param.equals("null")) {
			tvhead.setText(param);
		} else {
			lyhead.setVisibility(View.GONE);
		}
	}

	private void setLisener() {
		mRefreshLayout.setChildView(expandableListView);
		mRefreshLayout.setColorSchemeResources(R.color.blue);
		// 使用SwipeRefreshLayout的下拉刷新监听
		// use SwipeRefreshLayout OnRefreshListener
		mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				mRefreshLayout.setRefreshing(false);
			}
		});
		adapter = new MyAdapter(this);
		adapter.addlist(arrayList);
		expandableListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();

		for (int i = 0; i < adapter.getGroupCount(); i++) {

			expandableListView.expandGroup(i);

		}
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View clickedView, int groupPosition, long groupId) {
				return true;
			}
		});
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView expandablelistview, View clickedView, int groupPosition,
					int childPosition, long childId) {
				Intent it = new Intent(PersonnelActivity.this, DetailsActivity.class);
				it.putExtra("id", arrayList.get(groupPosition).getPsList().get(childPosition).getId());// ID
				it.putExtra("title", arrayList.get(groupPosition).getPsList().get(childPosition).getTitle());// 名称
				it.putExtra("attachpath", arrayList.get(groupPosition).getPsList().get(childPosition).getAttachpath());// 头像路径
				it.putExtra("duty", arrayList.get(groupPosition).getPsList().get(childPosition).getDuty());// 职务
				it.putExtra("tel", arrayList.get(groupPosition).getPsList().get(childPosition).getTel());// 电话
				it.putExtra("fax", arrayList.get(groupPosition).getPsList().get(childPosition).getFax());// 传真
				it.putExtra("phone1", arrayList.get(groupPosition).getPsList().get(childPosition).getPhone1());// 电话1
				it.putExtra("phone2", arrayList.get(groupPosition).getPsList().get(childPosition).getPhone2());// 电话2
				it.putExtra("email", arrayList.get(groupPosition).getPsList().get(childPosition).getEmail());// 邮箱
				it.putExtra("url", arrayList.get(groupPosition).getPsList().get(childPosition).getUrl());// 网址
				it.putExtra("site", arrayList.get(groupPosition).getPsList().get(childPosition).getSite());// 地址
				startActivity(it);
				return true;// 返回true表示此事件在此被处理了
			}
		});
	}

	private void getDbDatas(String dbName, int code) {
		DbManager db = DxqUtils.openDb(dbName);
		try {
			sectionList = db.selector(Section.class).where("typeID", "=", code).and("officeID", "=", officeID)
					.orderBy("id").findAll();
			if (sectionList.size() > 0) {
				if (!sectionList.get(0).getTitle().equals("领导办公室")) {
					// 本地排序，办公室领导人永远拍第一位
					Section lingdao = null;
					for (int i = 0; i < sectionList.size(); i++) {
						if (sectionList.get(i).getTitle().equals("领导办公室")) {
							lingdao = sectionList.remove(i);
							break;
						}
					}
					if (lingdao != null)
						sectionList.add(0, lingdao);
				}
				setContactsDatas(code, "personnel");
				//setHeadView();
			} else {
				ToastUtils.showToast(PersonnelActivity.this, "暂无人员", 0);
			}
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	private void setContactsDatas(int code, String dbName) {
		DbManager db = DxqUtils.openDb(dbName);
		// 生成部门List数据源
		for (int i = 0; i < sectionList.size(); i++) {
			try {
				Section section = new Section();
				section.setId(sectionList.get(i).getId());
				section.setTitle(sectionList.get(i).getTitle());
//				contactsList = db.selector(PersonContacts.class).where("typeID", "=", code)// 类别ID
//						.and("officeID", "=", officeID)// 旅游局ID
//						.and("sectionID", "=", sectionList.get(i).getId())// 部门ID
//						.orderBy("id").findAll();
				contactsList = db.selector(PersonContacts.class)
						.where("officeID", "=", officeID)// 旅游局ID
						.and("sectionID", "=", sectionList.get(i).getId())// 部门ID
						.orderBy("id").findAll();
				section.setPsList(contactsList);
				arrayList.add(section);
			} catch (DbException e) {
				e.printStackTrace();
			}
		}
		Log.i("PersonnelActivity", "code="+code+"officeID="+officeID+"sectionID="+sectionList.get(0).getId());
		Log.i("PersonnelActivity", arrayList+"");
	}

	public class ViewHolder {
		public TextView tv_title, tv_name, tv_duty;
		public RoundedImageView im;
	}

	class MyAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater = null;
		private List<Section> list;
		private DisplayImageOptions options;

		public MyAdapter(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = new ArrayList<Section>();
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.icon)
					.showImageForEmptyUri(R.drawable.icon).showImageOnFail(R.drawable.icon).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
					.build();
		}

		// 数据加载
		private void addlist(List<Section> sectionlist) {
			this.list = sectionlist;
		}

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			if (list.get(groupPosition).getPsList() == null) {
				return 0;
			} else {
				return list.get(groupPosition).getPsList().size();// 对应
			}
		}

		@Override
		public Object getGroup(int groupPosition) {
			// TODO Auto-generated method stub
			return list.get(groupPosition);
		}

		@Override
		public PersonContacts getChild(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return list.get(groupPosition).getPsList().get(childPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			// TODO Auto-generated method stub
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			// TODO Auto-generated method stub
			return childPosition;
		}

		@Override
		public boolean hasStableIds() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.ps_group_item, parent, false);
				holder.tv_title = (TextView) convertView.findViewById(R.id.ps_group_title);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_title.setText(list.get(groupPosition).getTitle());// 部门名称
			convertView.setBackgroundResource(R.drawable.list_item_bg);
			if (list.get(groupPosition).getTitle().equals("领导办公室")) {
				LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) holder.tv_title.getLayoutParams(); // 取控件View当前的布局参数
				linearParams.height = 0;// 控件的高强制设成0
				holder.tv_title.setLayoutParams(linearParams); // 使设置好的布局参数应用到控件
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			PersonContacts entity = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.ps_child_item, parent, false);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tvname_ps_item);
				holder.tv_duty = (TextView) convertView.findViewById(R.id.tvduty_ps_item);
				holder.im = (RoundedImageView) convertView.findViewById(R.id.im_ps_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name.setText(entity.getTitle());
			holder.tv_duty.setText(entity.getDuty());
			ImageLoader.getInstance().displayImage(entity.getUrl(), holder.im, options, null);
			convertView.setBackgroundResource(R.drawable.listcall_item_bg);
			return convertView;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	// ------------------搜索 Start-----------------------
	public void openSearch() {
		// --------------------------
		db = DxqUtils.openDb("personnel");
		search.revealFromMenuItem(R.id.ps_im_searchbox, this);
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
					personList = db.selector(PersonContacts.class).where("title", "LIKE", term + "%").findAll();
				} catch (DbException e) {
					e.printStackTrace();
				}
				ArrayList<SearchResult> searchResults = new ArrayList<SearchResult>();
				for (int x = 0; x < personList.size(); x++) {
					SearchResult option = new SearchResult(
							personList.get(x).getTitle() + "-" + personList.get(x).getDuty(),
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
					String result = msg.getData().getString("result");
					String[] strResult = result.split("-");
					if (strResult.length == 1) {
						personList = db.selector(PersonContacts.class).where("title", "==", strResult[0]).findAll();
					}
					if (strResult.length == 2) {
						personList = db.selector(PersonContacts.class).where("title", "==", strResult[0])
								.and("duty", "=", strResult[1]).findAll();
					}
				} catch (DbException e) {
					e.printStackTrace();
				}
				if (personList != null && personList.size() > 0) {
					isOpenSearch = false;
					Intent it = new Intent(PersonnelActivity.this, DetailsActivity.class);
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
				break;

			default:
				break;
			}
		};
	};
}
