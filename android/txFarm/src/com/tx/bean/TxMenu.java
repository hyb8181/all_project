package com.tx.bean;

import java.io.Serializable;

/**
 * 客户端菜单类
 * @author Crane
 * 
 */
public class TxMenu implements Serializable{
	private static final long serialVersionUID = 1L;
	private int order;
	private String name;
	private String logo;
	private String url;

	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Menu:\n");
		sb.append("order=>" + order);
		sb.append("\tname=>" + name);
		sb.append("\tlogo=>" + logo);
		sb.append("\turl=>" + url);
		return sb.toString();
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
