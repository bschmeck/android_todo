package com.s10r.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class EditItemActivity extends AppCompatActivity {
    int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        String itemText = getIntent().getStringExtra("itemText");
        this.pos = getIntent().getIntExtra("pos", -1);
        setText(itemText);
        registerHandlers();
    }

    private void registerHandlers() {
        final EditItemActivity self = this;
        EditText etDueDate = (EditText)findViewById(R.id.etDueDate);
        etDueDate.setFocusableInTouchMode(false);
        etDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                Dialog dialog = new DatePickerDialog(self, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        TextView etDueDate = (TextView) findViewById(R.id.etDueDate);
                        String dueDate = String.format("%d-%d-%d", year, month + 1, day);
                        etDueDate.setText(dueDate);
                    }
                }, year, month, day);
                dialog.show();
            }
        });
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

    public void onCancel(View v) {
        setResult(RESULT_CANCELED);
        this.finish();
    }
}
