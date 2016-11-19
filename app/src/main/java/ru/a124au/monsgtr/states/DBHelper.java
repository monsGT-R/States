package ru.a124au.monsgtr.states;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by monsgtr on 11/15/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 2;

    final String LOG_TAG = "myLogs";

    public DBHelper(Context context) {
        // конструктор суперкласса
        super(context, "myDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("create table states ("
                + "id integer primary key autoincrement,"
                + "name text);");

        db.execSQL("create table state_time ("
                + "id integer primary key autoincrement,"
                + "state_id integer, date datetime);");


        db.execSQL("create table current_state ("
                + "id integer primary key autoincrement,"
                + "state_id integer);");
        db.execSQL("INSERT INTO current_state (state_id) VALUES ('-1');");


        db.execSQL("create table questions ("
                + "id integer primary key autoincrement,"
                + "name text);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion < 2) {
            db.beginTransaction();
            try {

                db.execSQL("alter table mytable rename to states;");

                // создаем таблицу должностей
                db.execSQL("create table state_time ("
                        + "id integer primary key autoincrement,"
                        + "state_id integer, date datetime);");


                db.execSQL("create table current_state ("
                        + "id integer primary key autoincrement,"
                        + "state_id integer);");
                db.execSQL("INSERT INTO current_state (state_id) VALUES ('-1');");


                db.execSQL("create table questions ("
                        + "id integer primary key autoincrement,"
                        + "name text);");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
