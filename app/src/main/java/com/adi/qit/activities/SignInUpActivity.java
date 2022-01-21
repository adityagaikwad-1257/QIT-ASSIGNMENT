package com.adi.qit.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.adi.qit.R;
import com.adi.qit.fragments.SignInFragment;
import com.adi.qit.fragments.SignOutFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignInUpActivity extends AppCompatActivity {
    private static final String TAG = "aditya";
    Toolbar toolbar;
    TabLayout tabLayout;

    SignInFragment siFrag;
    SignOutFragment soFrag;

    GoogleSignInOptions gso;
    GoogleSignInClient googleSignInClient;

    ActivityResultLauncher<Intent> launcher;

    FirebaseAuth auth;

    ProgressDialog progressDialog;

    @Override
    protected void onStart() {
        super.onStart();
        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser() != null){
            sendToHome();
        }else {
            auth.signOut();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_in_up_activity);
        toolbar = findViewById(R.id.toolbar_sign_in_out);
        setSupportActionBar(toolbar);

        tabLayout = findViewById(R.id.tabLayout_sign_in_out);
        siFrag = new SignInFragment();
        soFrag = new SignOutFragment();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.sign_in_out_fcv, siFrag, "si")
                .commit();

        tabLayout.addOnTabSelectedListener(tabSelectedListener);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1024594794634-itci0fbbmtu1os7ruur55jeeao17pip6.apps.googleusercontent.com")
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
        googleSignInClient.signOut();

        registerLauncher();

        siFrag.setClickListener(new SignInFragment.OnClickListListener() {
            @Override
            public void onClick() {
                signInWithGoogle();
            }
        });
        
    }

    private void signInWithGoogle() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("signing you in");
        progressDialog.create();
        progressDialog.show();
        Intent signInIntent = googleSignInClient.getSignInIntent();
        launcher.launch(signInIntent);

    }

    private void registerLauncher() {

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                    try {
                        // Google Sign In was successful, authenticate with Firebase
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                        firebaseAuthWithGoogle(account.getIdToken());
                    } catch (ApiException e) {
                        // Google Sign In failed, update UI appropriately
                        Log.d(TAG, "Google sign in failed", e);
                        showError("Please try again..", "Something went wrong");
                    }
                });

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            sendToHome();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            showError(task.getException().getMessage(), "Something went wrong");
                        }
                    }
                });

    }

    private void sendToHome() {
        startActivity(new Intent(this, HomeActivity.class));
        finish();
    }

    private void showError(String msg, String title){
        if (progressDialog.isShowing())
            progressDialog.dismiss();

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton("Ok", (dialog, which) -> {
                    dialog.dismiss();
                }).show();
    }

    TabLayout.OnTabSelectedListener tabSelectedListener = new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {

            if (tab.getText().toString().equals("SIGN IN")) {
                replaceFragment(siFrag);
            }else {
                replaceFragment(soFrag);
            }
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    };

    public void replaceFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.sign_in_out_fcv, fragment)
                .commit();
    }
}