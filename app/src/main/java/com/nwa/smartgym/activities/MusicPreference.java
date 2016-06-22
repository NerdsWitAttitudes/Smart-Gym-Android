package com.nwa.smartgym.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.MusicPreferenceAPIInterface;
import com.nwa.smartgym.lib.adapters.MusicPreferenceAdapter;

import java.io.Serializable;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreference extends AppCompatActivity {
    private ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list_music_preferences);
        listView = (ListView) findViewById(R.id.listView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_music_preference);
        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MusicPreference.this, MusicPreferenceItem.class));
                }
            });
        }

        MusicPreferenceAdapter musicPreferenceAdapter = new MusicPreferenceAdapter(this);
        final MusicPreferenceAPIInterface musicPreferenceAPIInterface = new MusicPreferenceAPIInterface(this, musicPreferenceAdapter);

        listView.setAdapter(musicPreferenceAdapter);
        musicPreferenceAdapter.clear();
        musicPreferenceAPIInterface.listMusicPreferences();
        musicPreferenceAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final com.nwa.smartgym.models.MusicPreference musicPreference = (com.nwa.smartgym.models.MusicPreference) parent.getItemAtPosition(position);
                AlertDialog.Builder alert = new AlertDialog.Builder(MusicPreference.this);
                alert.setTitle("Delete..");
                alert.setMessage("Are you sure you want to remove " + musicPreference.getGenre());
                alert.setNegativeButton("Cancel", null);
                alert.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        musicPreferenceAPIInterface.delete(musicPreference);
                    }
                });
                alert.show();

            }
        });
    }
}

