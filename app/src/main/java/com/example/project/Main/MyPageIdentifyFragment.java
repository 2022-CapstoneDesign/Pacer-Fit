package com.example.project.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.project.R;
import com.google.android.material.textfield.TextInputLayout;

public class MyPageIdentifyFragment extends Fragment {
    TextInputLayout pwTextInputLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.main_my_page_identify_fragment, container, false);

        LinearLayout ll = v.findViewById(R.id.linearLayout);

        YoYo.with(Techniques.Tada)
                .duration(700)
                .repeat(5)
                .playOn(ll);


        return v;
    }
}