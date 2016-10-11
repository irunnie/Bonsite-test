package com.application.closed.testapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;


public class MyProvider extends ContentProvider {


    static final String DB_NAME = "history";
    static final byte DB_VERSION = 1;

    static final String LINK_TABLE = "links";

    static final String LINK_ID = "_id";
    static final String LINK_STATUS = "status";
    static final String LINK_REF = "ref";
    static final String LINK_TIME = "time";

    static final String DB_CREATE = "create table " + LINK_TABLE + " ("
            + LINK_ID + " integer primary key autoincrement, "
            + LINK_STATUS + " integer, " + LINK_REF + " text, "
            + LINK_TIME + " datetime default current_timestamp" + ");";

    static final String AUTHORITY = "com.application.provider.LinkProvider";
    static final String CONTACT_PATH = "links";

    public static final Uri CONTACT_CONTENT_URI = Uri.parse("content://"
            + AUTHORITY + "/" + CONTACT_PATH);

    static final String CONTACT_CONTENT_TYPE = "vnd.android.cursor.dir/vnd."
            + AUTHORITY + "." + CONTACT_PATH;

    static final String CONTACT_CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd."
            + AUTHORITY + "." + CONTACT_PATH;

    static final int URI_CONTACTS = 1;
    static final int URI_CONTACTS_ID = 2;

    private static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH, URI_CONTACTS);
        uriMatcher.addURI(AUTHORITY, CONTACT_PATH + "/#", URI_CONTACTS_ID);
    }

    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = LINK_STATUS + " DESC";
                }
                break;
            case URI_CONTACTS_ID: // Uri с ID
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }

        database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(LINK_TABLE, projection, selection,
                selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(),
                CONTACT_CONTENT_URI);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                return CONTACT_CONTENT_TYPE;
            case URI_CONTACTS_ID:
                return CONTACT_CONTENT_ITEM_TYPE;
        }
        return null;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != URI_CONTACTS)
        throw new IllegalArgumentException("Wrong URI: " + uri);

        database = dbHelper.getWritableDatabase();
        long rowID = database.insert(LINK_TABLE, null, values);
        Uri resultUri = ContentUris.withAppendedId(CONTACT_CONTENT_URI, rowID);
        getContext().getContentResolver().notifyChange(resultUri, null);
        return resultUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:
                break;
            case URI_CONTACTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        database = dbHelper.getWritableDatabase();
        int cnt = database.delete(LINK_TABLE, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        switch (uriMatcher.match(uri)) {
            case URI_CONTACTS:

                break;
            case URI_CONTACTS_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    selection = LINK_ID + " = " + id;
                } else {
                    selection = selection + " AND " + LINK_ID + " = " + id;
                }
                break;
            default:
                throw new IllegalArgumentException("Wrong URI: " + uri);
        }
        database = dbHelper.getWritableDatabase();
        int cnt = database.update(LINK_TABLE, values, selection, selectionArgs);
        getContext().getContentResolver().notifyChange(uri, null);
        return cnt;
    }

    private class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, DB_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }
}
