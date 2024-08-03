package com.example.ibadat;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class UserDataFragment extends Fragment {

    private static final String ARG_USER_DATA = "user_data";
    private String userData;

    public static UserDataFragment newInstance(String userData) {
        UserDataFragment fragment = new UserDataFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_DATA, userData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            userData = getArguments().getString(ARG_USER_DATA);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_data, container, false);
        TextView userDataTextView = view.findViewById(R.id.userDataTextView);
        userDataTextView.setText(userData);
        return view;
    }
}
