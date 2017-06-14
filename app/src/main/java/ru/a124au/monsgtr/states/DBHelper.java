package ru.a124au.monsgtr.states;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by monsgtr on 11/15/16.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 6;

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
                + "state_id integer, date datetime, end_time datetime);");


        db.execSQL("create table current_state ("
                + "id integer primary key autoincrement,"
                + "state_id integer);");
        db.execSQL("INSERT INTO current_state (state_id) VALUES ('-1');");


        db.execSQL("create table questions ("
                + "id integer primary key autoincrement,"
                + "name text);");
        db.execSQL("INSERT INTO questions (id, name) VALUES (1, 'свою усталость');");
        db.execSQL("INSERT INTO questions (id, name) VALUES (2, 'свое настроение');");
        db.execSQL("INSERT INTO questions (id, name) VALUES (3, 'свой голод');");
        db.execSQL("INSERT INTO questions (id, name) VALUES (4, 'свою cонливость');");
        db.execSQL("INSERT INTO questions (id, name) VALUES (5, 'свою раздражительность');");
        db.execSQL("INSERT INTO questions (id, name) VALUES (6, 'свое физическое самочувствие');");


        db.execSQL("create table answer_time ("
                + "id integer primary key autoincrement,"
                + "question_id integer, date datetime, answer integer);");
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

                db.execSQL("create table answers ("
                        + "id integer primary key autoincrement,"
                        + "question_id integer, answer integer);");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
        if (oldVersion < 3) {
            db.beginTransaction();
            try {

                db.execSQL("create table answers ("
                        + "id integer primary key autoincrement,"
                        + "question_id integer, answer integer);");
                db.execSQL("INSERT INTO questions (id, name) VALUES (1, 'свою усталость');");
                db.execSQL("INSERT INTO questions (id, name) VALUES (2, 'свое настроение');");
                db.execSQL("INSERT INTO questions (id, name) VALUES (3, 'свой голод');");
                db.execSQL("INSERT INTO questions (id, name) VALUES (4, 'свою cонливость');");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion < 4) {
            db.beginTransaction();
            try {
                db.execSQL("INSERT INTO questions (id, name) VALUES (5, 'свою раздражительность');");
                db.execSQL("INSERT INTO questions (id, name) VALUES (6, 'свое физическое самочувствие');");

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion < 5) {
            db.beginTransaction();
            try {
                db.execSQL("ALTER TABLE state_time ADD end_time datetime;");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }

        if (oldVersion < 6) {
            db.beginTransaction();
            try {
                db.execSQL("create table answer_time ("
                        + "id integer primary key autoincrement,"
                        + "question_id integer, date datetime, answer integer);");
                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }
        }
    }
}
