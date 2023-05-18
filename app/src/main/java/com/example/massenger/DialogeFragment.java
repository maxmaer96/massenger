package com.example.massenger;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DialogeFragment extends Fragment {
TextView noUserText;
String toWho;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        toWho = bundle.getString("user"); //принимаем друга которому юзер захотел написать
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialoge, container, false);
        noUserText=view.findViewById(R.id.noUserText);
        noUserText.setVisibility(View.GONE);
        if(toWho!=null||!toWho.isEmpty()){

        }
        return view;
    }
}