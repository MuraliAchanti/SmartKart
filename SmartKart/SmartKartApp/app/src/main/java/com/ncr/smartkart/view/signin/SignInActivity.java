package com.ncr.smartkart.view.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ncr.smartkart.R;
import com.ncr.smartkart.databinding.ActivitySignInBinding;
import com.ncr.smartkart.utils.ActivityUtils;
import com.ncr.smartkart.utils.Event;
import com.ncr.smartkart.utils.PersistentDeviceStorage;
import com.ncr.smartkart.viewModel.SignInViewModel;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = "SignInActivity";
    private final int GOOGLE_SIGN_IN = 1;
    SignInViewModel signInViewModel;
    ActivitySignInBinding activitySignInBinding;

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private PersistentDeviceStorage persistentDeviceStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignInBinding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        signInViewModel = ActivityUtils.obtainViewModel(this, SignInViewModel.class);
        activitySignInBinding.setSignInViewModel(signInViewModel);
        persistentDeviceStorage = PersistentDeviceStorage.getInstance(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();

        signInViewModel.getGoogleSignInCommand().observe(this, new Observer<Event<Object>>() {
            @Override
            public void onChanged(Event<Object> objectEvent) {
                if (objectEvent.getContentIfNotHandled() != null) {
                    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, GOOGLE_SIGN_IN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (account != null) {
                    firebaseAuthWithGoogle(account);
                } else {
                    throw new Exception();
                }
            } catch (ApiException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            persistentDeviceStorage.setEmail(acct.getEmail());
                            persistentDeviceStorage.setName(acct.getDisplayName());
                            persistentDeviceStorage.setPic(acct.getPhotoUrl().toString());
                            navigateToPhoneSignIn();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(SignInActivity.this, "Sign In Failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    private void navigateToPhoneSignIn() {
        Intent intent = new Intent(this, PhoneActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}