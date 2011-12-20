package tx.phonebook.data.proc;

import com.google.gson.annotations.Expose;

public class PersonUpdate {
	@Expose
	private String strUserId;
	@Expose
	private String[] strPsi;

	public String getStrUserId() {
		return strUserId;
	}
	public void setStrUserId(String strUserId) {
		this.strUserId = strUserId;
	}
	public String[] getStrPsi() {
		return strPsi;
	}
	public void setStrPsi(String[] strPsi) {
		this.strPsi = strPsi;
	}
}
