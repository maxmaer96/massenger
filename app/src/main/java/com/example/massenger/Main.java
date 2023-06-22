package com.example.massenger;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.massenger.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main extends AppCompatActivity {
    ActivityMainBinding binding;
    private UserSerial current_userSerial;
    String Username;
    ImageView splash;
    ProgressBar load;
    BottomNavigationView bottomNavigationView;
    public Fragment active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



         //тут мы принимаем данные которые взяли из токена

            current_userSerial = (UserSerial) getIntent().getSerializableExtra("user");
            Username= current_userSerial.getUsername();

            Bundle bundle= new Bundle();
            bundle.putSerializable("current_user", current_userSerial);


        //передадим всем фрагментам данные о текущем пользователе
        FriendsFragment friendsFragment = new FriendsFragment();
        friendsFragment.setArguments(bundle);

        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);

        final FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.fragmentsHolder, friendsFragment, "2").hide(friendsFragment).commit(); //прогружаю все фрагменты сразу при запуске
        fm.beginTransaction().add(R.id.fragmentsHolder,profileFragment, "1").commit();

        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        active= profileFragment;
        binding.BotNav.setSelectedItemId(R.id.Profile); //при запуске открывать профиль

        binding.BotNav.setOnItemSelectedListener(item -> { //навигация с помощью нижней панели
            switch(item.getItemId()){
                case R.id.Friend:
                    fm.beginTransaction().hide(profileFragment).show(friendsFragment).commit();
                    break;
                case R.id.Profile:
                    fm.beginTransaction().hide(friendsFragment).show(profileFragment).commit();
                    break;

            }
            return true;
        });


    }

@Override
public void onBackPressed() { //обработка нажатие пользователем кнопки "назад" для фрагментов
    FragmentManager fm = getSupportFragmentManager();
    OnBackPressedListener backPressedListener = null;
    for (Fragment fragment: fm.getFragments()) {
        if (fragment instanceof  OnBackPressedListener) {
            backPressedListener = (OnBackPressedListener) fragment;
            break;
        }
    }

    if (backPressedListener != null) {
        backPressedListener.onBackPressed();
    } else {
        super.onBackPressed();
    }
}
}
