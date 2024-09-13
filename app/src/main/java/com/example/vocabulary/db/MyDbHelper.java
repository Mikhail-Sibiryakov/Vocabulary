package com.example.vocabulary.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDbHelper extends SQLiteOpenHelper {

    public MyDbHelper(@Nullable Context context) {
        super(context, MyConstants.DB_NAME, null, MyConstants.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MyConstants.TABLE_STRUCT_EN);
        db.execSQL(MyConstants.TABLE_STRUCT_RU);
        db.execSQL(MyConstants.TABLE_STRUCT_EDGE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(MyConstants.DROP_TABLE_EN);
        db.execSQL(MyConstants.DROP_TABLE_RU);
        db.execSQL(MyConstants.DROP_TABLE_EDGE);
        onCreate(db);
    }
}
