package com.nwa.smartgym.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.api.interfaces.BuddyAPIInterface;
import com.nwa.smartgym.api.interfaces.MusicPreferenceAPIInterface;
import com.nwa.smartgym.api.interfaces.UserAPIInterface;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.SportScheduleAdapter;
import com.nwa.smartgym.lib.adapters.UserAdapter;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreference extends AppCompatActivity {

    private ListView listView;
    private MusicPreferenceAPIInterface musicPreferenceAPIInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_music_preferences);

        musicPreferenceAPIInterface = new MusicPreferenceAPIInterface(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_music_preference);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MusicPreference.this, MusicPreferenceItem.class));
                }
            });
        }

        showMusicPreferences();
    }

    private void showMusicPreferences() {
        ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.loading), getString(R.string.sport_schedule_loading));

        musicPreferenceAPIInterface.list();

        progressDialog.dismiss();
    }

}

