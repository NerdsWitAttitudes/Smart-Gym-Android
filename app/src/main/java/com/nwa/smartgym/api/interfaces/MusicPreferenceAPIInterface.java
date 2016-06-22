package com.nwa.smartgym.api.interfaces;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.activities.SportScheduleItem;
import com.nwa.smartgym.api.BusynessAPI;
import com.nwa.smartgym.api.MusicPreferenceAPI;
import com.nwa.smartgym.api.ServiceGenerator;
import com.nwa.smartgym.api.UserAPI;
import com.nwa.smartgym.api.callbacks.Callback;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.lib.adapters.MusicPreferenceAdapter;
import com.nwa.smartgym.lib.adapters.SportScheduleAdapter;
import com.nwa.smartgym.models.MusicPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreferenceAPIInterface {
    private Context context;
    private MusicPreferenceAdapter musicPreferenceAdapter;
    private MusicPreferenceAPI musicPreferenceService;

    public MusicPreferenceAPIInterface(Context context, MusicPreferenceAdapter musicPreferenceAdapter) {
        this.context = context;
        this.musicPreferenceAdapter = musicPreferenceAdapter;
        SecretsHelper secretsHelper = new SecretsHelper(context);
        this.musicPreferenceService = ServiceGenerator.createSmartGymService(MusicPreferenceAPI.class,
                secretsHelper.getAuthToken());
    }

    public void post(MusicPreference musicPreference) {
        Call<MusicPreference> call = musicPreferenceService.createMusicPreference(musicPreference);
        call.enqueue(new Callback<MusicPreference>(context) {
            @Override
            public void onResponse(Call<MusicPreference> call, Response<MusicPreference> response) {
                super.onResponse(call, response);

            }
        });
    }

    public void delete() {
        Call<MusicPreference> call = musicPreferenceService.deleteMusicPreference();
        call.enqueue(new Callback<MusicPreference>(context) {
            @Override
            public void onResponse(Call<MusicPreference> call, Response<MusicPreference> response) {
                super.onResponse(call, response);

            }
        });
    }

    public void listMusicPreferences() {
        if (musicPreferenceAdapter == null) {
            System.out.println("no adapter");
            return;
        }

        Call<List<MusicPreference>> call = this.musicPreferenceService.listMusicPreference();

        call.enqueue(new Callback<List<MusicPreference>>(context) {
            @Override
            public void onResponse(Call<List<MusicPreference>> call, Response<List<MusicPreference>> response) {
                super.onResponse(call, response);
                if (response.code() == 200) {
                    for (MusicPreference musicPreference : response.body()) {
                        musicPreferenceAdapter.add(musicPreference);
                    }
                }

                musicPreferenceAdapter.notifyDataSetChanged();
            }
        });
    }


}
