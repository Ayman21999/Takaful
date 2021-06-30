package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.graduatio.project.takaful.R;

public class Login extends AppCompatActivity {

    EditText email;
    EditText pass;
    TextView forgetpass;
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
                String emailtxt = email.getText().toString();
                String passtxt = pass.getText().toString();
                if (emailtxt.equals("") && emailtxt.isEmpty()) {
                    Toast.makeText(Login.this, "Email is required", Toast.LENGTH_SHORT).show();
                } else if (passtxt.isEmpty() && passtxt.trim().equals("")) {
                    Toast.makeText(Login.this, "Password is required", Toast.LENGTH_SHORT).show();

                } else {
                    LogIn(emailtxt, passtxt);

                }

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

    }

    public void LogIn(String email, String pass) {
        progressDialog.setMessage("Loading ...! ");
        Task<AuthResult> task = auth.signInWithEmailAndPassword(email, pass);

        task.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Intent intent = new Intent(Login.this, HomeActivity.class);
                startActivity(intent);
                progressDialog.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Error :" + e.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
    }


}