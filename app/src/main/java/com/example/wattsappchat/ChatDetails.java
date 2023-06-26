package com.example.wattsappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.example.wattsappchat.Adapter.ChatAdapter;
import com.example.wattsappchat.Models.MessageModdel;
import com.example.wattsappchat.databinding.ActivityChatDetailsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class ChatDetails extends AppCompatActivity {


    ActivityChatDetailsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        final String senderId = auth.getUid();
        String recieveId = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePic");

        binding.userName.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.baseline_account_circle_24).into(binding.profilepic);




       binding.backArrow.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(ChatDetails.this,MainActivity.class);
               startActivity(intent);
           }
       });
        public boolean onCreateOptionsMenu(Menu menu) {
            MenuInflater menuInflater=getMenuInflater();
            menuInflater.inflate(R.menu.chat_menu ,menu);
            return super.onCreateOptionsMenu(menu);
        }
//        binding.profilepic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(FirebaseDatabase.getInstance().getReference().child("Users").toString());
//                startActivity(intent);
//            }
//        });

        final ArrayList<MessageModdel> messageModdels = new ArrayList<>();
        final ChatAdapter chatAdapter = new ChatAdapter(messageModdels, this, recieveId);

        binding.chatRecyclerView.setAdapter(chatAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.chatRecyclerView.setLayoutManager(layoutManager);

        final String senderRoom = senderId + recieveId;
        final  String reciverRoom = recieveId + senderId;


        database.getReference().child("Chats")
                        .child(senderRoom)
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        messageModdels.clear();
                                        for(DataSnapshot snapshot1 : snapshot.getChildren() )
                                        {
                                            MessageModdel model = snapshot1.getValue(MessageModdel.class);
                                            model.setMessageId(snapshot1.getKey());
                                            messageModdels.add(model);
                                        }
                                        chatAdapter.notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.enterMessage.getText().toString();
                final MessageModdel moddel = new MessageModdel(senderId,message);
                moddel.setTimestamp(new Date().getTime());
                binding.enterMessage.setText(" ");

                database.getReference().child("Chats").child(senderRoom)
                        .push().setValue(moddel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                           database.getReference().child("Chats")
                                   .child(reciverRoom)
                                   .push()
                                   .setValue(moddel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                       @Override
                                       public void onSuccess(Void unused) {

                                       }
                                   });
                            }
                        });
            }
        });
    }
}