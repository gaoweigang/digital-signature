package com.gwg.demo.sign.test2;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import java.io.Serializable;

public class DigitalSignatureDTO implements Serializable {
    private static final long serialVersionUID = -5365630128856068164L;
    private String appKey;
    private CertTypeEnum certType;
    private DigestAlgEnum digestAlg;
    private String plainText;
    private String signature;

    public DigitalSignatureDTO() {
    }

    public DigestAlgEnum getDigestAlg() {
        return this.digestAlg;
    }

    public void setDigestAlg(DigestAlgEnum digestAlg) {
        this.digestAlg = digestAlg;
    }

    public String getPlainText() {
        return this.plainText;
    }

    public void setPlainText(String plainText) {
        this.plainText = plainText;
    }

    public String getSignature() {
        return this.signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    public String getAppKey() {
        return this.appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public CertTypeEnum getCertType() {
        return this.certType;
    }

    public void setCertType(CertTypeEnum certType) {
        this.certType = certType;
    }
}
