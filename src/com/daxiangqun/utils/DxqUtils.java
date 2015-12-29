package com.daxiangqun.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xutils.DbManager;
import org.xutils.x;

import com.daxiangqun.contacts.activity.MainActivity;
import com.daxiangqun.contacts.def.DxqDef;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;

public class DxqUtils {

	private static final String TAG = "DxqCantacts";

	public static void FlLog(String msg) {
		if (MainActivity.isBebug) {
			Log.i(TAG, msg);
		}
	}

	// 使用反射机制解析json，此方法较灵活
	public static <T> T getPerson(String jsonString, Class<T> cls) {
		Gson gson = new Gson();// 创建一个GsonD对象
		T t = gson.fromJson(jsonString, cls);// 使用fromJson方法返回一个json对象
		return t;
	}

	// 使用Gson解析json数组
	public static <T> List<T> getPersonList(String jsonString, Class<T> cls) {
		List<T> list = new ArrayList<T>();
		Gson gson = new Gson();
		// TypeToken是Gson提供的数据类型转换器，可以支持各种数据集合类型的转换
		list = gson.fromJson(jsonString, new TypeToken<List<T>>() {
		}.getType());
		return list;
	}

	/**
	 * 判断当前是否有可用的网络以及网络类型 0：无网络 1：WIFI 2：CMWAP 3：CMNET
	 * 
	 * @param context
	 * @return
	 */
	public static int isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {
			return 0;
		} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						NetworkInfo netWorkInfo = info[i];
						if (netWorkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
							return 1;
						} else if (netWorkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
							String extraInfo = netWorkInfo.getExtraInfo();
							if ("cmwap".equalsIgnoreCase(extraInfo) || "cmwap:gsm".equalsIgnoreCase(extraInfo)) {
								return 2;
							}
							return 3;
						}
					}
				}
			}
		}
		return 0;
	}

	// 判断网络是否可用
	public static boolean isNetAvailable(Context context) {
		try {
			ConnectivityManager connectivity = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivity == null) {
				return false;
			}
			NetworkInfo[] networkInfos = connectivity.getAllNetworkInfo();
			if (networkInfos == null) {
				return false;
			}
			for (NetworkInfo networkInfo : networkInfos) {
				if (networkInfo.isConnectedOrConnecting()) {
					return true;
				}
			}
			return false;
		} catch (Throwable e) {
			return false;
		}
	}

	// 判斷手機號
	public static boolean isMobileNO(String mobiles) {
		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static DbManager openDb(String dbName) {
		DbManager.DaoConfig daoConfig = new DbManager.DaoConfig().setDbName(dbName).setDbDir(new File(DxqDef.DBPATH))
				.setDbVersion(1).setDbUpgradeListener(new DbManager.DbUpgradeListener() {
					@Override
					public void onUpgrade(final DbManager db, final int oldVersion, final int newVersion) {

					}
				});
		return x.getDb(daoConfig);
	}

	private static String sdState = Environment.getExternalStorageState();

	// 删除文件
	public static void delFile(String fileName) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(
					Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName);
			deleteFile(file);
			// file.exists();
		}
	}

	// 将SD卡文件删除
	public static void deleteFile(File file) {
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			if (file.exists()) {
				if (file.isFile()) {
					file.delete();
				}
				// 如果它是一个目录
				else if (file.isDirectory()) {
					// 声明目录下所有的文件 files[];
					File files[] = file.listFiles();
					for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
						deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
					}
				}
				file.delete();
			}
		}
	}
	//判断文件是否存在,不存在创建文件夹
	public static void creatFile(String fileName){
		if (sdState.equals(Environment.MEDIA_MOUNTED)) {
			File file = new File(
					Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + fileName);
			if(!file.exists()){
				file.mkdir();
			}
		}
	}
}
