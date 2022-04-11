package com.example.project.Map;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.project.R;

public class RecordFragment extends Fragment {
    ImageButton helpBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.map_record_fragment, container, false);
        helpBtn = view.findViewById(R.id.kmHelpBtn);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void onClick(View map) {
                DetailBottomFragment helpFragment = new DetailBottomFragment(getActivity().getApplicationContext());
                helpFragment.show(getActivity().getSupportFragmentManager(), helpFragment.getTag());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
