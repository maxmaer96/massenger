package com.example.massenger;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class FriendsFragment extends Fragment {


    UserSerial current_user;
    FloatingActionButton FriendBtn;
    RecyclerView recyclerView;
    ArrayList<User> friends_list,Friends;

    ViewUpdateTask viewUpdateTask;
    FriendAdapter.ItemClickListener itemClickListener,profileClicklistner;
    SwipeRefreshLayout Refreshing;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        current_user =(UserSerial) bundle.getSerializable("current_user");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends,
                container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        viewUpdateTask = new ViewUpdateTask();
        if (friends_list==null) {
            viewUpdateTask.execute();
        }

         itemClickListener = new FriendAdapter.ItemClickListener() {
             @Override
             public void onClickMS(View view, int Position) { //обрабатываем переход в диалог с конкретным юзером
                 DialogeFragment dialogeFragment= new DialogeFragment();

                 Bundle bundle = new Bundle();
                 bundle.putSerializable("user",Friends.get(Position));
                 bundle.putSerializable("current_user",current_user);
                 dialogeFragment.setArguments(bundle);
                 FragmentManager fg=  getActivity().getSupportFragmentManager();
                 fg.beginTransaction().add(R.id.fragmentsHolder,dialogeFragment,"3").hide(fg.findFragmentByTag("2")).show(dialogeFragment).commit();
             }

             @Override
             public void onClickAva(View view, int Position) { //переход на профиль другого человека
                 Bundle bundle = new Bundle();
                 bundle.putSerializable("person",Friends.get(Position));
                 bundle.putString("from","friend");
                 AnotherUserProfile anotherUserProfile= new AnotherUserProfile();
                 anotherUserProfile.setArguments(bundle);
                 FragmentManager fg=  getActivity().getSupportFragmentManager();
                 fg.beginTransaction().add(R.id.fragmentsHolder,anotherUserProfile,"4").hide(fg.findFragmentByTag("2")).show(anotherUserProfile).commit();
             }


        };

        // обрабатываем диалог с пользователем когда он хочет добавить друга

        FriendBtn = view.findViewById(R.id.AddBtn);
        FriendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder AskUsername = new AlertDialog.Builder(getActivity());
                AskUsername.setTitle("введите username человека которого хотели бы добавить");
                final EditText AskedUsername = new EditText(getActivity());
                AskUsername.setView(AskedUsername);
                AskUsername.setPositiveButton("добавить", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (AskedUsername.getText().toString().isEmpty()) {
                            Toast.makeText(getActivity(), "не оставляйте поле пустым!", Toast.LENGTH_SHORT).show();
                        } else {
                            String api = "https://messapi.space/api/friend/create.php";
                            JSONObject json = new JSONObject();

                            try {
                                json.put("friend1", current_user.getUsername());
                                json.put("friend2", AskedUsername.getText().toString());
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }


                            RequestQueue queue = Volley.newRequestQueue(getActivity());
                            JsonObjectRequest friendRequest = new JsonObjectRequest(Request.Method.POST, api, json,
                                    response -> {
                                        Toast.makeText(getActivity(), "добавлен успешно!", Toast.LENGTH_SHORT).show();

                                            new ViewUpdateTask().execute();

                                        dialog.cancel();

                                    },
                                    error -> {
                                        Toast.makeText(getActivity(), "пользователь либо уже был добавлен, либо не существует", Toast.LENGTH_LONG).show();
                                        if (error.getMessage() == null) {
                                            Log.e("adderr", error.toString());
                                        } else Log.e("adderr", error.getLocalizedMessage());
                                        dialog.cancel();
                                    });
                            queue.add(friendRequest);
                        }
                    }
                });
                AskUsername.setNegativeButton("отмена", (dialog, which) -> dialog.cancel());
                AskUsername.show();
            }
        });

        Refreshing = view.findViewById(R.id.Refreshing);
        Refreshing.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() { //при свайпе вверх до упора обновляем
            @Override
            public void onRefresh() {
                ViewUpdateTask refreshUpdateTask= new ViewUpdateTask();
                refreshUpdateTask.execute();

            }
        });
        return view;
    }

    class ViewUpdateTask extends AsyncTask<Void, ArrayList<User>, ArrayList<User>> { //асинхронная задача для подгрузки данных на страницу
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getActivity(),"пару секунд.. подгружаю список друзей",Toast.LENGTH_SHORT).show();
        }
        @Override
        protected ArrayList<User> doInBackground(Void... voids) {
             friends_list = new ArrayList<>();

            String api = "https://messapi.space/api/friend/read.php/";
            JSONObject json = new JSONObject();
            try {
                json.put("friend1", current_user.getUsername());

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }

            RequestQueue queue = Volley.newRequestQueue(getActivity().getApplicationContext());
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api, json,
                    response -> {//перехватываем список друзей из базы с помощью цикла
                        try {
                            if (!response.getString("count").isEmpty()) {

                               int FriendCount = response.getInt("count");

                                for (int i = 1; i <= FriendCount; i++) {
                                    Bitmap photo;
                                    if (response.getString("user" + i + " photo").equals("пусто")) {
                                        photo = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.no_ava);
                                    } else {
                                        photo = ImageHandler.convert(response.getString("user" + i + " photo"));
                                    }

                                    friends_list.add(new User(response.getString("user" + i + " about"),response.getString("user" + i),photo));
                                    publishProgress(friends_list);

                                }

                            }else Toast.makeText(getActivity().getApplicationContext(),"у вас пока что нет друзей",Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    },
                    error -> {
                        Log.i("Volley", error.toString());
                        Toast.makeText(getActivity(), "ошибка подключения к базе", Toast.LENGTH_LONG).show();

                    });
            queue.add(request);
            return friends_list;
        }

        @SafeVarargs
        @Override
        protected final void onProgressUpdate(ArrayList<User>... friends) {
            super.onProgressUpdate(friends);
          recyclerView.setAdapter(new FriendAdapter(getActivity().getApplicationContext(),friends[0],itemClickListener));
          Friends=friends[0];
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            Toast.makeText(getActivity().getApplicationContext(),"сделано",Toast.LENGTH_SHORT).show();
            Refreshing.setRefreshing(false);
        }
    }
}