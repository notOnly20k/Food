package com.example.food.dao;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.food.bean.Message;
import com.example.food.bean.Shop;
import com.example.food.bean.User;
import com.example.food.bean.UserAllShop;
import com.example.food.bean.UserFav;

@Database(entities = {User.class, Message.class, Shop.class, UserFav.class}, version = 3, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "food.db";
    private static volatile AppDatabase instance;

    static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(
                context,
                AppDatabase.class,
                DB_NAME)
//                .fallbackToDestructiveMigration()
                .build();
    }

//    public static final Migration MIGRATION_1_2 = new Migration(1, 2) {
//        @Override
//        public void migrate(SupportSQLiteDatabase database) {
//            //告诉person表，增添一个String类型的字段 son
//            database.execSQL("ALTER TABLE Shop ADD COLUMN son TEXT");
//        }
//    };


    public abstract UserDao userDao();
    public abstract ShopDao shopDao();

}
