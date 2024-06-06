package com.example.toytrade2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "toytrade.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  name text NOT NULL,\n" +
                "  email text NOT NULL UNIQUE,\n" +
                "  phone text NOT NULL UNIQUE,\n" +
                "  password text NOT NULL,\n" +
                "  photo blob \n" +
                ");");

        db.execSQL("CREATE TABLE addresses (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  user_id integer NOT NULL,\n" +
                "  address text NOT NULL, \n" +
                "  FOREIGN KEY(user_id) REFERENCES users(id));");

        db.execSQL("CREATE TABLE toys (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  user_id integer NOT NULL,\n" +
                "  name text NOT NULL,\n" +
                "  condition text NOT NULL,\n" +
                "  category text NOT NULL,\n" +
                "  age_category text NOT NULL,\n" +
                "  description text NOT NULL,\n" +
                "  exchange_preferences text NOT NULL, \n" +
                "  address_id integer NOT NULL,\n" +
                "  is_active boolean NOT NULL, \n" +
//                "  FOREIGN KEY(address_id) REFERENCES addresses(id), " +
                "  FOREIGN KEY(user_id) REFERENCES users(id));");

        db.execSQL("CREATE TABLE toys_photos (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  toy_id integer NOT NULL,\n" +
                "  photo blob NOT NULL,\n" +
                "  FOREIGN KEY(toy_id) REFERENCES toys(id));");

        db.execSQL("CREATE TABLE trades (\n" +
                "  id integer PRIMARY KEY AUTOINCREMENT,\n" +
                "  user1_id integer NOT NULL,\n" +
                "  user2_id integer NOT NULL,\n" +
                "  toy1_id integer NOT NULL,\n" +
                "  toy2_id integer NOT NULL,\n" +
                "  status boolean NOT NULL,\n" +
                "  FOREIGN KEY(user1_id) REFERENCES users(id), " +
                "  FOREIGN KEY(user2_id) REFERENCES users(id), " +
                "  FOREIGN KEY(toy1_id) REFERENCES toys(id), " +
                "  FOREIGN KEY(toy2_id) REFERENCES toys(id) )");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users");
        db.execSQL("DROP TABLE IF EXISTS addresses");
        db.execSQL("DROP TABLE IF EXISTS toys");
        db.execSQL("DROP TABLE IF EXISTS toys_photos");
        db.execSQL("DROP TABLE IF EXISTS trades");
        onCreate(db);
    }
}
