package com.graduatio.project.takaful.Actvities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
import com.graduatio.project.takaful.R;
import com.graduatio.project.takaful.Service.MyFirebaseMessaging;

import java.util.HashMap;
import java.util.UUID;

public class SignUp extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseFirestore firebaseFirestore;
    EditText email, password, confirmpassword, phone, username;

    CollectionReference reference;
    Button singe;
    ProgressDialog progressDialog;
    TextView logText;

  RadioButton Donors ;
  RadioButton beneficiary ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpElement();

        singe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nametext = username.getText().toString();
                String emailtext = email.getText().toString();
                String passwordtext = password.getText().toString();
                String confirmtext = confirmpassword.getText().toString();
                String phone_txt = phone.getText().toString();
                if (nametext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, "Please fill the Your name", Toast.LENGTH_SHORT).show();
                } else if (emailtext.isEmpty() && emailtext.equals(" ")) {
                    Toast.makeText(SignUp.this, "Please fill the Your E-mail", Toast.LENGTH_SHORT).show();

                } else if (passwordtext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, "Please fill the Your  Password", Toast.LENGTH_SHORT).show();

                } else if (!confirmtext.equals(passwordtext)) {
                    Toast.makeText(SignUp.this, "Password must be matched", Toast.LENGTH_SHORT).show();

                } else if (phone_txt.isEmpty() && phone_txt.equals(" ")) {
                    Toast.makeText(SignUp.this, "Please fill the Your  Phone Number ", Toast.LENGTH_SHORT).show();

                } else {
                    Register(nametext, emailtext, passwordtext, phone_txt);
                    Toast.makeText(SignUp.this, "SingeUP Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, HomeActivity.class);
//                    startService(new Intent(SignUp.this, MyFirebaseMessaging.class));
                    startActivity(intent);
                }

            }
        });

    }

    public void setUpElement() {
        auth = FirebaseAuth.getInstance();
        Donors = findViewById(R.id.Donors);
        beneficiary = findViewById(R.id.beneficiary);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        confirmpassword = findViewById(R.id.confirm);
        username = findViewById(R.id.username);
        singe = findViewById(R.id.signe_button);
        progressDialog = new ProgressDialog(this);
        logText = findViewById(R.id.sgin_in_text);
        firebaseFirestore = FirebaseFirestore.getInstance();
        phone = findViewById(R.id.phone);
        reference = firebaseFirestore.collection("Users");
    }
    public void Register(String name, String email, String password, String phone) {
        progressDialog.setMessage("Loading...!!");
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", FirebaseAuth.getInstance().getUid());
                map.put("email", email);
                map.put("firstName", name);
                map.put("lastName", "");
                map.put("phone", phone);
                map.put("password", password);
                map.put("isHasCharity", false);
                map.put("userImage", "");
                map.put("isSuspend", false);
                map.put("adsID","");
                if (beneficiary.isChecked()){
                    map.put("role", "beneficiary");

                }else {
                    map.put("role", "Donors");
                }
                map.put("payMethod", "");
                map.put("isHasActivity", false);
                reference.document(FirebaseAuth.getInstance().getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, "", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(SignUp.this, "Fire store Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUp.this, "Auth Error : " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                Log.d("ttt", "Error : " + e.getLocalizedMessage());
                progressDialog.dismiss();
            }
        });

    }
}