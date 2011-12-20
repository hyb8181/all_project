package tx.phonebook.data.proc;

import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * 电话薄
 * @author Crane
 *
 */
public class PhoneBook {
	@Expose
	private String userId;
	@Expose
	private String imsi;
	@Expose
	private String psId;
	@Expose
	private String action;
	@Expose
	private PersonInfo psInfo;

	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("PhoneBook:\n");
		sb.append("userId=>"+userId);
		sb.append("\timsi=>"+imsi);
		sb.append("\tpbs=>"+psInfo);
		return sb.toString();
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public PersonInfo getPsInfo() {
		return psInfo;
	}

	public void setPsInfo(PersonInfo psInfo) {
		this.psInfo = psInfo;
	}
	public String getPsId() {
		return psId;
	}

	public void setPsId(String psId) {
		this.psId = psId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

}
