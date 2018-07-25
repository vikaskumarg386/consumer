package com.valuecomvikaskumar.consumer;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswordActivity extends AppCompatActivity {

    private Button clickHere;
    private EditText userEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        clickHere=findViewById(R.id.clickHere);
        userEmail=findViewById(R.id.forgetPasswordEmail);

        //click here to get reset password email
        clickHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String en=userEmail.getText().toString();
                if(!TextUtils.isEmpty(en)){
                    final ProgressDialog dialog=new ProgressDialog(ForgetPasswordActivity.this);
                    dialog.setMessage("please wait");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();
                    FirebaseAuth.getInstance().sendPasswordResetEmail(en).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                dialog.dismiss();
                                final AlertDialog.Builder builder=new AlertDialog.Builder(ForgetPasswordActivity.this);
                                builder.setMessage("An email is sent to you to reset password ");
                                builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent=new Intent(ForgetPasswordActivity.this,LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                                builder.show();

                            }
                            else {
                                Toast.makeText(ForgetPasswordActivity.this,"Wrong Email",Toast.LENGTH_SHORT).show();
                            }


                        }
                    });


                }
                else {
                    Toast.makeText(ForgetPasswordActivity.this,"Please Enter Your Email",Toast.LENGTH_SHORT).show();
                }






            }
        });
    }
}
