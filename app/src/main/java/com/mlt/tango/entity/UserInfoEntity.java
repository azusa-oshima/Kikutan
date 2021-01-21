package com.mlt.tango.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/*
* ユーザ情報
* */
@Entity(tableName = "user_info")
public class UserInfoEntity {

    @PrimaryKey
    public int number;
    public int now_round;
    public int now_category;
    public int rank;
    public int exp;
}