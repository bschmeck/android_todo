package com.s10r.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

public class EditItemActivity extends AppCompatActivity {
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra("itemText");
        this.pos = getIntent().getIntExtra("pos", -1);
        setText(itemText);
    }

    private void setText(String text) {
        EditText etItemText = (EditText)findViewById(R.id.etItemText);
        etItemText.setText(text);
        etItemText.requestFocus();
        etItemText.setSelection(text.length());
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_item, menu);
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

    public void onSave(View v) {
        EditText etItemText = (EditText)findViewById(R.id.etItemText);
        String itemText = etItemText.getText().toString();
        Intent data = new Intent();
        data.putExtra("itemText", itemText);
        data.putExtra("pos", this.pos);
        setResult(RESULT_OK, data);
        this.finish();
    }
}
