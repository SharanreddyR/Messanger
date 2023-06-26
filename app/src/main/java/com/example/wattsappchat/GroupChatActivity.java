package com.example.wattsappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wattsappchat.Adapter.ChatAdapter;
import com.example.wattsappchat.Models.MessageModdel;
import com.example.wattsappchat.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {


    ActivityGroupChatBinding activityGroupChatBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityGroupChatBinding = ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(activityGroupChatBinding.getRoot());

        getSupportActionBar().hide();

        activityGroupChatBinding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<MessageModdel> messageModdels = new ArrayList<>();

        final String senderId = FirebaseAuth.getInstance().getUid();
        activityGroupChatBinding.userName.setText("Group Chat");


        final ChatAdapter chatAdapter = new ChatAdapter(messageModdels, this);
        activityGroupChatBinding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        activityGroupChatBinding.chatRecyclerView.setLayoutManager(layoutManager);


        database.getReference().child("Group Chat")
                        .addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                messageModdels.clear();
                                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                                    MessageModdel moddel=dataSnapshot.getValue(MessageModdel.class);
                                    messageModdels.add(moddel);
                                }
                                chatAdapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
        activityGroupChatBinding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String message = activityGroupChatBinding.enterMessage.getText().toString();
                final MessageModdel model = new MessageModdel(senderId, message);
                model.setTimestamp(new Date().getTime());

                activityGroupChatBinding.enterMessage.setText(" ");
                database.getReference().child("Group Chat")
                        .push()
                        .setValue(model)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(GroupChatActivity.this, "Message Sent", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });
        return false;
    }
}