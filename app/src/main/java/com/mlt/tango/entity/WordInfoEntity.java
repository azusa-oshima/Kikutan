package com.mlt.tango.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


/*
* ユーザ情報
* */
@Entity(tableName = "word_info")
public class WordInfoEntity {

    /* 単語番号 */
    @PrimaryKey
    public int number;
    /* ミャンマー語 */
    public String burmese;
    /* 日本語 */
    public String japanese;
    /* 表示番号 */
    public int display_number;
}