package com.karan.thenaptaker.napwidget;


import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;

import android.database.Cursor;
import android.database.SQLException;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;

import android.net.Uri;
import android.text.TextUtils;

import java.util.HashMap;


/**
 * {@link MyContentProvider} provides details about  Nap selected
 */
public class MyContentProvider extends ContentProvider {

        static final String PROVIDER_NAME = "com.karan.thenaptaker.napwidget.MyContentProvider";
        static final String URL = "content://" + PROVIDER_NAME + "/widgetdata";
        static final Uri CONTENT_URI = Uri.parse(URL);

        static final String _ID = "_id";
        static final String NAME = "name";


        private static HashMap<String, String> STUDENTS_PROJECTION_MAP;

        static final int NAPS = 1;
        static final int NAP_ID = 2;

        static final UriMatcher uriMatcher;
        static{
            uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
            uriMatcher.addURI(PROVIDER_NAME, "students", NAPS);
            uriMatcher.addURI(PROVIDER_NAME, "students/#", NAP_ID);
        }

        /**
         * Database specific constant declarations
         */

        private SQLiteDatabase db;
        public static final String DATABASE_NAME = "WIDGETDETAILS.db";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ALARMMUSICID = "alarmMusicID";
        public static final String COLUMN_NAPMUSICID = "napMusicID";
        public static final String COLUMN_TIME = "time";
        static final String TABLE_NAME = "widgetdata";
        static final int DATABASE_VERSION = 1;
        static final String CREATE_DB_TABLE =
                "create table "+TABLE_NAME+" " +
                        "(id integer primary key, name text, alarmMusicID integer ,napMusicID integer,time real)";

        /**
         * Helper class that actually creates and manages
         * the provider's underlying data repository.
         */
        private static class DatabaseHelper extends SQLiteOpenHelper {
            DatabaseHelper(Context context){
                super(context, DATABASE_NAME, null, DATABASE_VERSION);
            }

            @Override
            public void onCreate(SQLiteDatabase db) {
                db.execSQL(CREATE_DB_TABLE);
            }

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
                db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
                onCreate(db);
            }
        }

        @Override
        public boolean onCreate() {
            Context context = getContext();
            DatabaseHelper dbHelper = new DatabaseHelper(context);

            /**
             * Create a write able database which will trigger its
             * creation if it doesn't already exist.
             */

            db = dbHelper.getWritableDatabase();
            return (db == null)? false:true;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
            /**
             * Add a new student record
             */
            long rowID = db.insert(TABLE_NAME, "", values);

            /**
             * If record is added successfully
             */
            if (rowID > 0) {
                Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
                getContext().getContentResolver().notifyChange(_uri, null);
                return _uri;
            }

            throw new SQLException("Failed to add a record into " + uri);
        }

        @Override
        public Cursor query(Uri uri, String[] projection,
                            String selection,String[] selectionArgs, String sortOrder) {
            SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
            qb.setTables(TABLE_NAME);

            switch (uriMatcher.match(uri)) {
                case NAPS:
                    qb.setProjectionMap(STUDENTS_PROJECTION_MAP);
                    break;

                case NAP_ID:
                    qb.appendWhere( _ID + "=" + uri.getPathSegments().get(1));
                    break;

                default:
            }


            Cursor c = qb.query(db,	projection,	selection,
                    selectionArgs,null, null, sortOrder);
            /**
             * register to watch a content URI for changes
             */
            c.setNotificationUri(getContext().getContentResolver(), uri);
            return c;
        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)){
                case NAPS:
                    count = db.delete(TABLE_NAME, selection, selectionArgs);
                    break;

                case NAP_ID:
                    String id = uri.getPathSegments().get(1);
                    count = db.delete(TABLE_NAME, _ID +  " = " + id +
                                    (!TextUtils.isEmpty(selection) ?
                                            " AND (" + selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri);
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        @Override
        public int update(Uri uri, ContentValues values,
                          String selection, String[] selectionArgs) {
            int count = 0;
            switch (uriMatcher.match(uri)) {
                case NAPS:
                    count = db.update(TABLE_NAME, values, selection, selectionArgs);
                    break;

                case NAP_ID:
                    count = db.update(TABLE_NAME, values,
                            _ID + " = " + uri.getPathSegments().get(1) +
                                    (!TextUtils.isEmpty(selection) ?
                                            "AND (" +selection + ')' : ""), selectionArgs);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown URI " + uri );
            }

            getContext().getContentResolver().notifyChange(uri, null);
            return count;
        }

        @Override
        public String getType(Uri uri) {
            switch (uriMatcher.match(uri)){
                /**
                 * Get all student records
                 */
                case NAPS:
                    return "vnd.android.cursor.dir/vnd.com.karan.thenaptaker.napwidget.widgetdata";
                /**
                 * Get a particular student
                 */
                case NAP_ID:
                    return "vnd.android.cursor.item/vnd.com.karan.thenaptaker.napwidget.widgetdata";
                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }
    }