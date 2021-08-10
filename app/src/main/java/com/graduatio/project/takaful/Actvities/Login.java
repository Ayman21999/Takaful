package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.MyFirebaseMessaging;

public class Login extends AppCompatActivity {

    EditText email;
    EditText pass;
    TextView forgetpass, createAccount;
    Button login;
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    CollectionReference reference;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SetUpElement();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if (user != null) {

                    Log.i("a user is logged in: ", user.getEmail());
                } else {
                    Log.i("Username", "there is no user");

                    String emailtxt = email.getText().toString();
                    String passtxt = pass.getText().toString();
                    if (emailtxt.isEmpty()) {
                        Toast.makeText(Login.this, getString(R.string.emialempty), Toast.LENGTH_SHORT).show();
                    } else if (passtxt.isEmpty()) {
                        Toast.makeText(Login.this, getString(R.string.emptypass), Toast.LENGTH_SHORT).show();
                    } else {
                        LogIn(emailtxt, passtxt);
//                        SessionManager manager = new SessionManager(Login.this);
                        startService(new Intent(Login.this, MyFirebaseMessaging.class));

//                        manager.CreateSession(emailtxt, passtxt);
                    }
                }
            }

        });

        forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgetPassowrd.class);
                startActivity(intent);
            }
        });

        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

    }

    public void SetUpElement() {
        email = findViewById(R.id.email);
        pass = findViewById(R.id.password);
        forgetpass = findViewById(R.id.forgetpassword);
        login = findViewById(R.id.signe_button);
        auth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        reference = firebaseFirestore.collection("Users");
        progressDialog = new ProgressDialog(this);
        createAccount = findViewById(R.id.sgin_in_text);
    }

    public void LogIn(String email, String pass) {
        progressDialog.setMessage("Loading ...! ");
        Task<AuthResult> task = auth.signInWithEmailAndPassword(email, pass);

        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Intent i = new Intent(Login.this, HomeActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
                finish();
                String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                        FirebaseFirestore.getInstance().collection("Users").document(id)
                                .update("cloudMessagingToken", s).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                            }
                        });
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ttt", "Error " + e.getLocalizedMessage());
                progressDialog.dismiss();

            }
        });

    }
//    public void verify() {
//
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null && user.isEmailVerified()) {
//            verifyEmail.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                        @Override
//                        public void onSuccess(Void aVoid) {
//                            Toast.makeText(Login.this,
//                                    R.string.Email_Verfiy,
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(Login.this,
//                                    "Email not sebt ", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                }
//            });
//        }
//    }


}