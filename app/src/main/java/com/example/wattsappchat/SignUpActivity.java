
package com.example.wattsappchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.wattsappchat.Models.Users;
import com.example.wattsappchat.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog progressdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_sign_up);

        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        getSupportActionBar().hide();
        progressdialog= new ProgressDialog(SignUpActivity.this);
        progressdialog.setTitle("Creating Account");
        progressdialog.setMessage("We're Creating Your Account.");

        binding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtuserName.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtpassword.getText().toString().isEmpty()){
                    progressdialog.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtpassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressdialog.dismiss();
                                    if(task.isSuccessful()){

                                        Users users=new Users(binding.txtuserName.getText().toString(), binding.txtEmail.getText().toString(),binding.txtpassword.getText().toString());
                                        String id= task.getResult().getUser().getUid();


//                                        database upload
                                        database.getReference().child("Users").child(id).setValue(users);
                                        Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                                    }else{
                                        Toast.makeText(SignUpActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignUpActivity.this, "Enter Deatils", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.txtAllready.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        return false;
    }
}
