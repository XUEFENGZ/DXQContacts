package com.daxiangqun.contacts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
//城市
@Table(name = "city", onCreated = "CREATE UNIQUE INDEX index_name ON city(title,provinceID,status,writetime,language)")
public class City {
	@Column(name = "id", isId = true)
	private String id;
	@Column(name = "title")
	private String title;
	@Column(name = "provinceID")
	private String provinceID;
	@Column(name = "status")
	private String status;
	@Column(name = "writetime")
	private String writetime;
	@Column(name = "language")
	private String language;
	public City() {
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

	public City(String id, String title, String provinceID, String status, String writetime, String language) {
		super();
		this.id = id;
		this.title = title;
		this.provinceID = provinceID;
		this.status = status;
		this.writetime = writetime;
		this.language = language;
	}

	@Override
	public String toString() {
		return "City [id=" + id + ", title=" + title + ", provinceID=" + provinceID + ", status=" + status
				+ ", writetime=" + writetime + ", language=" + language + "]";
	}
	
}
