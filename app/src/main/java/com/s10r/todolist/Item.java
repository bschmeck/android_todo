package com.s10r.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by bschmeckpeper on 9/29/15.
 */
public class Item {
    private ToDoListDbHelper dbHelper;
    public long id = -1;
    public String text;

    public static String[] COLUMN_PROJECTION = {
        ToDoListContract.ItemEntry._ID,
        ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT
    };

    public Item(ToDoListDbHelper dbHelper, String text) {
        this.dbHelper = dbHelper;
        this.text = text;
    }

    public Item(ToDoListDbHelper dbHelper, Cursor cursor) {
        this.dbHelper = dbHelper;

        if (cursor != null) {
            int indexId = cursor.getColumnIndexOrThrow(ToDoListContract.ItemEntry._ID);
            int indexText = cursor.getColumnIndexOrThrow(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT);
            this.id = cursor.getLong(indexId);
            this.text = cursor.getString(indexText);
        }
    }

    public void save() {
        if (isNewRecord()) {
            insert();
        } else {
            update();
        }
    }

    public void delete() {
        if (isNewRecord()) {
            return;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = ToDoListContract.ItemEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };
        db.delete(ToDoListContract.ItemEntry.TABLE_NAME, selection, selectionArgs);
    }

    private boolean isNewRecord() {
        return id == -1;
    }

    private void insert() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT, text);

        this.id = db.insert(ToDoListContract.ItemEntry.TABLE_NAME, null, values);
    }

    private void update() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT, text);

        String selection = ToDoListContract.ItemEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(ToDoListContract.ItemEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public String toString() {
        return text;
    }
}
