package com.daxiangqun.contacts.jsonstring;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.DbManager;
import org.xutils.ex.DbException;

import com.daxiangqun.contacts.db.City;
import com.daxiangqun.contacts.db.Office;
import com.daxiangqun.contacts.db.PersonContacts;
import com.daxiangqun.contacts.db.Province;
import com.daxiangqun.contacts.db.Section;
import com.daxiangqun.fragment.HomePageFragment;
import com.daxiangqun.listener.DxqHttpListener;

/**
 * 
 * @author xuefengz 20151209 生成5张数据库表
 *
 */
public class doHttpRequestDb {
	public static void doPersonnelDb(JSONArray jsonArray, final DbManager db, DxqHttpListener onDxqHttpListener) {
		final List<PersonContacts> list = new ArrayList<PersonContacts>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = (JSONObject) jsonArray.optJSONObject(i);
			PersonContacts dContacts = new PersonContacts();
			try {
				dContacts.setId(json.getString("id"));
				dContacts.setAttachment(json.getString("attachment"));
				dContacts.setAttachpath(json.getString("attachpath"));
				dContacts.setCityID(json.getString("cityID"));
				dContacts.setDuty(json.getString("duty"));
				dContacts.setEmail(json.getString("email"));
				dContacts.setFax(json.getString("fax"));
				dContacts.setJurisdiction(json.getString("jurisdiction"));
				dContacts.setLanguage(json.getString("language"));
				dContacts.setOfficeID(json.getString("officeID"));
				dContacts.setOrderID(json.getString("orderID"));
				dContacts.setPhone1(json.getString("phone1"));
				dContacts.setPhone2(json.getString("phone2"));
				dContacts.setProvinceID(json.getString("provinceID"));
				dContacts.setSectionID(json.getString("sectionID"));
				dContacts.setSite(json.getString("site"));
				dContacts.setStatus(json.getString("status"));
				dContacts.setTel(json.getString("tel"));
				dContacts.setTitle(json.getString("title"));
				dContacts.setTypeID(json.getString("typeID"));
				dContacts.setUrl(json.getString("url"));
				dContacts.setWritetime(json.getString("writetime"));
				dContacts.setOfficename(json.getString("officename"));
				list.add(dContacts);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					db.saveOrUpdate(list);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 通知下一个接口操作
		onDxqHttpListener.onCompleted(HomePageFragment.THREAD_TWO);
	}

	public static void doSectionDb(JSONArray jsonArray, final DbManager db, DxqHttpListener onDxqHttpListener) {
		final List<Section> list = new ArrayList<Section>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = (JSONObject) jsonArray.optJSONObject(i);
			Section dSection = new Section();
			try {
				dSection.setId(json.getString("id"));
				dSection.setTitle(json.getString("title"));
				dSection.setProvinceID(json.getString("provinceID"));
				dSection.setCityID(json.getString("cityID"));
				dSection.setTypeID(json.getString("typeID"));
				dSection.setOfficeID(json.getString("officeID"));
				dSection.setStatus(json.getString("status"));
				dSection.setWritetime(json.getString("writetime"));
				dSection.setLanguage(json.getString("language"));
				list.add(dSection);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					db.saveOrUpdate(list);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 通知下一个接口操作
		onDxqHttpListener.onCompleted(HomePageFragment.THREAD_THREE);
	}

	public static void doOfficeDb(JSONArray jsonArray, final DbManager db, DxqHttpListener onDxqHttpListener) {
		final List<Office> list = new ArrayList<Office>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = (JSONObject) jsonArray.optJSONObject(i);
			Office dOffice = new Office();
			try {
				dOffice.setId(json.getString("id"));
				dOffice.setTitle(json.getString("title"));
				dOffice.setProvinceID(json.getString("provinceID"));
				dOffice.setCityID(json.getString("cityID"));
				dOffice.setTypeID(json.getString("typeID"));
				dOffice.setSite(json.getString("site"));
				dOffice.setPostcode(json.getString("postcode"));
				dOffice.setUrl(json.getString("url"));
				dOffice.setStatus(json.getString("status"));
				dOffice.setWritetime(json.getString("writetime"));
				dOffice.setLanguage(json.getString("language"));

				dOffice.setTel(json.getString("tel"));
				dOffice.setTel(json.getString("fax"));
				dOffice.setTel(json.getString("email"));
				list.add(dOffice);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					db.saveOrUpdate(list);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 通知下一个接口操作
		onDxqHttpListener.onCompleted(HomePageFragment.THREAD_FOUR);

	}

	public static void doCityDb(JSONArray jsonArray, final DbManager db, DxqHttpListener onDxqHttpListener) {
		final List<City> list = new ArrayList<City>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = (JSONObject) jsonArray.optJSONObject(i);
			City dCity = new City();
			try {
				dCity.setId(json.getString("id"));
				dCity.setTitle(json.getString("title"));
				dCity.setProvinceID(json.getString("provinceID"));
				dCity.setStatus(json.getString("status"));
				dCity.setWritetime(json.getString("writetime"));
				dCity.setLanguage(json.getString("language"));
				list.add(dCity);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		new Thread(new Runnable() {
			public void run() {
				try {
					db.saveOrUpdate(list);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 通知下一个接口操作
		onDxqHttpListener.onCompleted(HomePageFragment.THREAD_FIVE);
	}

	public static void doProvinceDb(JSONArray jsonArray, final DbManager db, DxqHttpListener onDxqHttpListener) {
		final List<Province> list = new ArrayList<Province>();
		for (int i = 0; i < jsonArray.length(); i++) {
			JSONObject json = (JSONObject) jsonArray.optJSONObject(i);
			Province dProvince = new Province();
			try {
				dProvince.setId(json.getString("id"));
				dProvince.setTitle(json.getString("title"));
				dProvince.setStatus(json.getString("status"));
				dProvince.setWritetime(json.getString("writetime"));
				dProvince.setLanguage(json.getString("language"));
				list.add(dProvince);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		new Thread(new Runnable() {
			public void run() {
				try {
					db.saveOrUpdate(list);
				} catch (DbException e) {
					e.printStackTrace();
				}
			}
		}).start();
		// 通知下一个接口操作
		onDxqHttpListener.onCompleted(HomePageFragment.THREAD_END);
	}
}
