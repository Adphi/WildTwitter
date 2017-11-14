package fr.wcs.wildtwitter.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import fr.wcs.wildtwitter.Controllers.SignController;
import fr.wcs.wildtwitter.R;
import fr.wcs.wildtwitter.SignUpActivity;
import fr.wcs.wildtwitter.Utils.Constants;

public class SignInActivity extends AppCompatActivity {

    private static final String TAG = Constants.TAG;

    private static final int RC_SIGN_IN = 1;
    private GoogleApiClient mGoogleApiClient;
    private SignController mSignController = null;

    private ProgressDialog mProgressDialog;

    private int mBackButtonCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Signing In.");

        mSignController = SignController.getInstance();

        mSignController.setSignInListener(new SignController.SignInListener() {
            @Override
            public void onSuccess() {
                mProgressDialog.cancel();
                Toast.makeText(SignInActivity.this, "Signed In", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SignInActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(SignInActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        SignInButton buttonGoogleSignIn = findViewById(R.id.googleSignIn);
        buttonGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });


        // email and Password Auth
        Button buttonLogIn = findViewById(R.id.buttonLogIn);
        buttonLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextMail = findViewById(R.id.editTextMail);
                EditText editTextPassword = findViewById(R.id.editTextPassword);
                String email = editTextMail.getText().toString();
                String password = editTextPassword.getText().toString();
                if(!email.isEmpty() && !password.isEmpty()) {
                    mProgressDialog.show();
                    mSignController.signInWithMailAndPassword(SignInActivity.this, email, password);
                }
                else if(email.isEmpty() && password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please enter your email and password.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    Toast.makeText(SignInActivity.this, "Please enter your email.",
                            Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(SignInActivity.this, "Please enter your password.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button buttonSignUp = findViewById(R.id.buttonSignUp);
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, SignUpActivity.class));
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();
        mSignController.attach();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSignController.dettach();
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            mProgressDialog.show();
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                // Google Sign In was successful
                GoogleSignInAccount account = result.getSignInAccount();
                mSignController.signInWithGoogle(account);
            } else {
                // Google Sign In failed
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBackButtonCount = 0;
    }

    @Override
    public void onBackPressed() {
        if(mBackButtonCount > 0) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else {
            Toast.makeText(this, R.string.exit_confirmation, Toast.LENGTH_SHORT).show();
            mBackButtonCount++;
        }
    }
}
