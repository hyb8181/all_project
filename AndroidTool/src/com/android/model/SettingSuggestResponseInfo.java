package com.android.model;
/**
 * ����-��������ģ������������
 * @author wj
 * 
 */
public class SettingSuggestResponseInfo {
	public String suggest;//����
	public String feedback;//����
	public Boolean state;//0Ϊδ�� 1Ϊ��2��ɾ��
	public String getSuggest() {
		return suggest;
	}
	public void setSuggest(String suggest) {
		this.suggest = suggest;
	}
	public String getFeedback() {
		return feedback;
	}
	public void setFeedback(String feedback) {
		this.feedback = feedback;
	}
	public Boolean getState() {
		return state;
	}
	public void setState(Boolean state) {
		this.state = state;
	}
}
