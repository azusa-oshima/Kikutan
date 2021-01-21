package com.mlt.tango.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mlt.tango.entity.WordTestEntity;

import java.util.List;

/*
* ユーザ情報
* */
@Dao
public interface WordTestDao {

        @Insert
        void insert(WordTestEntity entity);

        @Update
        int update(WordTestEntity entity);

        @Query("update" +
                " word_test"+
                " set" +
                " category = :category" +
                " , test_status = :testStatus" +
                " , test_result = :testResult" +
                " where" +
                " number in (:numberList)")
        int updateForNewRound(int category, String testStatus, String testResult, List<Integer> numberList);

        @Query("update" +
                " word_test"+
                " set" +
                " test_status = :testStatus" +
                " where" +
                " number in (:numberList)")
        int updateTestStatus(String testStatus, List<String> numberList);
}