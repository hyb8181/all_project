package com.tx.bean;

import android.graphics.Bitmap;

public class Ad {

	private String imgAddress;
	private String imgUrl;
	private Bitmap bm;
	
	public String getImgAddress() {
		return imgAddress;
	}
	public void setImgAddress(String imgAddress) {
		this.imgAddress = imgAddress;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public Bitmap getBm() {
		return bm;
	}
	public void setBm(Bitmap bm) {
		this.bm = bm;
	}
}
