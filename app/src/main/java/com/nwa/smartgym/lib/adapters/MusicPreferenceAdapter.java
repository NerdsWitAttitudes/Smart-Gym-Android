package com.nwa.smartgym.lib.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nwa.smartgym.R;
import com.nwa.smartgym.api.MusicPreferenceAPI;
import com.nwa.smartgym.api.interfaces.MusicPreferenceAPIInterface;
import com.nwa.smartgym.lib.SecretsHelper;
import com.nwa.smartgym.models.MusicPreference;

import java.util.List;
import java.util.UUID;

/**
 * Created by rikvanderwerf on 11-6-16.
 */
public class MusicPreferenceAdapter extends ArrayAdapter<MusicPreference> {

    MusicPreferenceAPIInterface musicPreferenceAPIInterface;

    public MusicPreferenceAdapter(Context context) {
        super(context, 0);
        musicPreferenceAPIInterface = new MusicPreferenceAPIInterface(super.getContext(), this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        final MusicPreference musicPreference = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listview_music_preference, parent, false);
        }

        TextView tvName = (TextView) convertView.findViewById(R.id.tvMusicPreferenceTitle);
        tvName.setText(musicPreference.getGenre());
        return convertView;
    }




}
