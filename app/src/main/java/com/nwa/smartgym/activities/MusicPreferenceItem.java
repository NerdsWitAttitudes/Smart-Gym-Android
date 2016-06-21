package com.nwa.smartgym.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.MusicPreferenceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.SportScheduleAPI;
import com.nwa.smartgym.api.interfaces.MusicPreferenceAPIInterface;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.MusicPreferenceAdapter;
import com.nwa.smartgym.models.*;
import com.nwa.smartgym.models.MusicPreference;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreferenceItem extends AppCompatActivity {
    private com.nwa.smartgym.models.MusicPreference musicPreference;
    private SecretsHelper secretsHelper;
    private Spinner spGenres;
    private MusicPreferenceAPIInterface musicPreferenceService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        musicPreference = (MusicPreference) getIntent().getSerializableExtra("MusicPreference");
        setContentView(R.layout.activity_music_preference_item);
        spGenres = (Spinner) findViewById(R.id.spGenres);
        List<String> list = new ArrayList<String>();
        list.add("list 1");
        list.add("list 2");
        list.add("list 3");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spGenres.setAdapter(dataAdapter);
        MusicPreferenceAdapter musicPreferenceAdapter = new MusicPreferenceAdapter(this);
        musicPreferenceService = new MusicPreferenceAPIInterface(this, musicPreferenceAdapter);

        Button save = (Button) findViewById(R.id.btn_save_music_preference);
        if (save != null) {
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("saving");
                    musicPreferenceService.post(new MusicPreference(
                            spGenres.getSelectedItem().toString()
                    ));
                }
            });
        }
    }


}
