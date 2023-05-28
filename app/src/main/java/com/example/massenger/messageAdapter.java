package com.example.massenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class messageAdapter extends RecyclerView.Adapter{
    private Context context;
   private ArrayList<message> messages;
   private User sender;
   private UserSerial reciver;
    private static final int VIEW_TYPE_MESSAGE_SENT = 1;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 2;

    public messageAdapter(ArrayList<message> messages, Context context,User sender,UserSerial reciver){
       this.context= context;
       this.messages= messages;
       this.sender= sender;
       this.reciver= reciver;

    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getFrom_who().equals(reciver.getUsername())){
            return VIEW_TYPE_MESSAGE_SENT;                                              //если сообщение от текущего пользователя т.е reciver то он считаеться отправленным
        }else
        {
            return VIEW_TYPE_MESSAGE_RECEIVED;                                          //и наоборот
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType==VIEW_TYPE_MESSAGE_SENT){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_send_message,parent,false);
            return new SendedMessageHolder(view);
        }else if (viewType==VIEW_TYPE_MESSAGE_RECEIVED) {
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recive_message,parent,false);
            return new RecivedMessageHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
    message message = messages.get(position);
      switch (holder.getItemViewType()){
          case VIEW_TYPE_MESSAGE_SENT:
              ((SendedMessageHolder)holder).bind(message);
              break;
          case VIEW_TYPE_MESSAGE_RECEIVED:
              ((RecivedMessageHolder)holder).bind(message);
      }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class RecivedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText,messageTime;
        public RecivedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.recivedTextView);
            messageTime=itemView.findViewById(R.id.recivedMsTime);
        }
        void bind(message message ){
            messageText.setText(message.getText());
            messageTime.setText(message.getWhen_sent().split(" ")[1]);

        }
    }
    public class SendedMessageHolder extends RecyclerView.ViewHolder{
        TextView messageText,messageTime;
        public SendedMessageHolder(@NonNull View itemView) {
            super(itemView);
            messageText=itemView.findViewById(R.id.sendedTextView);
            messageTime=itemView.findViewById(R.id.sendedMsTime);
        }
        void bind(message message){
            messageText.setText(message.getText());
            messageTime.setText(message.getWhen_sent().split(" ")[1]);
        }
    }
}
