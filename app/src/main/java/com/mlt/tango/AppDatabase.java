package com.mlt.tango;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.mlt.tango.dao.UserInfoDao;
import com.mlt.tango.dao.WordInfoDao;
import com.mlt.tango.dao.WordTestDao;
import com.mlt.tango.entity.UserInfoEntity;
import com.mlt.tango.entity.WordInfoEntity;
import com.mlt.tango.entity.WordTestEntity;

/*
* ユーザ情報
* */
@Database(entities = {UserInfoEntity.class,
                    WordInfoEntity.class,
                    WordTestEntity.class},
        version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
                public abstract UserInfoDao userInfoDao();
                public abstract WordInfoDao wordInfoDao();
                public abstract WordTestDao wordTestDao();
}
