package fr.wcs.wildtwitter.Controllers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.StorageReference;

import fr.wcs.wildtwitter.Utils.Constants;
import fr.wcs.wildtwitter.Utils.FirebaseHelper;

/**
 * Created by adphi on 14/11/17.
 */

public class SignController {

    private final String TAG = Constants.TAG;

    private static volatile SignController sInstance = null;

    private FirebaseAuth mAuth = null;
    private FirebaseUser mUser = null;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseHelper mFirebaseHelper = null;

    // Listener
    private SignInListener mSignInListener = null;
    private SignUpListener mSignUpListener = null;
    private SignOutListener mSignOutListener = null;
    private ProfileUpdatedListener mProfileUpdatedListener = null;

    private SignController(){
        // Prevent from the reflection API.
        if(sInstance != null) {
            throw new RuntimeException("Use getInstance() to get the single instance of this class.");
        }

        mFirebaseHelper = FirebaseHelper.getInstance();
        mAuth = FirebaseHelper.getaAuthInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if(mSignUpListener != null) {
                        mSignInListener.onSuccess();
                    }

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
    }

    public static SignController getInstance(){
        // Check for the first time
        if(sInstance == null) {
            // Check for the second time
            synchronized (SignController.class) {
                // If no Instance available create new One
                if(sInstance == null) {
                    sInstance = new SignController();
                }
            }
        }
        return sInstance;
    }

    public void attach() {
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void dettach() {
        if(mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void signInWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            mUser = mAuth.getCurrentUser();
                            if(mSignUpListener != null) {
                                mSignUpListener.onSuccess();
                            }
                        }
                        else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if(mSignUpListener != null) {
                                mSignUpListener.onFailure(task.getException().getMessage().toString());
                            }
                        }
                    }
                });
    }

    public void signInWithMailAndPassword(final Activity activity, String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithEmail:success");
                            mUser = mAuth.getCurrentUser();
                            if(mSignInListener != null) {
                                mSignInListener.onSuccess();
                            }

                        } else {
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            if(mSignInListener != null) {
                                mSignInListener.onFailure(task.getException().getMessage().toString());
                            }
                        }
                    }
                });
    }

    public void signUpWithMailAndPassword(final Activity activity, String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Add name to Firebase User
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            if(mSignUpListener != null) {
                                mUser = task.getResult().getUser();
                                mSignUpListener.onSuccess();
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            if(mSignUpListener != null) {
                                mSignUpListener.onFailure(task.getException().getMessage().toString());
                            }
                        }
                    }
                });
    }

    public void updateUser(final String name, @NonNull Drawable drawable) {
        mFirebaseHelper.setOnImageUploadedListener(new FirebaseHelper.ImageUploadedListener() {
            @Override
            public void onSuccess(Uri imageUrl) {
                mFirebaseHelper.setOnImageUploadedListener(new FirebaseHelper.ImageUploadedListener() {
                    @Override
                    public void onSuccess(Uri imageUrl) {
                        updateUser(name, imageUrl);
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        if(mProfileUpdatedListener != null) {
                            mProfileUpdatedListener.onFailure();
                        }
                    }
                });
            }

            @Override
            public void onFailure(String errorMessage) {
                if(mProfileUpdatedListener != null) {
                    mProfileUpdatedListener.onFailure();
                }
            }
        });

        StorageReference ref = FirebaseHelper.getStorageInstance()
                .getReference(Constants.AVATAR_REFERENCE)
                .child(mUser.getUid());

        mFirebaseHelper.uploadImage(ref, drawable);
    }

    public void updateUser(String name) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            if(mProfileUpdatedListener != null) {
                                mProfileUpdatedListener.onSuccess();
                            }
                        }
                        else if(mProfileUpdatedListener != null) {
                            mProfileUpdatedListener.onFailure();
                        }
                    }
                });
    }

    public void updateUser(String name, Uri imageUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .setPhotoUri(imageUrl)
                    .build();


        mUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile updated.");
                            if(mProfileUpdatedListener != null) {
                                mProfileUpdatedListener.onSuccess();
                            }
                        }
                        else if(mProfileUpdatedListener != null) {
                            mProfileUpdatedListener.onFailure();
                        }
                    }
                });
    }

    public void setSignInListener(SignInListener listener) {
        mSignInListener = listener;
    }

    public interface SignInListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void setSignUpListener(SignUpListener listener) {
        mSignUpListener = listener;
    }

    public interface  SignUpListener {
        void onSuccess();
        void onFailure(String errorMessage);
    }

    public void setOnSignOutListener(SignOutListener listener) {
        mSignOutListener = listener;
    }

    public interface SignOutListener {
        void onSignOut();
    }

    public void setOnProfileUpdatedListener(ProfileUpdatedListener listener) {
        mProfileUpdatedListener = listener;
    }

    public interface ProfileUpdatedListener {
        void onSuccess();
        void onFailure();
    }

}
