package com.karan.thenaptaker.napdatabase;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "NAPDETAILS.db";
    public static final String TABLE_NAME = "napDetails";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_ALARMMUSICID = "alarmMusicID";
    public static final String COLUMN_NAPMUSICID = "napMusicID";
    public static final String COLUMN_TIME = "time";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table "+TABLE_NAME+" " +
                        "(id integer primary key, name text, alarmMusicID integer ,napMusicID integer,time real)"
        );
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, "NASA NAP");
        contentValues.put(COLUMN_ALARMMUSICID, 0);
        contentValues.put(COLUMN_NAPMUSICID, 3);
        contentValues.put(COLUMN_TIME, 26);
        db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COLUMN_NAME, "POWER NAP");
        contentValues.put(COLUMN_ALARMMUSICID, 1);
        contentValues.put(COLUMN_NAPMUSICID, 1);
        contentValues.put(COLUMN_TIME, 28);
        db.insert(TABLE_NAME, null, contentValues);
        contentValues.put(COLUMN_NAME, "LIGHT NAP");
        contentValues.put(COLUMN_ALARMMUSICID, 2);
        contentValues.put(COLUMN_NAPMUSICID, 3);
        contentValues.put(COLUMN_TIME, 30);
        db.insert(TABLE_NAME, null, contentValues);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertNapDetails (String name, int alarmMusicID, int napMusicID, float time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ALARMMUSICID, alarmMusicID);
        contentValues.put(COLUMN_NAPMUSICID, napMusicID);
        contentValues.put(COLUMN_TIME, time);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME+" where id="+id+"", null );
        return res;
    }



    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        return (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
    }

    public boolean updateNapDetails (Integer id,String name, int alarmMusicID, int napMusicID, float time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_ALARMMUSICID, alarmMusicID);
        contentValues.put(COLUMN_NAPMUSICID, napMusicID);
        contentValues.put(COLUMN_TIME, time);
        db.update(TABLE_NAME, contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer deleteNapDetails (Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME,
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

    public ArrayList<String> getAllNapDetails () {
        ArrayList<String> array_list = new ArrayList<String>();


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        while(!res.isAfterLast()){
            array_list.add(res.getString(res.getColumnIndex(COLUMN_NAME)));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public String[] getAllNapDetailsInStringArray () {
        String[] array_list =new String[numberOfRows()];


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from "+TABLE_NAME, null );
        res.moveToFirst();

        for (int i=0;!res.isAfterLast();i++){
            array_list[i]=res.getString(res.getColumnIndex(COLUMN_NAME));
            res.moveToNext();
        }
        res.close();
        return array_list;
    }
}