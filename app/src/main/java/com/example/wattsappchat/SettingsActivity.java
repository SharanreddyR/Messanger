package com.example.wattsappchat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wattsappchat.Models.Users;
import com.example.wattsappchat.databinding.ActivitySettingsBinding;
import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        binding =ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    getSupportActionBar().hide();
    storage= FirebaseStorage.getInstance();
    auth = FirebaseAuth.getInstance();
    database = FirebaseDatabase.getInstance();
    binding.backArrow.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
        }
    });

    binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (!binding.txtusername.getText().toString().equals("") && !binding.txtabout.getText().toString().equals("")) {
                String status = binding.txtabout.getText().toString();
                String username = binding.txtusername.getText().toString();

                HashMap<String, Object> obj = new HashMap<>();
                obj.put("userName", username);
                obj.put("status", status);

                database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .updateChildren(obj);
                Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();

            }
            else {
                Toast.makeText(SettingsActivity.this, "Please Enter Details", Toast.LENGTH_SHORT).show();
            }
        }
    });

    database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Users users = snapshot.getValue(Users.class);
                            Picasso.get()
                                    .load(users.getProfilePic())
                                    .placeholder(R.drawable.baseline_account_circle_24)
                                    .into(binding.profile);


                            binding.txtabout.setText(users.getStatus());
                            binding.txtusername.setText(users.getUserName());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

    binding.plus.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("Images/=");
            startActivityForResult(intent, 25);
        }
    });
    binding.txtview2.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(SettingsActivity.this, "Privacy Clicked", Toast.LENGTH_SHORT).show();
        }
    });
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){
            Uri sFile= data.getData();
            binding.profile.setImageURI(sFile);


            final StorageReference storageReference = storage.getReference().child("Profile_pic")
                    .child(FirebaseAuth.getInstance().getUid());

            storageReference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            database.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("ProfilePic").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}