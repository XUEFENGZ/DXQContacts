package com.daxiangqun.fragment;

import java.util.ArrayList;
import java.util.List;

import org.xutils.DbManager;
import org.xutils.x;
import org.xutils.db.sqlite.WhereBuilder;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.R;
import com.daxiangqun.contacts.db.Recent;
import com.daxiangqun.contacts.library.RefreshLayout;
import com.daxiangqun.contacts.library.RoundedImageView;
import com.daxiangqun.utils.DxqUtils;
import com.daxiangqun.utils.ToastUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.TextView;

public class RecentCallsFragment extends BaseFragment {
	private DbManager db = null;
	private List<Recent> recentList = null;
	private ExpandableListView mlistview;
	private RefreshLayout mRefreshLayout;
	private MyAdapter adapter;
	private View view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_recent_call, container, false);
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		creatSaveDb(view);// 打开数据库
//		if (recentList == null) {
//			ToastUtils.showToast(getActivity(), "无通话历史", 0);
//		}
		init(view);
	}

	private void init(View view) {
		mlistview = (ExpandableListView) view.findViewById(R.id.call_lv);
		mlistview.setGroupIndicator(null); // 去掉默认带的箭头
		mRefreshLayout = (RefreshLayout) view.findViewById(R.id.call_swipe);
		mRefreshLayout.setChildView(mlistview);
		mRefreshLayout.setColorSchemeResources(R.color.blue);
		// 使用SwipeRefreshLayout的下拉刷新监听
		// use SwipeRefreshLayout OnRefreshListener
		mRefreshLayout.setOnRefreshListener(new RefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				if (recentList==null) {
					ToastUtils.showToast(getActivity(), "没有通话记录", 0);
					mRefreshLayout.setRefreshing(false);
					return;
				}
				adapter.addlist(recentList);// 重新加載
				mlistview.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				mRefreshLayout.setRefreshing(false);
			}
		});
		// -----------------
		if(recentList!=null){
			adapter = new MyAdapter(getActivity());
			adapter.addlist(recentList);
			mlistview.setAdapter(adapter);
			adapter.notifyDataSetChanged();
			mlistview.setOnGroupExpandListener(new OnGroupExpandListener() {
				@Override
				public void onGroupExpand(int groupPosition) {
					for (int i = 0, count = mlistview.getExpandableListAdapter().getGroupCount(); i < count; i++) {
						if (groupPosition != i) {// 关闭其他分组
							mlistview.collapseGroup(i);
						}
					}
				}
			});
		}
	}

	@Override
	public View initView(LayoutInflater inflater) {
		return null;
	}

	@Override
	public void initData(Bundle savedInstanceState) {

	}

	private void creatSaveDb(View view) {
		x.view().inject(this, view);
		db = DxqUtils.openDb("recent");
		try {
			recentList = new ArrayList<Recent>();
			recentList = db.selector(Recent.class).orderBy("calltime", true).findAll();

			if (recentList != null && recentList.size() > 50) {
				// 删除超过50条的数据库数据-自动删除10条
				db.delete(Recent.class, WhereBuilder.b("calltime", "<", recentList.get(50).getCalltime()));
				recentList = db.selector(Recent.class).findAll();// 重新查询得到数据
			}

		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	// --------------------------------
	public class ViewHolder {
		public TextView tv_phone, tv_name, tv_duty;
		public RoundedImageView im;
		private ImageView im_call, im_sms, im_save;
	}

	class MyAdapter extends BaseExpandableListAdapter {
		private LayoutInflater inflater = null;
		private List<Recent> list;
		private DisplayImageOptions options;

		public MyAdapter(Context context) {
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			list = new ArrayList<Recent>();
			options = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.icon)
					.showImageForEmptyUri(R.drawable.icon).showImageOnFail(R.drawable.icon).cacheInMemory(true)
					.cacheOnDisk(true).considerExifParams(true).displayer(new CircleBitmapDisplayer(Color.WHITE, 5))
					.build();
		}

		// 数据加载
		private void addlist(List<Recent> recentlist) {
			this.list = recentlist;
		}

		@Override
		public int getGroupCount() {
			return list.size();
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			return 1;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return list.get(groupPosition);
		}

		@Override
		public Recent getChild(int groupPosition, int childPosition) {
			return list.get(groupPosition);
		}

		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return groupPosition;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.call_group_item, parent, false);
				holder.im = (RoundedImageView) convertView.findViewById(R.id.im_call_item);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tvname_call_item);
				holder.tv_duty = (TextView) convertView.findViewById(R.id.tvduty_call_item);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_name.setText(list.get(groupPosition).getTitle());
			holder.tv_duty.setText(list.get(groupPosition).getDuty());
			ImageLoader.getInstance().displayImage(list.get(groupPosition).getAttachpath(), holder.im, options, null);
			convertView.setBackgroundResource(R.drawable.listcall_item_bg);
			return convertView;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
				ViewGroup parent) {
			ViewHolder holder = null;
			// Recent entity = getChild(groupPosition, childPosition);
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.call_child_item, parent, false);
				holder.tv_phone = (TextView) convertView.findViewById(R.id.tv_call_child);
				holder.im_call = (ImageView) convertView.findViewById(R.id.im_call_child);
				holder.im_sms = (ImageView) convertView.findViewById(R.id.imsms_call_child);
				holder.im_save = (ImageView) convertView.findViewById(R.id.imsave_call_child);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_phone.setText(list.get(groupPosition).getTel());
			childButtonLinener(groupPosition, holder);
			return convertView;
		}

		private void childButtonLinener(final int groupPosition, ViewHolder holder) {
			final String tel = list.get(groupPosition).getTel();
			holder.im_call.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (tel != null && !tel.equals("")) {
						Intent it_tel = new Intent();
						it_tel.setAction(Intent.ACTION_CALL);
						it_tel.setData(Uri.parse("tel:" + tel));// 指定电话号码
						startActivity(it_tel);
					} else {
						ToastUtils.showToast(getActivity(), "手机号格式不对", 0);
					}
				}
			});
			holder.im_sms.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (tel != null && !tel.equals("") && DxqUtils.isMobileNO(tel)) {
						Uri smsToUri = Uri.parse("smsto:" + tel);
						Intent phone1_sms = new Intent(Intent.ACTION_SENDTO, smsToUri);
						startActivity(phone1_sms);
					} else {
						ToastUtils.showToast(getActivity(), "手机号格式不对", 0);
					}
				}
			});
			holder.im_save.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (tel != null && !tel.equals("") && DxqUtils.isMobileNO(tel)) {
						Intent intent = new Intent(Intent.ACTION_INSERT_OR_EDIT);
						intent.setType("vnd.android.cursor.item/person");
						intent.setType("vnd.android.cursor.item/contact");
						intent.setType("vnd.android.cursor.item/raw_contact");
						intent.putExtra(android.provider.ContactsContract.Intents.Insert.NAME,
								list.get(groupPosition).getTitle());
						intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE, tel);
						intent.putExtra(android.provider.ContactsContract.Intents.Insert.PHONE_TYPE, 3);
						startActivity(intent);
					} else {
						ToastUtils.showToast(getActivity(), "手机号格式不对", 0);
					}
				}
			});
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

}
