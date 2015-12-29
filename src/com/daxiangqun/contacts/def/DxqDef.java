package com.daxiangqun.contacts.def;

public class DxqDef {
	//http://gjlyj.daxiangqun.net/
	private final static String url="http://gjlyj.daxiangqun.cn/";
	//获取人员列表
	public final static String PersonnelUrl=url+"index.php/Ch/Cms/Search/personnel";
	//获取部门列表
	public final static String SectionUrl=url+"index.php/Ch/Cms/Search/section";
	//获取旅游局列表
	public final static String OfficeUrl = url+"index.php/Ch/Cms/Search/office";
	//获取城市列表
	public final static String CityUrl = url+"index.php/Ch/Cms/Search/city";
	//获取省份列表
	public final static String ProvinceUrl=url+"index.php/Ch/Cms/Search/province";
	//手机号获得验证码
	public final static String SetSMSUrl = url+"index.php/Ch/Cms/Login/sendMsg";
	//绑定手机号BINDSMSURL
	public final static String BindSMSUrl = url+"index.php/Ch/Cms/Login/checkcode";
	//数据库路径
	public final static String DBPATH = "/sdcard/dxqContacts";
	//code 和phone post
	public final static String ISLOGIN=url+"index.php/Ch/Cms/Login/checkyzm";
	public final static String PreferencesName = "login";
	public final static String SETCALL=url+"index.php/Ch/Cms/Customer/msgadd";
	
}
