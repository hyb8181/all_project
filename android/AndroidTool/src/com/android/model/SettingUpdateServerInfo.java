package com.android.model;

/**
 * �������ģ��������������
 * @author Administrator
 *
 */
public class SettingUpdateServerInfo {
	public String latest;//1Ϊ��Ҫ����,0Ϊ������
	public String pversion;//��������Ʒ�汾��1.2.1
	public String url;//���ص�ַ
	public String changes;//�������
	public String getlatest() {
		return latest;
	}
	public void setlatest(String latest) {
		this.latest = latest;
	}		
	
	public String getpversion() {
		return pversion;
	}
	public void setpversion(String pversion) {
		this.pversion = pversion;
	}		
	
	public String geturl() {
		return url;
	}
	public void seturl(String url) {
		this.url = url;
	}		
	
	public String getchanges() {
		return changes;
	}
	public void setchanges(String changes) {
		this.changes = changes;
	}		


}
