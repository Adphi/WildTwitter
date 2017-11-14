package fr.wcs.wildtwitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.wcs.wildtwitter.Controllers.SignController;
import fr.wcs.wildtwitter.UI.Activities.MainActivity;
import fr.wcs.wildtwitter.Utils.Constants;
import pl.aprilapps.easyphotopicker.EasyImage;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = Constants.TAG;

    private CircleImageView mAvatarView;

    private ProgressDialog mProgressDialog;

    private boolean uploadAvatar = false;

    private SignController mSignController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mProgressDialog = new ProgressDialog(SignUpActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setMessage("Registration in Progress.");

        mAvatarView = (CircleImageView) findViewById(R.id.avatarView);
        mAvatarView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "Pick an Avatar", 0);
            }
        });

        mSignController = SignController.getInstance();
        mSignController.setOnProfileUpdatedListener(new SignController.ProfileUpdatedListener() {
            @Override
            public void onSuccess() {
                mProgressDialog.cancel();
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure() {
                mProgressDialog.cancel();
                Toast.makeText(SignUpActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });

        Button buttonRegister = findViewById(R.id.buttonRegister);
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editTextName = findViewById(R.id.editTextName);
                EditText editTextMail = findViewById(R.id.editTextSignUpMail);
                EditText editTextPassword = findViewById(R.id.editTextSignUpPassword);
                EditText editTextConfirmPassword = findViewById(R.id.editTextSignUpConfirmPassword);
                final String name = editTextName.getText().toString();
                String email = editTextMail.getText().toString();
                String password = editTextPassword.getText().toString();
                String confirmPassword = editTextConfirmPassword.getText().toString();
                if(email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || name.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please, fill all the fields.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(!password.equals(confirmPassword)) {
                    Toast.makeText(SignUpActivity.this, "Password and confirmation are different.",
                            Toast.LENGTH_SHORT).show();
                }
                else if(password.length() < 6) {
                    Toast.makeText(SignUpActivity.this, "Password should be at least 6 characters.",
                            Toast.LENGTH_SHORT).show();
                }
                else {
                    mProgressDialog.show();
                    mSignController.signUpWithMailAndPassword(SignUpActivity.this, email, password);
                    mSignController.setSignUpListener(new SignController.SignUpListener() {
                        @Override
                        public void onSuccess() {
                            if(uploadAvatar) {
                                mSignController.updateUser(name, mAvatarView.getDrawable());
                            }
                            else {
                                mSignController.updateUser(name);
                            }
                        }

                        @Override
                        public void onFailure(String errorMessage) {
                            Toast.makeText(SignUpActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                            mProgressDialog.cancel();
                        }
                    });

                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSignController.attach();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSignController.dettach();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new EasyImage.Callbacks() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {

            }

            @Override
            public void onImagePicked(File imageFile, EasyImage.ImageSource source, int type) {
                Log.d(TAG, "onImagePicked() called with: imageFile = [" + imageFile + "], source = [" + source + "], type = [" + type + "]");
                String avatarUri = imageFile.getPath();
                mAvatarView.setImageDrawable(Drawable.createFromPath(avatarUri));
                uploadAvatar = true;
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
}
