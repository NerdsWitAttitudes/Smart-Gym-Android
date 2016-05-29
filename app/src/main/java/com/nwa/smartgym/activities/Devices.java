package com.nwa.smartgym.activities;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OrmLiteBaseListActivity;
import com.j256.ormlite.android.apptools.OrmLiteCursorAdapter;
import com.j256.ormlite.android.apptools.OrmLiteCursorLoader;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.DeviceAPI;
import com.nwa.smartgym.api.DeviceAPIInterface;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.lib.DatabaseHelper;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.Device;
import com.nwa.smartgym.models.HTTPResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Devices extends OrmLiteBaseListActivity<DatabaseHelper>{

    private Context context;
    private OrmLiteCursorAdapter<Device, RelativeLayout> viewAdapter;
    private LayoutInflater layoutInflater;

    private Dao<Device, Integer> deviceDao;
    private PreparedQuery<Device> preparedListQuery;
    private DeviceAPIInterface deviceAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        context = this;
        layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        setFabListener();

        try {
            deviceDao = getHelper().getDeviceDao();
            prepareQueries();
        } catch( SQLException e) {
            Log.e(this.getLocalClassName(), "Unable to access database", e);
        }

        deviceAPIInterface = new DeviceAPIInterface(context, deviceDao);

        viewAdapter = new OrmLiteCursorAdapter<Device, RelativeLayout>(context) {
            @Override
            public void bindView(RelativeLayout relativeLayout, Context context, final Device device) {
                TextView title = (TextView) relativeLayout.findViewById(R.id.title_device_list_item);
                TextView subTitle = (TextView) relativeLayout.findViewById(R.id.subtitle_device_list_item);
                ImageView delete = (ImageView) relativeLayout.findViewById(R.id.delete_device_list_item);

                title.setText(device.getName());
                subTitle.setText(device.getDeviceAddress());
                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deviceAPIInterface.delete(device);
                    }
                });

            }

            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                RelativeLayout relativeLayout = (RelativeLayout) View.inflate(context, R.layout.device_list_item, null);
                return relativeLayout;
            }
        };

        setListAdapter(viewAdapter);

        getLoaderManager().initLoader(0, null, new LoaderManager.LoaderCallbacks<Cursor>() {
            @Override
            public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
                deviceAPIInterface.list();
                return new OrmLiteCursorLoader<Device>(context, deviceDao, preparedListQuery);
            }

            @Override
            public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
                viewAdapter.changeCursor(cursor, ((OrmLiteCursorLoader<Device>) loader).getQuery());
            }

            @Override
            public void onLoaderReset(Loader<Cursor> loader) {
                viewAdapter.changeCursor(null,null);
            }
        });
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
