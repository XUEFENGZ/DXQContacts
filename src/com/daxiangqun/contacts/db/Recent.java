package com.daxiangqun.contacts.db;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

@Table(name = "recent", onCreated = "CREATE UNIQUE INDEX index_name ON recent(title,tel,phone1,phone2)")
public class Recent {
	@Column(name = "id")
	private int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Column(name = "userid", isId = true)
	private String userid;// 123 1（局长副局长什么的） 2（中层领导） 3（员工）
	@Column(name = "title")
	private String title;// 用户姓名
	@Column(name = "tel")
	private String tel;// 电话
	@Column(name = "phone1")
	private String phone1;// 手机1
	@Column(name = "phone2")
	private String phone2;// 手机2
	@Column(name = "calltime")
	private String calltime;
	@Column(name = "duty")
	private String duty;
	@Column(name = "officeID")
	private String officeID;// 旅游局 id
	@Column(name = "sectionID")
	private String sectionID;// 部门 id
	@Column(name = "attachpath")
	private String attachpath;

	public Recent() {
		// TODO Auto-generated constructor stub
	}

	public String getAttachpath() {
		return attachpath;
	}

	public void setAttachpath(String attachpath) {
		this.attachpath = attachpath;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
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

	public String getCalltime() {
		return calltime;
	}

	public void setCalltime(String calltime) {
		this.calltime = calltime;
	}

	public String getDuty() {
		return duty;
	}

	public void setDuty(String duty) {
		this.duty = duty;
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

	public Recent(int id, String userid, String title, String tel, String phone1, String phone2, String calltime,
			String duty, String officeID, String sectionID, String attachpath) {
		super();
		this.id = id;
		this.userid = userid;
		this.title = title;
		this.tel = tel;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.calltime = calltime;
		this.duty = duty;
		this.officeID = officeID;
		this.sectionID = sectionID;
		this.attachpath = attachpath;
	}

	@Override
	public String toString() {
		return "Recent [id=" + id + ", userid=" + userid + ", title=" + title + ", tel=" + tel + ", phone1=" + phone1
				+ ", phone2=" + phone2 + ", calltime=" + calltime + ", duty=" + duty + ", officeID=" + officeID
				+ ", sectionID=" + sectionID + ", attachpath=" + attachpath + "]";
	}

}
