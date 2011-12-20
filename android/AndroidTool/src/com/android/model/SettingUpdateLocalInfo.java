package com.android.model;

/**
 * �������ģ���ϴ����
 * @author Administrator
 *
 */
public class SettingUpdateLocalInfo {
	String os;//����汾Android.2.2
	String version;//�ͻ�������汾��1.2.0
	String pversion;//�ͻ��˲�Ʒ�汾��1.2.1
	String channel;//������0102
	String imei;//�ֻ�IMEIʶ��
	String screen;//��Ļ480X800
	
	public String getos() {
		return os;
	}
	public void setos(String os) {
		this.os = os;
	}
	
	public String getversion() {
		return version;
	}
	public void setversion(String version) {
		this.version = version;
	}
	
	public String getpversion() {
		return pversion;
	}
	public void setpversion(String pversion) {
		this.pversion = pversion;
	}

	public String getchannel() {
		return channel;
	}
	public void setchannel(String channel) {
		this.channel = channel;
	}
	
	public String getimei() {
		return imei;
	}
	public void setimei(String imei) {
		this.imei = imei;
	}	    	
	
	public String getscreen() {
		return screen;
	}
	public void setscreen(String screen) {
		this.screen = screen;
	}		


}
