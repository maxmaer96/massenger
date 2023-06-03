package com.example.massenger;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class AnotherUserProfile extends Fragment implements OnBackPressedListener{
    private User person;
    TextView email, username, about, age;
    ImageView avatar;
    FloatingActionButton back;
    String from;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        person = (User) bundle.getSerializable("person");
        from = bundle.getString("from"); //смотрим откуда вызвали фрагмент
        }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.another_person_profile,
                container, false);

        avatar = view.findViewById(R.id.avatar_view);

        email = view.findViewById(R.id.aboutTextViewItem);
        email.setText("почта: " + person.getEmail());

        username = view.findViewById(R.id.usernameTextView);
        username.setText("имя: " + person.getUsername());

        about = view.findViewById(R.id.aboutTextView);
        if (!person.getAbout_me().equals("пусто")) {about.setText(person.getAbout_me());}

        age = view.findViewById(R.id.ageTextView);
        age.setText("пользователю " + person.getAge() + " лет");


        if(person.getPhoto() != null) {
            avatar.setImageBitmap(person.getPhoto());
        }
        back=view.findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (from=="friend") {
                    FragmentManager fg = getActivity().getSupportFragmentManager();
                    fg.beginTransaction().show(fg.findFragmentByTag("2")).hide(fg.findFragmentByTag("4")).commit();
                } else  if (from=="dialog"){
                    FragmentManager fg = getActivity().getSupportFragmentManager();
                    fg.beginTransaction().show(fg.findFragmentByTag("3")).hide(fg.findFragmentByTag("4")).commit();
                }
            }
        });
        return view;
    }

    @Override
    public void onBackPressed() {
        if (from=="friend") {
            FragmentManager fg = getActivity().getSupportFragmentManager();
            fg.beginTransaction().show(fg.findFragmentByTag("2")).hide(fg.findFragmentByTag("4")).commit();
        } else  if (from=="dialog"){
            FragmentManager fg = getActivity().getSupportFragmentManager();
            fg.beginTransaction().show(fg.findFragmentByTag("3")).hide(fg.findFragmentByTag("4")).commit();
        }
    }

}

