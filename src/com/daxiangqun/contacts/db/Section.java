package com.daxiangqun.contacts.db;

import java.util.List;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
@Table(name = "section", onCreated = "CREATE UNIQUE INDEX index_name ON section(title,provinceID,cityID,typeID,officeID,status,writetime,language)")
public class Section {
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
	@Column(name = "officeID")
	private String officeID;
	@Column(name = "status")
	private String status;
	@Column(name = "writetime")
	private String writetime;
	@Column(name = "language")
	private String language;
	
	//二级数据生成
	private List<PersonContacts> psList;
	
	public List<PersonContacts> getPsList() {
		return psList;
	}

	public void setPsList(List<PersonContacts> psList) {
		this.psList = psList;
	}

	public Section() {
		// TODO Auto-generated constructor stub
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

	public String getOfficeID() {
		return officeID;
	}

	public void setOfficeID(String officeID) {
		this.officeID = officeID;
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

	@Override
	public String toString() {
		return "Section [id=" + id + ", title=" + title + ", provinceID=" + provinceID + ", cityID=" + cityID
				+ ", typeID=" + typeID + ", officeID=" + officeID + ", status=" + status + ", writetime=" + writetime
				+ ", language=" + language + ", psList=" + psList + "]";
	}

	public Section(String id, String title, String provinceID, String cityID, String typeID, String officeID,
			String status, String writetime, String language, List<PersonContacts> psList) {
		super();
		this.id = id;
		this.title = title;
		this.provinceID = provinceID;
		this.cityID = cityID;
		this.typeID = typeID;
		this.officeID = officeID;
		this.status = status;
		this.writetime = writetime;
		this.language = language;
		this.psList = psList;
	}

	
}
