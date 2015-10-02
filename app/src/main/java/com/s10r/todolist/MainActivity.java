package com.s10r.todolist;

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

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
        Item item = new Item(mDbHelper, itemText);
        item.save();
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
                        itm.complete();
                        items.add(itm);
                        itemsAdapter.notifyDataSetChanged();
                        return true;
                    }
                }
        );
        lvItems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapter,
                                            View item, int pos, long id) {
                        Item itm = items.get(pos);
                        if (!itm.isCompleted()) {
                            launchEditView(pos, itm);
                        }
                    }
                }
        );
    }

    public void launchEditView(int pos, Item item) {
        Intent i = new Intent(MainActivity.this, EditItemActivity.class);
        i.putExtra("itemText", item.text);
        i.putExtra("pos", pos);
        i.putExtra("dueDate", formatDate(item.dueDate));
        startActivityForResult(i, EDIT_ITEM_REQUEST_CODE);
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
        Date date = formatter.parse(dateStr, pos);
        if (date != null) {
            return date;
        }
        formatter = new SimpleDateFormat("yyyy-M-dd");
        return formatter.parse(dateStr, pos);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == EDIT_ITEM_REQUEST_CODE) {
            String itemText = data.getStringExtra("itemText");
            String dueDate = data.getStringExtra("dueDate");
            // TODO: Handle invalid pos
            int pos = data.getIntExtra("pos", -1);
            Item item = items.get(pos);
            item.text = itemText;
            item.dueDate = parseDate(dueDate);
            item.save();
            itemsAdapter.notifyDataSetChanged();
        }
    }

    private void readItems() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor c = db.query(
                ToDoListContract.ItemEntry.TABLE_NAME,
                Item.COLUMN_PROJECTION,
                null,
                null,
                null,
                null,
                ToDoListContract.ItemEntry.COLUMN_NAME_COMPLETED_AT
        );
        items = new ArrayList<Item>();
        c.moveToFirst();
        while (!c.isAfterLast()) {
            items.add(new Item(mDbHelper, c));
            c.moveToNext();
        }
        c.close();
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
