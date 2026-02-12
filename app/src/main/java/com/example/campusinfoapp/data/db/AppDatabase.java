package com.example.campusinfoapp.data.db;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

// 数据库版本改为2，添加 User 实体
@Database(entities = {Course.class, User.class}, version = 2, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CourseDao courseDao();
    public abstract UserDao userDao();  // 新增 UserDao

    private static AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    AppDatabase.class, "campus_info_db")
                    .fallbackToDestructiveMigration() // 避免旧表结构冲突
                    .allowMainThreadQueries()         // 演示用，可在主线程操作
                    .build();
        }
        return instance;
    }
}
