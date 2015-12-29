package com.daxiangqun.contacts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

//在表上创建一个简单的索引。允许使用重复的值：
@Table(name = "personContacts", onCreated = "CREATE UNIQUE INDEX index_name ON personContacts(orderID,provinceID,cityID,officeID,sectionID,status,language)")
public class PersonContacts {
	@Column(name = "id", isId = true)
	private String id;// 123 1（局长副局长什么的） 2（中层领导） 3（员工）
	@Column(name = "title")
	private String title;// 用户姓名
	@Column(name = "jurisdiction")
	private String jurisdiction;// 权限 id
	@Column(name = "orderID")
	private String orderID;// 排序 id
	@Column(name = "duty")
	private String duty;// 职务
	@Column(name = "tel")
	private String tel;// 电话
	@Column(name = "fax")
	private String fax;// 传真
	@Column(name = "phone1")
	private String phone1;// 手机1
	@Column(name = "phone2")
	private String phone2;// 手机2
	@Column(name = "email")
	private String email;// 电子邮箱
	@Column(name = "url")
	private String url;// 网址
	@Column(name = "provinceID")
	private String provinceID;// 省份 id
	@Column(name = "cityID")
	private String cityID;// 城市 id
	@Column(name = "typeID")
	private String typeID;// 分类 id
	@Column(name = "officeID")
	private String officeID;// 旅游局 id
	@Column(name = "sectionID")
	private String sectionID;// 部门 id
	@Column(name = "site")
	private String site; // 地址
	@Column(name = "attachpath")
	private String attachpath;// 头像路径
	@Column(name = "attachment")
	private String attachment;// 头像文件名称
	@Column(name = "status")
	private String status; // 显示状态（1为显示 2为暂存）
	@Column(name = "writetime")
	private String writetime;// 写入时间
	@Column(name = "language")
	private String language; // 语言（1为中文）
	
	private String sectionName;//部门名称
	@Column(name = "officename")
	private String officename;//旅游局名称
	public PersonContacts() {
		// TODO Auto-generated constructor stub
	}
	public String getOfficename() {
		return officename;
	}

	public void setOfficename(String officename) {
		this.officename = officename;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName(String sectionName) {
		this.sectionName = sectionName;
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getJurisdiction() {
		return jurisdiction;
	}

	public void setJurisdiction(String jurisdiction) {
		this.jurisdiction = jurisdiction;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getProvinceID() {
		return provinceID;
	}

	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}

	public String getCityID() {
		return cityID;
	}

	public void setCityID(String cityID) {
		this.cityID = cityID;
	}

	public String getTypeID() {
		return typeID;
	}

	public void setTypeID(String typeID) {
		this.typeID = typeID;
	}

	public String getOfficeID() {
		return officeID;
	}

	public void setOfficeID(String officeID) {
		this.officeID = officeID;
	}

	public String getSectionID() {
		return sectionID;
	}

	public void setSectionID(String sectionID) {
		this.sectionID = sectionID;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getAttachpath() {
		return attachpath;
	}

	public void setAttachpath(String attachpath) {
		this.attachpath = attachpath;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getWritetime() {
		return writetime;
	}

	public void setWritetime(String writetime) {
		this.writetime = writetime;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}
	public PersonContacts(String id, String title, String jurisdiction, String orderID, String duty, String tel,
			String fax, String phone1, String phone2, String email, String url, String provinceID, String cityID,
			String typeID, String officeID, String sectionID, String site, String attachpath, String attachment,
			String status, String writetime, String language, String sectionName, String officename) {
		super();
		this.id = id;
		this.title = title;
		this.jurisdiction = jurisdiction;
		this.orderID = orderID;
		this.duty = duty;
		this.tel = tel;
		this.fax = fax;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.email = email;
		this.url = url;
		this.provinceID = provinceID;
		this.cityID = cityID;
		this.typeID = typeID;
		this.officeID = officeID;
		this.sectionID = sectionID;
		this.site = site;
		this.attachpath = attachpath;
		this.attachment = attachment;
		this.status = status;
		this.writetime = writetime;
		this.language = language;
		this.sectionName = sectionName;
		this.officename = officename;
	}
	@Override
	public String toString() {
		return "PersonContacts [id=" + id + ", title=" + title + ", jurisdiction=" + jurisdiction + ", orderID="
				+ orderID + ", duty=" + duty + ", tel=" + tel + ", fax=" + fax + ", phone1=" + phone1 + ", phone2="
				+ phone2 + ", email=" + email + ", url=" + url + ", provinceID=" + provinceID + ", cityID=" + cityID
				+ ", typeID=" + typeID + ", officeID=" + officeID + ", sectionID=" + sectionID + ", site=" + site
				+ ", attachpath=" + attachpath + ", attachment=" + attachment + ", status=" + status + ", writetime="
				+ writetime + ", language=" + language + ", sectionName=" + sectionName + ", officename=" + officename
				+ "]";
	}

}