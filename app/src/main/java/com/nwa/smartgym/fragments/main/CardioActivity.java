package com.nwa.smartgym.fragments.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nwa.smartgym.R;
import com.nwa.smartgym.lib.DefaultPageAdapter;

public class CardioActivity extends Fragment {

    public static CardioActivity newInstance() {
        CardioActivity cardioActivity = new CardioActivity();
        Bundle bundle = new Bundle();
        bundle.putString(DefaultPageAdapter.TAB_NAME, "Cardio progression");
        cardioActivity.setArguments(bundle);
        return cardioActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cardio_activity, container, false);

        return rootView;
    }
}
