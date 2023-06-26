package com.example.wattsappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.wattsappchat.Adapter.FragmentsAdapter;
import com.example.wattsappchat.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();

        binding.viewPager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
binding.tabLayout.setupWithViewPager(binding.viewPager);
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu ,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();


        if(id == R.id.settings){
            Toast.makeText(this, "Clicked Settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }else if(id == R.id.groupchart){
            Toast.makeText(this, "Opening Group Chat", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, GroupChatActivity.class);
            startActivity(intent);
        }else if (id==R.id.logout){
            Toast.makeText(this, "Logging Out", Toast.LENGTH_SHORT).show();
            mAuth.signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

    }
}