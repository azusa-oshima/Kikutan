package com.mlt.tango.dao;

import androidx.room.Dao;
import androidx.room.Query;

import com.mlt.tango.entity.WordInfoDto;

import java.util.List;

/*
* ユーザ情報
* */
@Dao
public interface WordInfoDao {

        @Query("select" +
                " wi.number as number" +
                ", wi.burmese as burmese" +
                ", wi.japanese as japanese" +
                ", wi.display_number as display_number" +
                ", wt.category as category" +
                ", wt.test_status  as test_status" +
                ", wt.test_result as test_result" +
                ", wt.pre_test_result as pre_test_result" +
                ", wt.wrong_count as wrong_count" +
                " from " +
                " word_info wi" +
                " left outer join " +
                " word_test wt" +
                " on " +
                " wi.number = wt.number" +
                " order by" +
                " wi.display_number")
        List<WordInfoDto> getAll();

        @Query("select" +
                " wi.number as number" +
                ", wi.burmese as burmese" +
                ", wi.japanese as japanese" +
                ", wi.display_number as display_number" +
                ", wt.category as category" +
                ", wt.test_status  as test_status" +
                ", wt.test_result as test_result" +
                ", wt.pre_test_result as pre_test_result" +
                ", wt.wrong_count as wrong_count" +
                " from " +
                " word_info wi" +
                " inner join " +
                " word_test wt" +
                " on " +
                " wi.number = wt.number" +
                " where " +
                " wt.category == :category" +
                " and wt.test_status == :test_status" +
                " order by" +
                " wi.display_number")
        List<WordInfoDto> getTestWord(int category, String test_status);

}