package com.jp.apps.rating_of_things;

import android.content.Context;

import com.jp.apps.rating_of_things.dao.ItemDao;
import com.jp.apps.rating_of_things.dao.ItemDaoImpl;

public class Config {

    public static ItemDao getDefaultItemDao(Context context) {
        return new ItemDaoImpl(context);
    }
}
