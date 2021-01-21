package com.mlt.tango.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.mlt.tango.entity.UserInfoEntity;

/*
* ユーザ情報
* */
@Dao
public interface UserInfoDao {

        @Query("select *"+
                " from" +
                " user_info" +
                " where" +
                " number == :number")
        UserInfoEntity getByNumber(int number);

        @Query("update" +
                " user_info" +
                " set" +
                " now_category = :category" +
                " where" +
                " number == :number")
        int updateCategory(int number, int category);

        @Query("update" +
                " user_info" +
                " set" +
                " now_round = :round" +
                " , now_category = :category" +
                " where" +
                " number == :number")
        int updateRound(int number, int round, int category);

}