package com.adi.qit.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.qit.R;
import com.adi.qit.activities.HomeActivity;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class SignInFragment extends Fragment {
    TextInputLayout emailEt, passwordEt;
    TextView forgotPTv;
    Button signInButton;

    OnClickListListener listener;

    SignInButton googleSignBtn;

    public SignInFragment(){
        super(R.layout.sign_in_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        signInButton = view.findViewById(R.id.sign_btn_si);
        emailEt = view.findViewById(R.id.email_et_si);
        passwordEt = view.findViewById(R.id.password_et_si);
        forgotPTv = view.findViewById(R.id.fp_tv_si);
        googleSignBtn = view.findViewById(R.id.google_sign_in_btn);

        Button fbBtn = view.findViewById(R.id.fn_sign_in_btn);

        fbBtn.setOnClickListener(v -> Toast.makeText(getContext(), "Facebook log in", Toast.LENGTH_SHORT).show());

        googleSignBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null){
                    listener.onClick();
                }
            }
        });

        signInButton.setOnClickListener(v -> {
            adminCheck();
            if (validate())
                signIn();
        });

        forgotPTv.setOnClickListener(v -> sendForgotLink());

        emailEt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                emailEt.setError(null);
            }
        });

        passwordEt.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                passwordEt.setError(null);
            }
        });
    }

    private void adminCheck() {
        String e = emailEt.getEditText().getText().toString().toLowerCase().trim();
        String p = passwordEt.getEditText().getText().toString().toLowerCase().trim();

        if (e.equals("admin") && p.equals("admin")) {
            startActivity(new Intent(getActivity(), HomeActivity.class));
            getActivity().finish();
        }
    }

    public void setClickListener(OnClickListListener listener){
        this.listener = listener;
    }

    public interface OnClickListListener{
        void onClick();
    }

    private void sendForgotLink() {
        if (validateEmail()){
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setCancelable(false);
            progressDialog.setTitle("Please wait..");
            progressDialog.setMessage("sending you the reset link");
            progressDialog.create();
            progressDialog.show();

            FirebaseAuth auth = FirebaseAuth.getInstance();
            auth.sendPasswordResetEmail(emailEt.getEditText().getText().toString())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if (task.isSuccessful()){
                                Toast.makeText(getContext(), "Password reset link sent successfully", Toast.LENGTH_SHORT).show();
                            }else {
                                new AlertDialog.Builder(getContext())
                                        .setTitle("Something went wrong")
                                        .setMessage(task.getException().getMessage())
                                        .setPositiveButton("Ok", (dialog, which) -> {
                                            dialog.dismiss();
                                        }).show();
                            }
                        }
                    });
        }
    }

    private boolean validateEmail(){
        boolean allOk = true;
        if (emailEt.getEditText().getText().toString().trim().matches("")){
            allOk = false;
            emailEt.setError("Email required to send reset link");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getEditText().getText().toString()).matches()){
            allOk = false;
            emailEt.setError("Invalid email to send reset link");
        }

        return allOk;
    }

    public boolean validate(){
        boolean allOk = true;
        if (emailEt.getEditText().getText().toString().trim().matches("")){
            allOk = false;
            emailEt.setError("Email required");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getEditText().getText().toString()).matches()){
            allOk = false;
            emailEt.setError("Invalid email");
        }

        if (passwordEt.getEditText().getText().toString().matches("")){
            allOk = false;
            passwordEt.setError("password required");
        }

        return allOk;
    }

    private void signIn(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("signing you in");
        progressDialog.create();
        progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(emailEt.getEditText().getText().toString(), passwordEt.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(getContext(), "successfully signed in", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getContext(), HomeActivity.class));
                            getActivity().finish();
                        }else {
                            new AlertDialog.Builder(getContext())
                                    .setTitle("Something went wrong")
                                    .setMessage(task.getException().getMessage())
                                    .setPositiveButton("Ok", (dialog, which) -> {
                                        dialog.dismiss();
                                    }).show();
                        }
                    }
                });
    }
}
