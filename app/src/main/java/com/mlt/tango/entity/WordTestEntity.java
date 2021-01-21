package com.mlt.tango.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/*
* テスト開始時に受け渡すワード情報
* */
@Entity(tableName = "word_test")
public class WordTestEntity implements Serializable {

    /* 単語番号 */
    @PrimaryKey
    public int number;
    /* テストターゲット */
    public int category;
    /* テストステータス */
    public String test_status;
    /* テスト結果 */
    public String test_result;
    /* テスト結果 */
    public String pre_test_result;
    /* 間違いカウント */
    public int wrong_count;
}
