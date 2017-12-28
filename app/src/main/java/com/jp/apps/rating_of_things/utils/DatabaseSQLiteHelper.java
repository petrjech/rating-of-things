package com.jp.apps.rating_of_things.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DatabaseSQLiteHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "rating_of_things.db";
    private static final int DATABASE_VERSION = 1;

    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");

    public enum TABLES {
        ITEMS, RATINGS, TAGS, ITEM_TAGS
    }

    public enum ITEMS_COLUMNS {
        ID, NAME, DESCRIPTION
    }

    public enum RATINGS_COLUMNS {
        ID, ITEM_ID, RATING, DATE, NOTE
    }

    public enum TAGS_COLUMNS {
        ID, NAME
    }

    public enum ITEM_TAGS_COLUMNS {
        ID, ITEM_ID, TAG_ID
    }

    private static DatabaseSQLiteHelper instance;

    private DatabaseSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static DatabaseSQLiteHelper getInstance(Context context){
        if (instance == null){
            instance = new DatabaseSQLiteHelper(context);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(createTableItems());
        database.execSQL(createTableRatings());
        database.execSQL(createTableTags());
        database.execSQL(createTableItemTags());
    }

     @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DatabaseSQLiteHelper.class.getName(), "Version 1 - upgrading of the database isn't implemented");
        throw new UnsupportedOperationException("Version 1 - upgrading of the database isn't implemented");
    }

    private static String createTableItems(){
        return "CREATE TABLE " + TABLES.ITEMS.name() +"("
                + ITEMS_COLUMNS.ID.name()      + " INTEGER PRIMARY KEY ASC,"
                + ITEMS_COLUMNS.NAME.name()          + " TEXT NOT NULL,"
                + ITEMS_COLUMNS.DESCRIPTION.name()   + " TEXT NOT NULL,"
                + ");";
    }

    private static String createTableRatings(){
        return "CREATE TABLE " + TABLES.RATINGS.name() + "("
                + RATINGS_COLUMNS.ID.name()  + " INTEGER NOT NULL,"
                + RATINGS_COLUMNS.ITEM_ID.name()      + " TEXT NOT NULL,"
                + RATINGS_COLUMNS.DATE.name() + " TEXT NOT NULL,"
                + RATINGS_COLUMNS.RATING.name() + " INTEGER NOT NULL,"
                + RATINGS_COLUMNS.NOTE.name() + " TEXT NOT NULL,"
                + " FOREIGN KEY(" + RATINGS_COLUMNS.ITEM_ID.name() + ") REFERENCES ITEMS(" + ITEMS_COLUMNS.ID.name() + ")"
                + ");";
    }

    private static String createTableTags(){
        return "CREATE TABLE " + TABLES.TAGS.name() +"("
                + TAGS_COLUMNS.ID.name()      + " INTEGER PRIMARY KEY ASC,"
                + TAGS_COLUMNS.NAME.name()          + " TEXT NOT NULL,"
                + ");";
    }

    private static String createTableItemTags(){
        return "CREATE TABLE " + TABLES.ITEM_TAGS.name() + "("
                + ITEM_TAGS_COLUMNS.ID.name()  + " INTEGER NOT NULL,"
                + ITEM_TAGS_COLUMNS.ITEM_ID.name()      + " TEXT NOT NULL,"
                + ITEM_TAGS_COLUMNS.TAG_ID.name() + " TEXT NOT NULL,"
                + " FOREIGN KEY(" + ITEM_TAGS_COLUMNS.ITEM_ID.name() + ") REFERENCES ITEMS(" + ITEMS_COLUMNS.ID.name() + "),"
                + " FOREIGN KEY(" + ITEM_TAGS_COLUMNS.TAG_ID.name() + ") REFERENCES ITEMS(" + TAGS_COLUMNS.ID.name() + "),"
                + " UNIQUE (" +ITEM_TAGS_COLUMNS.ITEM_ID.name() + ", " + ITEM_TAGS_COLUMNS.TAG_ID.name() + ") ON CONFLICT REPLACE"
                + ");";
    }

    public static String convertDateToString(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date convertStringToDate(String date) throws ParseException {
        return DATE_FORMAT.parse(date);
    }

}
