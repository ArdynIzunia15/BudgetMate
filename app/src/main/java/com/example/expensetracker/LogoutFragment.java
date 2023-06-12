package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class LogoutFragment extends Fragment {
    ConstraintLayout containerLogout;
    SharedPreferences sharedPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_logout, container, false);
        containerLogout = view.findViewById(R.id.containerLogout);

        sharedPref = this.getActivity().getSharedPreferences("activeUserAccount", Context.MODE_PRIVATE);

        containerLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                @SuppressLint("CommitPrefEdits")
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.clear().apply();
                getActivity().finish();
            }
        });
        return view;
    }
}