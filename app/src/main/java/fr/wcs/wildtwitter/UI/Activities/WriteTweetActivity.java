package fr.wcs.wildtwitter.UI.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

import fr.wcs.wildtwitter.Controllers.TweetController;
import fr.wcs.wildtwitter.R;
import fr.wcs.wildtwitter.Utils.Constants;
import pl.aprilapps.easyphotopicker.EasyImage;

public class WriteTweetActivity extends AppCompatActivity {

    private final static String TAG = Constants.TAG;

    private TweetController mTweetController;
    private ImageView mImageViewPicture;
    private boolean imageAttached = false;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tweet);

        mTweetController = TweetController.getInstance();

        mProgressDialog = new ProgressDialog(WriteTweetActivity.this);
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Message Sending.");

        mTweetController.setTweetPublishListener(new TweetController.TweetPublishListener() {
            @Override
            public void onTweetPublished(boolean succes) {
                if(succes) {
                    Toast.makeText(WriteTweetActivity.this, "Sent.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(WriteTweetActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                mProgressDialog.cancel();
                startActivity(new Intent(WriteTweetActivity.this, MainActivity.class));
            }
        });

        mImageViewPicture = findViewById(R.id.imageViewPicture);
        mImageViewPicture.setVisibility(View.GONE);

        ImageButton imageButtonPicture = findViewById(R.id.imageButtonPicture);
        imageButtonPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(WriteTweetActivity.this, "Pick an Avatar", 0);
            }
        });

        FloatingActionButton buttonSend = findViewById(R.id.floatingActionButtonSend);
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editText = findViewById(R.id.editTextMessage);
                String message = editText.getText().toString();
                mProgressDialog.show();

                if(imageAttached) {
                    Drawable image = mImageViewPicture.getDrawable();
                    mTweetController.pusblish(message, image);
                }
                else {
                    mTweetController.publish(message);
                }
            }
        });
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
                String avatarUri = imageFile.getPath();
                mImageViewPicture.setImageDrawable(Drawable.createFromPath(avatarUri));
                mImageViewPicture.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mImageViewPicture.setVisibility(View.VISIBLE);
                    }
                }, 100);
                imageAttached = true;
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
}
