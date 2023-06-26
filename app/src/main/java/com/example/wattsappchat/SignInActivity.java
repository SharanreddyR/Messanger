
package com.example.wattsappchat;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.wattsappchat.Models.Users;
import com.example.wattsappchat.databinding.ActivitySignInBinding;
import com.example.wattsappchat.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;


public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    ProgressDialog progressdialog;
    FirebaseAuth mAuth;
    FirebaseDatabase firebaseDatabase;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sign_in);
        binding= ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        progressdialog = new ProgressDialog(SignInActivity.this);
        progressdialog.setTitle("Login");
        progressdialog.setMessage("Please wait\nValidation in Progress");

//        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
//                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                        .setSupported(true)
//                        // Your server's client ID, not your Android client ID.
//                        .setServerClientId(getString(R.string.default_web_client_id))
//                        // Only show accounts previously used to sign in.
//                        .setFilterByAuthorizedAccounts(true)
//                        .build())
//                .build();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                                .build();
         mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        binding.btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.txtEmail.getText().toString().isEmpty() && !binding.txtPassword.getText().toString().isEmpty()){
                    progressdialog.show();
                    mAuth.signInWithEmailAndPassword(binding.txtEmail.getText().toString(), binding.txtPassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressdialog.dismiss();
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                       startActivity(intent);
                                    }else{
                                        Toast.makeText(SignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });


                }else{
                    Toast.makeText(SignInActivity.this, "Enter Valid Details", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(mAuth.getCurrentUser()!=null){
            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            startActivity(intent);
        }
        binding.txtClickSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(intent);
            }
        });

        binding.btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
        return false;
    }

    int RC_SIGN_IN=65;
    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:"+account.getId());
                firebaseAuthWithGoogle(account.getIdToken());


            } catch (ApiException e) {
                Log.w("TAG", "Google Sign In Failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(firebaseCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setProfilePic(user.getPhotoUrl().toString());
                            firebaseDatabase.getReference().child("Users").child(user.getUid()).setValue(users);

                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(SignInActivity.this, "Sign In With Google", Toast.LENGTH_SHORT).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }
    }



//package com.example.wattsappchat;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Toast;
//
//import com.example.wattsappchat.Models.Users;
//import com.example.wattsappchat.databinding.ActivitySignUpBinding;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.database.FirebaseDatabase;
//
//    public class SignInActivity extends AppCompatActivity {
//
//        ActivitySignUpBinding binding;
//
//        private FirebaseAuth mAuth;
//        FirebaseDatabase database;
//        ProgressDialog progressdialog;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            binding=ActivitySignUpBinding.inflate(getLayoutInflater());
//            setContentView(R.layout.activity_sign_up);
//
//            setContentView(binding.getRoot());
//
//            mAuth = FirebaseAuth.getInstance();
//            database = FirebaseDatabase.getInstance();
//
//            getSupportActionBar().hide();
//            progressdialog= new ProgressDialog(SignInActivity.this);
//            progressdialog.setTitle("Creating Account");
//            progressdialog.setMessage("We're Creating Your Account.");
//
//            binding.btnSignup.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    if(!binding.txtuserName.getText().toString().isEmpty() && !binding.txtEmail.getText().toString().isEmpty() && !binding.txtpassword.getText().toString().isEmpty()){
//                        progressdialog.show();
//                        mAuth.createUserWithEmailAndPassword(binding.txtEmail.getText().toString(),binding.txtpassword.getText().toString())
//                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                                    @Override
//                                    public void onComplete(@NonNull Task<AuthResult> task) {
//                                        progressdialog.dismiss();
//                                        if(task.isSuccessful()){
//
//                                            Users users=new Users(binding.txtuserName.getText().toString(), binding.txtEmail.getText().toString(),binding.txtpassword.getText().toString());
//                                            String id= task.getResult().getUser().getUid();
//
//
////                                        database upload
//                                            database.getReference().child("Users").child(id).setValue(users);
//                                            Toast.makeText(SignInActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
//                                        }else{
//                                            Toast.makeText(SignInActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
//                                        }
//                                    }
//                                });
//                    }
//                    else{
//                        Toast.makeText(SignInActivity.this, "Enter Credntials", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//
//
//        }
//    }
