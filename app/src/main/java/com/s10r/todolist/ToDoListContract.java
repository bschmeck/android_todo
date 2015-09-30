package com.s10r.todolist;

import android.provider.BaseColumns;

/**
 * Created by bschmeckpeper on 9/29/15.
 */
public final class ToDoListContract {
    public ToDoListContract() {}

    public static abstract class ItemEntry implements BaseColumns {
        public static final String TABLE_NAME = "item";
        public static final String COLUMN_NAME_ITEM_ID = "itemid";
        public static final String COLUMN_NAME_ITEM_TEXT = "itemtext";
        public static final String COLUMN_NAME_COMPLETED_AT = "completed_at";
        public static final String COLUMN_NAME_DUE_AT = "due_at";
    }
}
