package com.daxiangqun.contacts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;


@Table(name = "office", onCreated = "CREATE UNIQUE INDEX index_name ON office(title,provinceID,cityID,typeID,site,postcode,status,writetime,language)")
public class Office {
	
	@Column(name = "id", isId = true)
	private String id;
	@Column(name = "title")
	private String title;
	@Column(name = "provinceID")
	private String provinceID;
	@Column(name = "cityID")
	private String cityID;
	@Column(name = "typeID")
	private String typeID;
	@Column(name = "site")
	private String site;
	@Column(name = "postcode")
	private String postcode;
	@Column(name = "url")
	private String url;
	@Column(name = "status")
	private String status;
	@Column(name = "writetime")
	private String writetime;
	@Column(name = "language")
	private String language;
	
	@Column(name = "tel")
	private String tel;// 电话
	@Column(name = "fax")
	private String fax;// 传真
	@Column(name = "email")
	private String email;// 电子邮箱
	public Office() {
		// TODO Auto-generated constructor stub
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
	public Office(String id, String title, String provinceID, String cityID, String typeID, String site,
			String postcode, String url, String status, String writetime, String language, String tel, String fax,
			String email) {
		super();
		this.id = id;
		this.title = title;
		this.provinceID = provinceID;
		this.cityID = cityID;
		this.typeID = typeID;
		this.site = site;
		this.postcode = postcode;
		this.url = url;
		this.status = status;
		this.writetime = writetime;
		this.language = language;
		this.tel = tel;
		this.fax = fax;
		this.email = email;
	}
	@Override
	public String toString() {
		return "Office [id=" + id + ", title=" + title + ", provinceID=" + provinceID + ", cityID=" + cityID
				+ ", typeID=" + typeID + ", site=" + site + ", postcode=" + postcode + ", url=" + url + ", status="
				+ status + ", writetime=" + writetime + ", language=" + language + ", tel=" + tel + ", fax=" + fax
				+ ", email=" + email + "]";
	}
}
