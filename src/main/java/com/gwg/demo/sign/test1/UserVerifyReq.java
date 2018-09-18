package com.gwg.demo.sign.test1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 四要素验证请求
 */
@XmlRootElement(name="AIPG")
@XmlAccessorType(XmlAccessType.FIELD)
public class UserVerifyReq {
	@XmlElement(name="INFO")
	private Info info;
	private ValidReq  VALIDR;
	public Info getInfo() {
		return info;
	}
	public void setInfo(Info info) {
		this.info = info;
	}
	public ValidReq getVALIDR() {
		return VALIDR;
	}
	public void setVALIDR(ValidReq vALIDR) {
		VALIDR = vALIDR;
	}
}
