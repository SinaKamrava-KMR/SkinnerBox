package com.mainway.skinnerbox.DataBase;


import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.mainway.skinnerbox.Models.Card;
import com.mainway.skinnerbox.Models.GroupItem;


@Database(version = 1,exportSchema = false,entities = {Card.class,GroupItem.class})
public abstract class AppDataBase extends RoomDatabase {

    public static AppDataBase appDataBase;

    public static AppDataBase getInstance(Context context) {
        if (appDataBase==null)
            appDataBase= Room.databaseBuilder(context.getApplicationContext(),AppDataBase.class,"leitner.db")
                    .allowMainThreadQueries()
                    .build();
        return appDataBase;
    }


    public abstract GroupItemDao getGroupItemDao();
    public abstract CardsTackDao getCardsTackDao();



}
