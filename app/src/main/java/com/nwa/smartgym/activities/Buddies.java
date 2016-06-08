package com.nwa.smartgym.activities;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nwa.smartgym.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Buddies.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Buddies#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Buddies extends Fragment {

    public Buddies() {
        // Required empty public constructor
    }

    public static Buddies newInstance() {
        Buddies fragment = new Buddies();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_buddies, container, false);
    }
}
