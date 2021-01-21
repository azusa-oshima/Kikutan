package com.mlt.tango.model;

public enum TestStatus {

    ONGOING("0"),
    FINISH("1");

    // フィールドの定義
    private String testStatus;

    // コンストラクタの定義
    private TestStatus(String testStatus) {
        this.testStatus = testStatus;
    }

    // コンストラクタの定義
    public String getCode() {
        return this.testStatus;
    }

    // コンストラクタの定義
    public boolean equalsByCode(String testStatus) {
        if (this.testStatus.equals(testStatus)) {
            return true;
        }
        return  false;
    }
}
