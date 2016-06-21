package com.nwa.smartgym.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.interfaces.MusicPreferenceAPIInterface;
import com.nwa.smartgym.lib.adapters.MusicPreferenceAdapter;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreference extends AppCompatActivity {
    private ListView listView;
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
        MusicPreferenceAPIInterface musicPreferenceAPIInterface = new MusicPreferenceAPIInterface(this, musicPreferenceAdapter);
        musicPreferenceAPIInterface.listMusicPreferences();
        listView.setAdapter(musicPreferenceAdapter);
    }
}

