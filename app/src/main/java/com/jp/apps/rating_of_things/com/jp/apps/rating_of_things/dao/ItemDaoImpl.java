package com.jp.apps.rating_of_things.com.jp.apps.rating_of_things.dao;

import android.content.Context;

import com.jp.apps.rating_of_things.com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper;
import com.jp.apps.rating_of_things.Item;

import java.util.List;

public class ItemDaoImpl implements ItemDao {

    private final DatabaseSQLiteHelper dbHelper;

    public ItemDaoImpl(Context context) {
        this.dbHelper = DatabaseSQLiteHelper.getInstance(context);
    }

    @Override
    public long addItem(Item item) {
        return 0;
    }

    @Override
    public boolean updateItem(Item item) {
        return false;
    }

    @Override
    public boolean deleteEvent(Item item) {
        return false;
    }

    @Override
    public List<Item> getAllItems() {
        return null;
    }
}
