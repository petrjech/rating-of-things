package com.jp.apps.rating_of_things;

import android.content.Context;

import com.jp.apps.rating_of_things.dao.Dao;
import com.jp.apps.rating_of_things.dao.DaoImpl;

public class Config {

    private static Dao defaultDao;

    public static Dao initializeDefaultDao(Context applicationContext) {
        defaultDao = new DaoImpl(applicationContext);
        return defaultDao;
    }

    public static Dao getDefaultDao() {
        if (defaultDao == null) {
            throw new RuntimeException("Default DAO not initialized.");
        }
        return defaultDao;
    }
}
