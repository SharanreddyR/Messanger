package com.example.wattsappchat.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wattsappchat.Models.MessageModdel;
import com.example.wattsappchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChatAdapter extends RecyclerView.Adapter
{


    ArrayList<MessageModdel> messageModdel;
    Context context;

    String recId;
    int SENDER_VIEW_TyPE =1;
    int RECEIVER_VIEW_TYPE=2;

    public ChatAdapter(ArrayList<MessageModdel> messageModdel, Context context) {
        this.messageModdel = messageModdel;
        this.context = context;
    }

    public ChatAdapter(ArrayList<MessageModdel> messageModdel, Context context, String recId) {
        this.messageModdel = messageModdel;
        this.context = context;
        this.recId = recId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TyPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sender_message, parent, false);
            return new SenderViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.reciver_message, parent, false);
            return new ReciverViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MessageModdel messageModdel1 = messageModdel.get(position);

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                new AlertDialog.Builder(context)
                        .setTitle("Delete")
                        .setMessage("Are You Sure Want To Delete This Message ?")
                        .setPositiveButton("DELETE", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recId;
                                database.getReference().child("Chats").child(senderRoom)
                                        .child(messageModdel1.getMessageId())
                                        .setValue(null);
                            }
                        }).setNegativeButton("CANCLE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                return false;
            }
        });
        if(holder.getClass()==SenderViewHolder.class){
            ((SenderViewHolder) holder).senderMsg.setText(messageModdel1.getMessage());

            Date date = new Date(messageModdel1.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm a");
            String strDate = simpleDateFormat.format(date);

            ((SenderViewHolder) holder).senderTime.setText(strDate.toString());


        }else{
            ((ReciverViewHolder)holder).receverMsg.setText(messageModdel1.getMessage());

            Date date = new Date(messageModdel1.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("HH:mm a");
            String strDate = simpleDateFormat.format(date);

            ((ReciverViewHolder) holder).reciverTime.setText(strDate.toString());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messageModdel.get(position).getuId().equals(FirebaseAuth.getInstance().getUid()))
        {
            return SENDER_VIEW_TyPE;
        }else{
            return RECEIVER_VIEW_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return  messageModdel.size();
    }

    public class ReciverViewHolder extends RecyclerView.ViewHolder{
        TextView receverMsg, reciverTime;

        public ReciverViewHolder(@NonNull View itemView) {
            super(itemView);

            receverMsg = itemView.findViewById(R.id.recivertext);
            reciverTime = itemView.findViewById(R.id.reciverTime);
        }
    }
    public class SenderViewHolder extends RecyclerView.ViewHolder{

        TextView senderMsg, senderTime;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.sender_message);
            senderTime = itemView.findViewById(R.id.sender_time);
        }
    }
}
