package com.s10r.todolist;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bschmeckpeper on 9/29/15.
 */
public class Item {
    private ToDoListDbHelper dbHelper;
    public long id = -1;
    public String text;
    public Date dueDate;

    public static String[] COLUMN_PROJECTION = {
        ToDoListContract.ItemEntry._ID,
        ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT,
        ToDoListContract.ItemEntry.COLUMN_NAME_DUE_AT
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
            int indexDueDate = cursor.getColumnIndexOrThrow(ToDoListContract.ItemEntry.COLUMN_NAME_DUE_AT);
            this.id = cursor.getLong(indexId);
            this.text = cursor.getString(indexText);
            this.dueDate = parseDate(cursor.getString(indexDueDate));
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

        this.id = db.insert(ToDoListContract.ItemEntry.TABLE_NAME, null, contentValues());
    }

    private void update() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selection = ToDoListContract.ItemEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(id) };

        db.update(ToDoListContract.ItemEntry.TABLE_NAME, contentValues(), selection, selectionArgs);
    }

    private ContentValues contentValues() {
        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT, text);
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_DUE_AT, formatDate(dueDate));

        return values;
    }

    private String formatDate(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd HH:mm");
        return formatter.format(date);
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null) {
            return null;
        }

        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-M-dd HH:mm");
        return formatter.parse(dateStr, pos);
    }

    public String toString() {
        return text;
    }
}
