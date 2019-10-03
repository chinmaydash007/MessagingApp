package com.example.messagingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActitivity extends AppCompatActivity {

    TextInputLayout phoneNumber, verifiactionCode;
    MaterialButton sendVerificatonCode, VerifyCode;
    String VerificationId;
    PhoneAuthProvider.ForceResendingToken  mSendToken;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login_actitivity);
        phoneNumber = findViewById(R.id.phone_number);
        verifiactionCode = findViewById(R.id.verification_code);
        sendVerificatonCode = findViewById(R.id.send_verify_code_btn);
        VerifyCode = findViewById(R.id.verify_code_btn);
        firebaseAuth=FirebaseAuth.getInstance();


        sendVerificatonCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneno = phoneNumber.getEditText().getText().toString();
                if (TextUtils.isEmpty(phoneno) || phoneno.length() != 10) {
                    phoneNumber.setError("Enter your valid phone number");
                } else {

                    sendVerificationCode("+91"+phoneno);
                }
            }
        });

        VerifyCode.setOnClickListener(new View.OnClickListener() {

            String code=verifiactionCode.getEditText().getText().toString();
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(code)){
                    verifiactionCode.setError("Enter the verification code");
                }
                else{
                    PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(VerificationId,code);
                    signInWithCredential(phoneAuthCredential);
                }
            }
        });
    }

    private void sendVerificationCode(String number) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(number, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallback);


    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            VerificationId=s;
            mSendToken=forceResendingToken;

            sendVerificatonCode.setVisibility(View.INVISIBLE);
            phoneNumber.setVisibility(View.INVISIBLE);
            VerifyCode.setVisibility(View.VISIBLE);
            verifiactionCode.setVisibility(View.VISIBLE);

        }

        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code=phoneAuthCredential.getSmsCode();
            if(code!=null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            Toast.makeText(PhoneLoginActitivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            sendVerificatonCode.setVisibility(View.VISIBLE);
            phoneNumber.setVisibility(View.VISIBLE);
            VerifyCode.setVisibility(View.INVISIBLE);
            verifiactionCode.setVisibility(View.INVISIBLE);
        }
    };

    private void verifyCode(String code){
        PhoneAuthCredential phoneAuthCredential=PhoneAuthProvider.getCredential(VerificationId,code);
        signInWithCredential(phoneAuthCredential);

    }
    private void signInWithCredential(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(PhoneLoginActitivity.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(PhoneLoginActitivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
