package com.s10r.todolist;

/**
 * Created by bschmeckpeper on 9/29/15.
 */
public class Item {
    public long id;
    public String text;

    public Item(long id, String text) {
        this.id = id;
        this.text = text;
    }

    public String toString() {
        return text;
    }
}
