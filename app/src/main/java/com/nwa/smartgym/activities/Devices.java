package com.nwa.smartgym.activities;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.android.apptools.OrmLiteCursorAdapter;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.DeviceAPIInterface;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.MessageHelper;
import com.nwa.smartgym.lib.adapters.DeviceAdapter;
import com.nwa.smartgym.models.Device;

import java.sql.SQLException;
import java.util.UUID;

public class Devices extends OrmLiteBaseListActivity<DatabaseHelper>{

    private Context context;
    private OrmLiteCursorAdapter<Device, RelativeLayout> viewAdapter;

    private Dao<Device, UUID> deviceDao;
    private PreparedQuery<Device> preparedListQuery;
    private DeviceAPIInterface deviceAPIInterface;

    private Snackbar noneFoundSnackbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        noneFoundSnackbar = MessageHelper.getIndefiniteSnackbar(getListView(), getString(R.string.no_devices));
        context = this;
        setFabListener();

        try {
            deviceDao = getHelper().getDeviceDao();
            prepareQueries();
        } catch( SQLException e) {
            Log.e(this.getLocalClassName(), "Unable to access database", e);
        }

        deviceAPIInterface = new DeviceAPIInterface(context, deviceDao);
        viewAdapter = new DeviceAdapter(context, deviceAPIInterface);

        setListAdapter(viewAdapter);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                deviceAPIInterface.list();
                return new OrmLiteCursorLoader<>(context, deviceDao, preparedListQuery);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                viewAdapter.changeCursor(cursor, ((OrmLiteCursorLoader<Device>) loader).getQuery());
                if (viewAdapter.getCount() == 0) {
                    noneFoundSnackbar.show();
                } else {
                    noneFoundSnackbar.dismiss();
                }
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                viewAdapter.changeCursor(null,null);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewAdapter.notifyDataSetChanged();
    }

    private void prepareQueries() throws SQLException {
        preparedListQuery = deviceDao.queryBuilder().prepare();
    }

    private void setFabListener() {
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_device);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AddDeviceActivity.class);
                startActivity(intent);
            }
        });
    }
}
