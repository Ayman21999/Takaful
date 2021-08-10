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
import android.widget.RadioGroup;
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
    Button singe, next;
    ProgressDialog progressDialog;
    TextView logText;
    String id ;

    RadioGroup group;

    RadioButton Donors, beneficiary;
    String nametext, emailtext, passwordtext, confirmtext, phone_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        setUpElement();

        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (Donors.isChecked()) {
                    next.setVisibility(View.INVISIBLE);
                    singe.setVisibility(View.VISIBLE);
                } else if (beneficiary.isChecked()) {
                    singe.setVisibility(View.INVISIBLE);
                    next.setVisibility(View.VISIBLE);
                } else {

                }
            }
        });


        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nametext = username.getText().toString();
                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();
                confirmtext = confirmpassword.getText().toString();
                phone_txt = phone.getText().toString();
                if (nametext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.pleasfillname), Toast.LENGTH_SHORT).show();
                } else if (emailtext.isEmpty() && emailtext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.emialempty), Toast.LENGTH_SHORT).show();

                } else if (passwordtext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.emptypass), Toast.LENGTH_SHORT).show();

                } else if (!confirmtext.equals(passwordtext)) {
                    Toast.makeText(SignUp.this, getString(R.string.failpass), Toast.LENGTH_SHORT).show();

                } else if (phone_txt.isEmpty() && phone_txt.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.phoneEmpty), Toast.LENGTH_SHORT).show();

                } else if (!isValidMobile(phone_txt)) {
                    Toast.makeText(SignUp.this, getString(R.string.wrongPhoneNum), Toast.LENGTH_SHORT).show();
                } else {
                    RegisterBen(nametext, emailtext, passwordtext, phone_txt);
                    Toast.makeText(SignUp.this, getString(R.string.next), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, UserBeneficiaryInfo.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            }
        });
        logText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent  = new Intent(SignUp.this ,Login.class);
                startActivity(intent);
            }
        });
        singe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nametext = username.getText().toString();
                emailtext = email.getText().toString();
                passwordtext = password.getText().toString();
                confirmtext = confirmpassword.getText().toString();
                phone_txt = phone.getText().toString();
                if (nametext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.namempty), Toast.LENGTH_SHORT).show();
                } else if (emailtext.isEmpty() && emailtext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.emialempty), Toast.LENGTH_SHORT).show();

                } else if (passwordtext.isEmpty() && nametext.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.emptypass), Toast.LENGTH_SHORT).show();

                } else if (!confirmtext.equals(passwordtext)) {
                    Toast.makeText(SignUp.this, getString(R.string.failpass), Toast.LENGTH_SHORT).show();

                } else if (phone_txt.isEmpty() && phone_txt.equals(" ")) {
                    Toast.makeText(SignUp.this, getString(R.string.phoneEmpty), Toast.LENGTH_SHORT).show();

                } else if (!isValidMobile(phone_txt)) {
                    Toast.makeText(SignUp.this, getString(R.string.validphone), Toast.LENGTH_SHORT).show();
                } else {
                    Register(nametext, emailtext, passwordtext, phone_txt);
//                    RegisterMethod(nametext, emailtext, passwordtext, phone_txt);
//                    Toast.makeText(SignUp.this, getString(R.string.success), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUp.this, Login.class);
//                    verify();
//                    startService(new Intent(SignUp.this, MyFirebaseMessaging.class));
                    startActivity(intent);
                }

            }
        });

    }

    public void setUpElement() {
        auth = FirebaseAuth.getInstance();
        Donors = findViewById(R.id.Donors);
        group = findViewById(R.id.radioGroup2);
        beneficiary = findViewById(R.id.beneficiary);
        next = findViewById(R.id.next);
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
        progressDialog.setMessage(getString(R.string.loading));
        progressDialog.show();
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                if (authResult == null || authResult.getUser() == null) {
                    return;
                }

                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                map.put("email", email);
                map.put("firstName", name);
                map.put("lastName", "");
                map.put("phone", phone);
                map.put("password", password);
                map.put("isHasCharity", false);
                map.put("userImage", "");
                map.put("isSuspend", false);
                map.put("isBlocked", false);
                map.put("adsID", "");
                map.put("havework", "");
                map.put("social", "");
                map.put("age", "");
                map.put("familynum", "");
                map.put("salary", "");
                map.put("identity", "");
                map.put("isVerified", false);

                if (beneficiary.isChecked()) {
                    map.put("role", "beneficiary");

                } else {
                    map.put("role", "Donors");

                }
                map.put("payMethod", "");
                map.put("isHasActivity", false);
                reference.document(authResult.getUser().getUid())
                        .set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, "", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        auth.signOut();
                        finish();
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


    public void RegisterBen(String name, String email, String password, String phone) {
        progressDialog.setMessage(getString(R.string.loadingmssfa));
        progressDialog.show();
        Task<AuthResult> task = auth.createUserWithEmailAndPassword(email, password);
        task.addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                if (authResult == null || authResult.getUser() == null) {
                    return;
                }
                HashMap<String, Object> map = new HashMap<>();
                map.put("userId", FirebaseAuth.getInstance().getCurrentUser().getUid());
                map.put("email", email);
                map.put("firstName", name);
                map.put("lastName", "");
                map.put("phone", phone);
                map.put("password", password);
                map.put("isHasCharity", false);
                map.put("userImage", "");
                map.put("isSuspend", false);
                map.put("isBlocked", false);
                map.put("adsID", "");
                map.put("havework", "");
                map.put("social", "");
                map.put("age", "");
                map.put("familynum", "");
                map.put("salary", "");
                map.put("identity", "");
                map.put("isVerified", false);
                if (beneficiary.isChecked()) {
                    map.put("role", "beneficiary");

                } else {
                    map.put("role", "Donors");

                }
                map.put("payMethod", "");
                map.put("isHasActivity", false);
                reference.document(FirebaseAuth.getInstance().getCurrentUser().getUid()).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(SignUp.this, getString(R.string.next), Toast.LENGTH_SHORT).show();
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

//    public void verify() {
//
//        FirebaseUser user = auth.getCurrentUser();
//
//        if (user != null && user.isEmailVerified()) {
//            user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Toast.makeText(SignUp.this, "pleas check your email to verify your account ", Toast.LENGTH_SHORT).show();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("ttt", e.getMessage());
//                }
//            });
//        }
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        auth.signOut();
    }

    private boolean isValidMobile(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }

}