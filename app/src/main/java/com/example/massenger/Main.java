package com.example.massenger;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.massenger.databinding.ActivityMainBinding;

public class Main extends AppCompatActivity {
    ActivityMainBinding binding;
    private UserSerial current_userSerial;
    String Username;
    public Fragment active;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

         //тут мы принимаем данные которые взяли из токена

            current_userSerial = (UserSerial) getIntent().getSerializableExtra("user");
            Username= current_userSerial.getUsername();

            Bundle bundle= new Bundle();
            bundle.putSerializable("current_user", current_userSerial);
        final FragmentManager fm = getSupportFragmentManager();
        FriendsFragment friendsFragment = new FriendsFragment();
        friendsFragment.setArguments(bundle);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        DialogeFragment dialogeFragment = new DialogeFragment();
        SettingsFragment settingsFragment = new SettingsFragment();
        active= profileFragment;

        fm.beginTransaction().add(R.id.fragmentsHolder, dialogeFragment, "3").hide(dialogeFragment).commit();
        fm.beginTransaction().add(R.id.fragmentsHolder, friendsFragment, "2").hide(friendsFragment).commit();
        fm.beginTransaction().add(R.id.fragmentsHolder,profileFragment, "1").commit();

        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.BotNav.setSelectedItemId(R.id.Profile);

        binding.BotNav.setOnItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.Dialogs:
                    fm.beginTransaction().hide(active).show(dialogeFragment).commit();
                    active = dialogeFragment;

                    break;
                case R.id.Friend:
                    fm.beginTransaction().hide(active).show(friendsFragment).commit();
                    active = friendsFragment;
                    break;
                case R.id.Profile:
                    fm.beginTransaction().hide(active).show(profileFragment).commit();
                    active = profileFragment;
                    break;
                case R.id.settings:
                    fm.beginTransaction().hide(active).show(settingsFragment).commit();
                    active = settingsFragment;
                    break;
            }
            return true;
        });

    }
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction= fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentsHolder,fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}