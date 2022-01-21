package com.adi.qit.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.adi.qit.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignOutFragment extends Fragment {
    TextInputEditText firstEt, lastEt, emailEt, phoneEt, passwordEt, cPasswordEt;
    Button signUpBtn;
    public SignOutFragment(){
        super(R.layout.sign_out_fragment);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        firstEt = view.findViewById(R.id.first_name_et_su);
        lastEt = view.findViewById(R.id.last_name_et_su);
        emailEt = view.findViewById(R.id.email_et_su);
        phoneEt = view.findViewById(R.id.phone_et_su);
        passwordEt = view.findViewById(R.id.password_et_su);
        cPasswordEt = view.findViewById(R.id.confirm_password_et_su);
        signUpBtn = view.findViewById(R.id.sign_up_btn_su);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate())
                    signUp();
            }
        });

    }

    private void signUp(){
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("while we create your account");
        progressDialog.create();
        progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(emailEt.getEditableText().toString(), passwordEt.getEditableText().toString())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Account created successfully", Toast.LENGTH_SHORT).show();
                            ((TabLayout) getActivity().findViewById(R.id.tabLayout_sign_in_out)).getTabAt(0).select();
                        }else{
                            progressDialog.dismiss();
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

    public boolean validate(){
        boolean allOk = true;

        if (firstEt.getEditableText().toString().trim().matches("")){
            allOk = false;
            firstEt.setError("name required");
        }
        if (emailEt.getEditableText().toString().trim().matches("")){
            emailEt.setError("Email required");
            allOk = false;
        }else if (!Patterns.EMAIL_ADDRESS.matcher(emailEt.getEditableText().toString()).matches()) {
            emailEt.setError("Invalid email");
            allOk = false;
        }
        if (phoneEt.getEditableText().toString().matches("")){
            phoneEt.setError("Phone number required");
            allOk = false;
        }else if (phoneEt.getEditableText().toString().length() < 10){
            phoneEt.setError("Invalid phone number");
            allOk = false;
        }
        if (passwordEt.getEditableText().toString().matches("")){
            passwordEt.setError("password required");
            allOk = false;
        }else if (!passwordEt.getEditableText().toString().equals(cPasswordEt.getEditableText().toString())){
            cPasswordEt.setError("password doesn't match");
            allOk = false;
        }

        return allOk;
    }
}
