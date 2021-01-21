package com.mlt.tango.model;

public enum TestTarget {

    OFF("0"),
    ON("1");

    // フィールドの定義
    private String testTarget;

    // コンストラクタの定義
    private TestTarget(String testTarget) {
        this.testTarget = testTarget;
    }

    // コンストラクタの定義
    public String getCode() {
        return this.testTarget;
    }

    // コンストラクタの定義
    public boolean equalsByCode(String testStatus) {
        if (this.testTarget.equals(testStatus)) {
            return true;
        }
        return  false;
    }
}
