package com.gwg.demo.test1.cer;

public interface CAConfig {

    /**
     * C
     */
    String CA_C = "CN";
    /**
     * ST
     */
    String CA_ST = "BJ";
    /**
     * L
     */
    String CA_L = "BJ";
    /**
     */
    String CA_O = "SICCA";

    /**
     * CA_ROOT_ISSUER
     */
    String CA_ROOT_ISSUER="C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=SICCA";
    /**
     * CA_DEFAULT_SUBJECT
     */
    String CA_DEFAULT_SUBJECT="C=CN,ST=BJ,L=BJ,O=SICCA,OU=SC,CN=";

    String CA_SHA="SHA256WithRSAEncryption";

}