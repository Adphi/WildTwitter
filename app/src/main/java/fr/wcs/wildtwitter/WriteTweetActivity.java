package fr.wcs.wildtwitter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import pl.aprilapps.easyphotopicker.EasyImage;

public class WriteTweetActivity extends AppCompatActivity {

    private final static String TAG = Constants.TAG;

    private ImageView mImageViewPicture;
    private String pictureUrl = "";
    private boolean uploadPicture = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_tweet);

        mImageViewPicture = findViewById(R.id.imageViewPicture);
        Log.d(TAG, "onCreate: Hiding Picture View");
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
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();
                final String author = user.getDisplayName();
                final String uid = user.getUid();
                final String avatarUrl = user.getPhotoUrl().toString();
                final long date = Calendar.getInstance().getTimeInMillis();

                EditText editText = findViewById(R.id.editTextMessage);
                final String message = editText.getText().toString();

                if(message.isEmpty()) {
                    return;
                }

                final DatabaseReference tweetRef = FirebaseDatabase.getInstance().
                        getReference("Tweets")
                        .push();

                if(!uploadPicture) {
                    TweetModel tweet = new TweetModel(author, uid, avatarUrl, message, pictureUrl, date);
                    tweetRef.setValue(tweet);
                    Toast.makeText(WriteTweetActivity.this, "Send.", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(
                            WriteTweetActivity.this, MainActivity.class);
                    WriteTweetActivity.this.finish();
                    startActivity(intent);
                }
                else {

                    final ProgressDialog progressDialog = new ProgressDialog(WriteTweetActivity.this);
                    progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Message Sending.");
                    progressDialog.show();

                    Drawable avatarDrawable = mImageViewPicture.getDrawable();
                    final Bitmap picture = ((BitmapDrawable)avatarDrawable).getBitmap();
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    picture.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] data = baos.toByteArray();

                    StorageReference pictureRef = FirebaseStorage.getInstance()
                            .getReference("TweetsPictures")
                            .child(String.valueOf(data.hashCode()));
                    UploadTask uploadTask = pictureRef.putBytes(data);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            Log.d(TAG, "onFailure() called with: exception = [" + exception + "]");
                            progressDialog.cancel();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            pictureUrl = downloadUrl.toString();

                            TweetModel tweet = new TweetModel(author, uid, avatarUrl, message, pictureUrl, date);
                            tweetRef.setValue(tweet);
                            Toast.makeText(
                                    WriteTweetActivity.this, "Send.", Toast.LENGTH_SHORT)
                                    .show();
                            progressDialog.cancel();
                            Intent intent = new Intent(
                                    WriteTweetActivity.this, MainActivity.class);
                            WriteTweetActivity.this.finish();
                            startActivity(intent);
                        }
                    });
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
                Log.d(TAG, "onImagePicked() called with: imageFile = [" + imageFile + "], source = [" + source + "], type = [" + type + "]");
                String avatarUri = imageFile.getPath();
                mImageViewPicture.setImageDrawable(Drawable.createFromPath(avatarUri));
                mImageViewPicture.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "onImagePicked: Showing Picture View");
                        mImageViewPicture.setVisibility(View.VISIBLE);
                    }
                }, 100);
                //mImageViewPicture.setVisibility(View.VISIBLE);
                uploadPicture = true;
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {

            }
        });
    }
}
