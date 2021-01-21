package com.mlt.tango.entity;

import java.io.Serializable;

/*
* ユーザ情報
* */
public class WordInfoDto implements Serializable {

    /* 単語番号 */
    public int number;
    /* ミャンマー語 */
    public String burmese;
    /* 日本語 */
    public String japanese;
    /* 表示番号 */
    public int display_number;

    public int category;
    public String test_status;
    public String test_result;
    public String pre_test_result;
    public int wrong_count;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getBurmese() {
        return burmese;
    }

    public void setBurmese(String burmese) {
        this.burmese = burmese;
    }

    public String getJapanese() {
        return japanese;
    }

    public void setJapanese(String japanese) {
        this.japanese = japanese;
    }

    public int getDisplay_number() {
        return display_number;
    }

    public void setDisplay_number(int display_number) {
        this.display_number = display_number;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getTest_status() {
        return test_status;
    }

    public void setTest_status(String test_status) {
        this.test_status = test_status;
    }

    public String getTest_result() {
        return test_result;
    }

    public void setTest_result(String test_result) {
        this.test_result = test_result;
    }

    public String getPre_test_result() {
        return pre_test_result;
    }

    public void setPre_test_result(String pre_test_result) {
        this.pre_test_result = pre_test_result;
    }

    public int getWrong_count() {
        return wrong_count;
    }

    public void setWrong_count(int wrong_count) {
        this.wrong_count = wrong_count;
    }
}