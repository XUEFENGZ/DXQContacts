package com.daxiangqun.contacts.datas;

public class CodeDatas {
	
	private String success;
	private String yzm;
	private String jurisdictionID;
	private String phone;
	
	public CodeDatas() {
		// TODO Auto-generated constructor stub
	}

	public String getSuccess() {
		return success;
	}

	public void setSuccess(String success) {
		this.success = success;
	}

	public String getYzm() {
		return yzm;
	}

	public void setYzm(String yzm) {
		this.yzm = yzm;
	}

	public String getJurisdictionID() {
		return jurisdictionID;
	}

	public void setJurisdictionID(String jurisdictionID) {
		this.jurisdictionID = jurisdictionID;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public CodeDatas(String success, String yzm, String jurisdictionID, String phone) {
		super();
		this.success = success;
		this.yzm = yzm;
		this.jurisdictionID = jurisdictionID;
		this.phone = phone;
	}

	@Override
	public String toString() {
		return "CodeDatas [success=" + success + ", yzm=" + yzm + ", jurisdictionID=" + jurisdictionID + ", phone="
				+ phone + "]";
	}
	
}
