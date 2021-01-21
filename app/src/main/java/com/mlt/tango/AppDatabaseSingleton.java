package com.mlt.tango;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

public class AppDatabaseSingleton {
    private static AppDatabase instance = null;
    private AppDatabaseSingleton() {}



    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
//            database.execSQL(
//                    "CREATE TABLE IF NOT EXISTS " +
//                            "`user_info` (" +
//                            "`number` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
//                            "`now_category` INTEGER NOT NULL," +
//                            "`rank` INTEGER NOT NULL, " +
//                            "`exp` INTEGER NOT NULL" +
//                            ")");
        }
    };

    /*
    * インスタンス生成
    *
    **/
    public static AppDatabase init(Context context) {
        if (instance != null) {
            return instance;
        }

        RoomDatabase.Callback rdc = new RoomDatabase.Callback() {
            public void onCreate (SupportSQLiteDatabase db) {
                // do something after database has been created
                super.onCreate(db);

                // test.dbを更新して、versionを4に更新したときに子を追加した。
                System.out.println("てすと");
            }
            public void onOpen (SupportSQLiteDatabase db) {
                // do something every time database is open
//                System.out.println("てすとう");
            }
        };

        System.out.println("データベース再生成");
        instance =  Room.databaseBuilder(context, AppDatabase.class, "test")
                .createFromAsset("test.db")
                .addMigrations(MIGRATION_1_2)
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
//                .addCallback(rdc)
                .build();
        return instance;
    }

    public static AppDatabase getInstance() {
        return instance;
    }
}