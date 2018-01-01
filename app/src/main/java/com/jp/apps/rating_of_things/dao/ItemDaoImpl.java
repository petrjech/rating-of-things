package com.jp.apps.rating_of_things.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import static com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper.ITEMS_COLUMNS;
import static com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper.TABLES;

public class ItemDaoImpl implements ItemDao {

    private final DatabaseSQLiteHelper dbHelper;
    private String[] itemColumns;

    public ItemDaoImpl(Context context) {
        this.dbHelper = DatabaseSQLiteHelper.getInstance(context);
        getItemColumns();
    }

    @Override
    public long createItem(Item item) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEMS_COLUMNS.NAME.name(), item.getName());
        cv.put(ITEMS_COLUMNS.DESCRIPTION.name(), item.getDescription());
        return db.insert(TABLES.ITEMS.name(), null, cv);
    }

    @Override
    public boolean updateItem(Item item) {
        return false;
    }

    @Override
    public boolean deleteItem(Item item) {
        return false;
    }

    @Override
    public boolean itemExists(String itemName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemColumns, "name = ?", new String[] {itemName}, null, null, null, "1");
        cursor.moveToFirst();
        boolean result = !cursor.isAfterLast();
        cursor.close();
        db.close();
        return result;
    }

    @Override
    public List<Item> getAllItems() {
        List<Item> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemColumns, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToItem(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return result;
    }

    @Override
    public List<Item> searchItems(String itemName, List<String> includingTags, List<String> excludingTags) {
        // TODO implement
        List<Item> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemColumns, "name LIKE ?", new String[] {itemName + "%"}, null, null, null, "1");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToItem(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return result;
    }

    private Item cursorToItem(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(ITEMS_COLUMNS.ID.name());
        long id = cursor.getLong(index);

        index = cursor.getColumnIndexOrThrow(ITEMS_COLUMNS.NAME.name());
        String name = cursor.getString(index);

        index = cursor.getColumnIndexOrThrow(ITEMS_COLUMNS.DESCRIPTION.name());
        String description = cursor.getString(index);

        Item item = new Item(name);
        item.setId(id);
        item.setDescription(description);
        return item;
    }

    private void getItemColumns() {
        List<String> columns = new ArrayList<>();
        for (ITEMS_COLUMNS column : ITEMS_COLUMNS.values()) {
            columns.add(column.name());
        }
        itemColumns = new String[columns.size()];
        columns.toArray(itemColumns);
    }
}
