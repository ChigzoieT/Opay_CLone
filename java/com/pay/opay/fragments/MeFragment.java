package com.pay.opay.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.pay.opay.R;

public class MeFragment extends Fragment {

    private static final String ARG_TEXT = "text";

    public static MeFragment newInstance(String text) {
        MeFragment fragment = new MeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TEXT, text);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
//        TextView textView = view.findViewById(R.id.text_profile);
//        if (getArguments() != null) {
//            textView.setText(getArguments().getString(ARG_TEXT));
//        }
        return view;
    }
}