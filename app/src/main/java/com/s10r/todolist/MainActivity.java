package com.s10r.todolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Item> items;
    ArrayAdapter<Item> itemsAdapter;
    ListView lvItems;
    ToDoListDbHelper mDbHelper;

    private final int EDIT_ITEM_REQUEST_CODE = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvItems = (ListView)findViewById(R.id.lvItems);
        mDbHelper = new ToDoListDbHelper(this.getApplicationContext());
        readItems();
        itemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        setupListViewListener();
    }

    public void onAddItem(View v) {
        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
        String itemText = etNewItem.getText().toString();
        Item item = persistNewItem(itemText);
        itemsAdapter.add(item);
        etNewItem.setText("");
    }

    private void setupListViewListener() {
        lvItems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        Item itm = items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        return removeItem(itm);
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        String itemText = items.get(pos).toString();
                        launchEditView(itemText, pos);
                    }
                }
        );
    }

    public void launchEditView(String itemText, int pos) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("itemText", itemText);
        i.putExtra("pos", pos);
        startActivityForResult(i, EDIT_ITEM_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
            String itemText = data.getStringExtra("itemText");
            // TODO: Handle invalid pos
            int pos = data.getIntExtra("pos", -1);
            Item item = items.get(pos);
            item.text = itemText;
            persistItem(item);
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void readItems() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String[] projection = {
                ToDoListContract.ItemEntry._ID,
                ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT
        };
        Cursor c = db.query(
                ToDoListContract.ItemEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        items = new ArrayList<Item>();
        c.moveToFirst();
        int indexId = c.getColumnIndexOrThrow(ToDoListContract.ItemEntry._ID);
        int indexText = c.getColumnIndexOrThrow(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT);
        while (!c.isAfterLast()) {
            long id = c.getLong(indexId);
            String text = c.getString(indexText);
            items.add(new Item(id, text));
            c.moveToNext();
        }
    }

    private void persistItem(Item item) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT, item.text);

        String selection = ToDoListContract.ItemEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(item.id) };

        int count = db.update(ToDoListContract.ItemEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private Item persistNewItem(String itemText) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ToDoListContract.ItemEntry.COLUMN_NAME_ITEM_TEXT, itemText);

        long id = db.insert(ToDoListContract.ItemEntry.TABLE_NAME, null, values);
        return new Item(id, itemText);
    }

    private boolean removeItem(Item item) {
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
