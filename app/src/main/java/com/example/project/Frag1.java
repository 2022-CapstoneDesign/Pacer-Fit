package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag1 extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.frag1,container,false);

        ImageButton Km_button = (ImageButton) v.findViewById(R.id.Km_button);
        Km_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View map) {
                Intent intent = new Intent(getActivity(),Map_add.class); //Fragment -> Activity로 이동 (Map_add.java)
                startActivity(intent);
            }
        });

        return v;

    }
}