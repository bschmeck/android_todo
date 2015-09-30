package com.s10r.todolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by bschmeckpeper on 9/29/15.
 */
public class ToDoListDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "ToDoList.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ITEMS =
            "CREATE TABLE " + ToDoListContract.ItemEntry.TABLE_NAME + " (" +
                    ToDoListContract.ItemEntry._ID + " INTEGER PRIMARY KEY," +
                    ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_ID + TEXT_TYPE + COMMA_SEP +
                    ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT + TEXT_TYPE + COMMA_SEP +
                    ToDoListContract.ItemEntry.COLUMN_NAME_COMPLETED_AT + TEXT_TYPE + COMMA_SEP +
                    ToDoListContract.ItemEntry.COLUMN_NAME_DUE_AT + TEXT_TYPE +
            " )";
    private static final String SQL_DELETE_ITEMS =
            "DROP TABLE IF EXISTS " + ToDoListContract.ItemEntry.TABLE_NAME;

    public ToDoListDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ITEMS);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ITEMS);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
