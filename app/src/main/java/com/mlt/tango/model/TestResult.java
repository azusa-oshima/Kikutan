package com.mlt.tango.model;

public enum TestResult {

    NO_DATA("0"),
    SUPER_BAD("1"),
    BAD("2"),
    GOOD("3"),
    FINISH("99");

    // フィールドの定義
    private String testResult;

    // コンストラクタの定義
    private TestResult(String testResult) {
        this.testResult = testResult;
    }

    // コンストラクタの定義
    public String getCode() {
        return this.testResult;
    }

    // コンストラクタの定義
    public boolean equalsByCode(String testStatus) {
        if (this.testResult.equals(testStatus)) {
            return true;
        }
        return  false;
    }
}
