package com.tx.bean;

import java.io.Serializable;

/**
 * 应用信息
 * 
 * @author Crane
 * 
 */
public class App implements Serializable {
	private static final long serialVersionUID = 1L;
	private int appId;
	private String cnName;
	private String enName;
	private String ver;
	private String addr;// apk下载地址
	// 是否是第一次安装，是：1；否：其他
	private int isFirst;
	private int type;// 1:所发布客户端应用标志,0为天下游戏厅其他应用标志
	//private String indexUrl;//应用首页地址名称，

	/*
	 * //每次新做一个客户端，修改初始数据即可 public App(){ appId=1; cnName = "天下客户度"; enName =
	 * "TX Client"; ver = "1.0"; isFirst = 0; }
	 */

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("App:\n");
		sb.append("appId=>" + appId);
		sb.append("\tcnName=>" + cnName);
		sb.append("\tenName=>" + enName);
		sb.append("\tver=>" + ver);
		sb.append("\taddr=>" + addr);
		sb.append("\tisFirst=>" + isFirst);
		sb.append("\ttype=>" + type + "\n");
		return sb.toString();
	}

	public int getAppId() {
		return appId;
	}

	public void setAppId(int appId) {
		this.appId = appId;
	}

	public String getCnName() {
		return cnName;
	}

	public void setCnName(String cnName) {
		this.cnName = cnName;
	}

	public String getEnName() {
		return enName;
	}

	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getVer() {
		return ver;
	}

	public void setVer(String ver) {
		this.ver = ver;
	}

	public String getAddr() {
		return addr;
	}

	public void setAddr(String addr) {
		this.addr = addr;
	}

	public int getIsFirst() {
		return isFirst;
	}

	public void setIsFirst(int isFirst) {
		this.isFirst = isFirst;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
