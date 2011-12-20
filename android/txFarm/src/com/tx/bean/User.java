package com.tx.bean;

import java.io.Serializable;
/**
 * 用户信息
 * @author Crane
 *
 */
public class User implements Serializable{
	private static final long serialVersionUID = 1L;
	private int userId;
	private String imsi;
	private String phoneNum;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("User:\n");
		sb.append("userId=>" + userId);
		sb.append("\timsi=>" + imsi);
		sb.append("\tphoneNum=>" + phoneNum + "\n");
		return sb.toString();
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

}
