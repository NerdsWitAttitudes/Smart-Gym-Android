package com.nwa.smartgym.lib;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.nwa.smartgym.R;
import com.nwa.smartgym.models.Buddy;
import com.nwa.smartgym.models.Device;
import com.nwa.smartgym.models.User;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by robin on 25-5-16.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {
    private static final String DATABASE_NAME = "smartgym.db";
    private static final int DATABASE_VERSION = 2;
    private static final Class[] TABLES = {
    Device.class,
    Buddy.class,
    User.class
    };

    private Dao<Buddy, UUID> buddyDao;
    private Dao<Device, UUID> deviceDao;
    private Dao<User, UUID> userDao;
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public Dao<Device, UUID> getDeviceDao() throws SQLException {
        if (deviceDao == null) {
            deviceDao = getDao(Device.class);
        }
        return deviceDao;
    }

    public Dao<Buddy, UUID> getBuddyDao() throws SQLException {
        if (buddyDao == null) {
            buddyDao = getDao(Buddy.class);
        }

        return buddyDao;
    }
    public Dao<User, UUID> getUserDao() throws SQLException {
        if (userDao == null) {
            userDao = getDao(User.class);
        }

        return userDao;
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            for (Class table : TABLES) {
                TableUtils.createTable(connectionSource, table);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Error creating the database", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource,
                          int oldVersion, int newVersion) {
        try {
            if (newVersion == 2 && oldVersion == 1) {
                TableUtils.createTable(connectionSource, Buddy.class);
                TableUtils.createTable(connectionSource, User.class);
            }
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Error updating version " +
                    oldVersion + " to version " + newVersion);
        }
    }

    public void clearAll() {
        for (Class table : TABLES) {
            try {
                TableUtils.clearTable(getConnectionSource(), table);
            } catch (SQLException e) {
                Log.e(getClass().getName(), "Failure clearing table", e);
            }
        }
    }
}
