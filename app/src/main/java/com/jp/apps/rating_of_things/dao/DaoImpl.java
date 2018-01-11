package com.jp.apps.rating_of_things.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.Tag;
import com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper;

import java.util.ArrayList;
import java.util.List;

import static com.jp.apps.rating_of_things.utils.DatabaseSQLiteHelper.*;

public class DaoImpl implements Dao {

    private final DatabaseSQLiteHelper dbHelper;
    private String[] itemsColumns;
    private String[] tagsColumns;
    private String[] itemTagsColumns;

    public DaoImpl(Context context) {
        this.dbHelper = DatabaseSQLiteHelper.getInstance(context);
//        SQLiteDatabase db = dbHelper.getWritableDatabase();
//        dbHelper.onCreate(db);
        getItemsColumns();
        getTagsColumns();
        getItemTagsColumns();
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
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemsColumns, ITEMS_COLUMNS.NAME.name() + " = ?", new String[] {itemName}, null, null, null, "1");
        cursor.moveToFirst();
        boolean result = !cursor.isAfterLast();
        cursor.close();
        db.close();
        return result;
    }

    @Override
    public boolean tagExists(String tagName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.TAGS.name(), tagsColumns, TAGS_COLUMNS.NAME.name() + " = ?", new String[] {tagName}, null, null, null, "1");
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
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemsColumns, null, null, null, null, null, null);

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
        // TODO implement search including tags
        List<Item> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.ITEMS.name(), itemsColumns, ITEMS_COLUMNS.NAME.name() + " LIKE ?", new String[] {itemName + "%"}, null, null, null, null);
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
    public List<Tag> searchTags(String name) {
        List<Tag> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.TAGS.name(), tagsColumns, TAGS_COLUMNS.NAME.name() + " LIKE ?", new String[] {name + "%"}, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToTag(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return result;
    }

    @Override
    public List<Tag> getAllTags() {
        List<Tag> result = new ArrayList<>();

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(TABLES.TAGS.name(), tagsColumns, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToTag(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return result;
    }

    @Override
    public List<Tag> getItemTags(Item item) {
        List<Tag> result = new ArrayList<>();

        // SELECT ITEM_TAGS.TAG_ID, TAGS.NAME FROM ITEM_TAGS INNER JOIN TAGS ON ITEM_TAGS.TAG_ID = TAGS.ID
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(TABLES.ITEM_TAGS.name()
                + " INNER JOIN "
                + TABLES.TAGS.name()
                + " ON "
                + TABLES.ITEM_TAGS.name() + "." + ITEM_TAGS_COLUMNS.TAG_ID.name()
                + " = "
                + TABLES.TAGS.name() + "." + TAGS_COLUMNS.ID.name());
        queryBuilder.appendWhereEscapeString(ITEM_TAGS_COLUMNS.ITEM_ID.name() + " = " + item.getName());
        String[] columns = new String[] {TABLES.TAGS.name() + "." + TAGS_COLUMNS.ID.name(), TABLES.TAGS.name() + "." + TAGS_COLUMNS.NAME.name()};
        Cursor cursor = queryBuilder.query(db, columns, null, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            result.add(cursorToTag(cursor));
            cursor.moveToNext();
        }

        cursor.close();
        db.close();

        return result;
    }

    @Override
    public long createTag(Tag tag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEMS_COLUMNS.NAME.name(), tag.getName());
        return db.insert(TABLES.TAGS.name(), null, cv);
    }

    @Override
    public boolean addItemTag(Item item, Tag tag) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(ITEM_TAGS_COLUMNS.ITEM_ID.name(), item.getId());
        cv.put(ITEM_TAGS_COLUMNS.TAG_ID.name(), tag.getId());
        long id = db.insert(TABLES.ITEM_TAGS.name(), null, cv);
        return id >= 0;
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

    private Tag cursorToTag(Cursor cursor) {
        int index = cursor.getColumnIndexOrThrow(TAGS_COLUMNS.ID.name());
        long id = cursor.getLong(index);

        index = cursor.getColumnIndexOrThrow(ITEMS_COLUMNS.NAME.name());
        String name = cursor.getString(index);

        Tag tag = new Tag(name);
        tag.setId(id);
        return tag;
    }

    private void getItemsColumns() {
        List<String> columns = new ArrayList<>();
        for (ITEMS_COLUMNS column : ITEMS_COLUMNS.values()) {
            columns.add(column.name());
        }
        itemsColumns = new String[columns.size()];
        columns.toArray(itemsColumns);
    }

    private void getTagsColumns() {
        List<String> columns = new ArrayList<>();
        for (TAGS_COLUMNS column : TAGS_COLUMNS.values()) {
            columns.add(column.name());
        }
        tagsColumns = new String[columns.size()];
        columns.toArray(tagsColumns);
    }

    private void getItemTagsColumns() {
        List<String> columns = new ArrayList<>();
        for (ITEM_TAGS_COLUMNS column : ITEM_TAGS_COLUMNS.values()) {
            columns.add(column.name());
        }
        itemTagsColumns = new String[columns.size()];
        columns.toArray(itemTagsColumns);
    }
}
