package com.gwg.demo.sign.test2;

import java.util.HashMap;
import java.util.Map;

public enum CertTypeEnum {
    AES128("AES128", "AES算法，密钥长度128", "AES", 128, 16, true),
    AES256("AES256", "AES算法, 密钥长度256", "AES", 256, 32, true),
    RSA2048("RSA2048", "RSA算法, 密钥长度2048", "RSA", 2048, 294, false),
    RSA4096("RSA4096", "RSA算法, 密钥长度4096", "RSA", 4096, 550, false);

    private static final Map<String, CertTypeEnum> VALUE_MAP = new HashMap();
    private String value;
    private String displayName;
    private String algorithm;
    private int keySize;
    private int encodedSize;
    private boolean symmetric;

    private CertTypeEnum(String value, String displayName, String algorithm, int keySize, int encodedSize, boolean symmetric) {
        this.value = value;
        this.displayName = displayName;
        this.algorithm = algorithm;
        this.keySize = keySize;
        this.symmetric = symmetric;
        this.encodedSize = encodedSize;
    }

    public static CertTypeEnum parse(String value) {
        return (CertTypeEnum)VALUE_MAP.get(value);
    }

    public String getValue() {
        return this.value;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getAlgorithm() {
        return this.algorithm;
    }

    public int getKeySize() {
        return this.keySize;
    }

    public boolean isSymmetric() {
        return this.symmetric;
    }

    public int getEncodedSize() {
        return this.encodedSize;
    }

    public static Map<String, CertTypeEnum> getValueMap() {
        return VALUE_MAP;
    }

    static {
        CertTypeEnum[] var0 = values();
        int var1 = var0.length;

        for(int var2 = 0; var2 < var1; ++var2) {
            CertTypeEnum item = var0[var2];
            VALUE_MAP.put(item.value, item);
        }

    }
}
