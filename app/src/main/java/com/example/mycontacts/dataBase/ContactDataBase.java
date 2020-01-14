package com.example.mycontacts.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ContactDataBase extends SQLiteOpenHelper {

    private static String DB_NAME = "CONTACT";
    private static int DB_VERSION = 1;

    public ContactDataBase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE GROUP_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT , CONTACT_GROUP_NAME TEXT)";
        String query1 = "CREATE TABLE CONTACT_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT , GROUP_NAME,CONTACT_NAME,CONTACT_NUMBER)";

        db.execSQL(query);
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
