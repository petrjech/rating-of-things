package com.jp.apps.rating_of_things.dao;

import com.jp.apps.rating_of_things.Item;

import java.util.List;

public interface ItemDao {

    long createItem(Item item);

    boolean updateItem(Item item);

    boolean deleteItem(Item item);

    boolean itemExists(String itemName);

    List<Item> searchItems(String name, List<String> includingTags, List<String> excludingTags);

    List<Item> getAllItems();
}
