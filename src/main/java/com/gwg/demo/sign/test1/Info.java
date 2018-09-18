package com.gwg.demo.sign.test1;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="INFO")
public class Info {
	private String TRX_CODE = "";
	private String VERSION = "";
	private String DATA_TYPE = "";
	private String LEVEL = "";
	private String USER_NAME = "";
	private String USER_PASS = "";
	private String REQ_SN = "";
	private String REQTIME;
	private String SIGNED_MSG = "";

	public String getDATA_TYPE() {
		return this.DATA_TYPE;
	}

	public void setDATA_TYPE(String data_type) {
		this.DATA_TYPE = data_type;
	}

	public String getLEVEL() {
		return this.LEVEL;
	}

	public void setLEVEL(String level) {
		this.LEVEL = level;
	}

	public String getUSER_NAME() {
		return this.USER_NAME;
	}

	public void setUSER_NAME(String user_name) {
		this.USER_NAME = user_name;
	}

	public String getUSER_PASS() {
		return this.USER_PASS;
	}

	public void setUSER_PASS(String user_pass) {
		this.USER_PASS = user_pass;
	}

	public String getREQ_SN() {
		return this.REQ_SN;
	}

	public void setREQ_SN(String req_sn) {
		this.REQ_SN = req_sn;
	}

	public String getSIGNED_MSG() {
		return this.SIGNED_MSG;
	}

	public void setSIGNED_MSG(String signed_msg) {
		this.SIGNED_MSG = signed_msg;
	}

	public String getTRX_CODE() {
		return this.TRX_CODE;
	}

	public void setTRX_CODE(String trx_code) {
		this.TRX_CODE = trx_code;
	}

	public String getVERSION() {
		return this.VERSION;
	}

	public void setVERSION(String version) {
		this.VERSION = version;
	}

	public String getREQTIME() {
		return this.REQTIME;
	}

	public void setREQTIME(String rEQTIME) {
		this.REQTIME = rEQTIME;
	}
}