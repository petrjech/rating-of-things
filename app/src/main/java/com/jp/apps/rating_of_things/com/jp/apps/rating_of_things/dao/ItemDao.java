package com.jp.apps.rating_of_things.com.jp.apps.rating_of_things.dao;

import com.jp.apps.rating_of_things.Item;

import java.util.List;

public interface ItemDao {

    long addItem(Item item);

    boolean updateItem(Item item);

    boolean deleteEvent(Item item);

    List<Item> getAllItems();
}
