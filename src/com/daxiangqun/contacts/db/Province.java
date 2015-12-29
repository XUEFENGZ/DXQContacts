package com.daxiangqun.contacts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
//省份
@Table(name = "province", onCreated = "CREATE UNIQUE INDEX index_name ON province(title,status,writetime,language)")
public class Province {
	@Column(name = "id", isId = true)
	private String id;
	@Column(name = "title")
	private String title;
	@Column(name = "status")
	private String status;
	@Column(name = "writetime")
	private String writetime;
	@Column(name = "language")
	private String language;

	public Province() {
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

	public Province(String id, String title, String status, String writetime, String language) {
		super();
		this.id = id;
		this.title = title;
		this.status = status;
		this.writetime = writetime;
		this.language = language;
	}

	@Override
	public String toString() {
		return "Province [id=" + id + ", title=" + title + ", status=" + status + ", writetime=" + writetime
				+ ", language=" + language + "]";
	}
}
