package com.jp.apps.rating_of_things.dao;

import com.jp.apps.rating_of_things.Item;
import com.jp.apps.rating_of_things.Tag;

import java.util.List;

public interface Dao {

    long createItem(Item item);

    boolean updateItem(Item item);

    boolean deleteItem(Item item);

    boolean itemExists(String itemName);

    boolean tagExists(String tagName);

    List<Item> searchItems(String name, List<String> includingTags, List<String> excludingTags);

    List<Tag> searchTags(String name);

    List<Item> getAllItems();

    List<Tag> getAllTags();

    List<Tag> getItemTags(Item item);

    boolean addItemTag(Item item, Tag tag);

    boolean deleteItemTag(Item item, Tag tag);

    long createTag(Tag tag);
}
