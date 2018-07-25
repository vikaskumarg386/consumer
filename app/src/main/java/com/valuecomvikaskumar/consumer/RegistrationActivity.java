package com.valuecomvikaskumar.consumer;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class RegistrationActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button createAccount;
    private EditText name;
    private EditText email;
    private EditText password;
    private EditText rePassword;
    private FirebaseAuth firebaseAuth;
    private String userId;
    private DatabaseReference mRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        toolbar=findViewById(R.id.registerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setTitle("SignUp");

        name=findViewById(R.id.fullName);
        email=findViewById(R.id.registerEmail);
        password=findViewById(R.id.registerPassword);
        rePassword=findViewById(R.id.registerReenterPassword);

        mRef= FirebaseDatabase.getInstance().getReference();
        firebaseAuth=FirebaseAuth.getInstance();
        createAccount=findViewById(R.id.createAccount);

        //create account
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(name.getText())&&!TextUtils.isEmpty(email.getText())&&!TextUtils.isEmpty(password.getText())&&!TextUtils.isEmpty(rePassword.getText())) {

                    if(password.getText().toString().equals(rePassword.getText().toString())){
                        final ProgressDialog progressDialog=new ProgressDialog(RegistrationActivity.this);
                        progressDialog.setMessage("please wait");
                        progressDialog.setCanceledOnTouchOutside(false);
                        progressDialog.show();
                        firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    userId=FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    final Map map=new HashMap();
                                    map.put("name",name.getText().toString());
                                    map.put("email",email.getText().toString());
                                    map.put("password",password.getText().toString());
                                    mRef.child("Consumer").child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                mRef.child("Vendor").child(userId).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isSuccessful()){
                                                        progressDialog.dismiss();
                                                        Intent intent = new Intent(RegistrationActivity.this, AddressActivity.class);
                                                        startActivity(intent);
                                                    }
                                                    else {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(RegistrationActivity.this,"failed",Toast.LENGTH_SHORT).show();

                                                    }
                                                }
                                            });
                                            }
                                            else {
                                                progressDialog.dismiss();
                                                Toast.makeText(RegistrationActivity.this,"failed",Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    });
                                }
                                else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this,"registration failed",Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
                    }
                    else {
                        Toast.makeText(RegistrationActivity.this,"Please check your password",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(RegistrationActivity.this,"Please fill all the fields",Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}
