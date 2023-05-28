package com.example.massenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final ArrayList<User> friends;
    ItemClickListener itemClickListener;

    public FriendAdapter(Context context, ArrayList<User> friends,ItemClickListener itemClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.friends = friends;
        this.itemClickListener=itemClickListener;
    }

    @NonNull
    @Override
    public FriendAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =inflater.inflate(R.layout.item_friend,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendAdapter.ViewHolder holder, int position) {
        User friend = friends.get(position);
        holder.nameView.setText(friend.getUsername());
        holder.AvaView.setImageBitmap(friend.getPhoto());
        holder.aboutView.setText(friend.getAbout_me());
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameView, aboutView;
        ImageView AvaView,sendMs;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameView= itemView.findViewById(R.id.nameTextViewItem);
            AvaView = itemView.findViewById(R.id.friendAvatarItem);
            aboutView = itemView.findViewById(R.id.aboutTextViewItem);
            sendMs= itemView.findViewById(R.id.messageBtn);
            sendMs.setOnClickListener(this);

    }

        @Override
        public void onClick(View v) {
            itemClickListener.onClick(v,getAdapterPosition());
        }
    }
   public interface ItemClickListener{
        void onClick(View view, int Position);

    }
}
