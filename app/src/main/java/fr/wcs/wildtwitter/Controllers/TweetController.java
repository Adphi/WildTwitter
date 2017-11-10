package fr.wcs.wildtwitter.Controllers;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;

import fr.wcs.wildtwitter.Models.TweetModel;
import fr.wcs.wildtwitter.Utils.FirebaseHelper;

/**
 * Created by adphi on 10/11/17.
 */

public class TweetController {

    private static volatile TweetController sInstance = null;

    private DatabaseReference tweetsReference;
    private ArrayList<TweetModel> tweets = new ArrayList<>();
    private TweetsListener mNewTweetListener = null;
    private TweetPublishListener mTweetPublishListener = null;

    private TweetController(){
        // Prevent from the reflection API.
        if(sInstance != null) {
            throw new RuntimeException("Use getInstance() to get the single instance of this class.");
        }
        tweetsReference = FirebaseHelper.getDatabaseInstance().getReference("Tweets");
        tweetsReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                TweetModel tweet = dataSnapshot.getValue(TweetModel.class);
                tweets.add(0, tweet);
                if(mNewTweetListener != null) {
                    mNewTweetListener.onTweetsChanged(tweets);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                TweetModel tweet = dataSnapshot.getValue(TweetModel.class);
                tweets.remove(tweet);
                if(mNewTweetListener != null) {
                    mNewTweetListener.onTweetsChanged(tweets);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void publish(String message) {
        publish(message, "");
    }

    private void publish(String message, String imageUrl) {
        // TODO: Write Tweet in Firebase
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String author = user.getDisplayName();
        String uid = user.getUid();
        String avatarUrl = user.getPhotoUrl().toString();
        long date = Calendar.getInstance().getTimeInMillis();

        TweetModel tweet = new TweetModel(author, uid, avatarUrl, message, imageUrl, date);
        publish(tweet);
    }

    public void pusblish(final String message, Drawable drawable) {
        final Bitmap picture = ((BitmapDrawable)drawable).getBitmap();
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
               mTweetPublishListener.onTweetPublished(false);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                String pictureUrl = downloadUrl.toString();
                publish(message, pictureUrl);
            }
        });
    }

    public void publish(TweetModel tweet){
        DatabaseReference tweetRef = tweetsReference.push();
        tweetRef.setValue(tweet);
        mTweetPublishListener.onTweetPublished(true);
    }

    public ArrayList<TweetModel> getTweets() {
        return tweets;
    }

    public static TweetController getInstance(){
        // Check for the first time
        if(sInstance == null) {
            // Check for the second time
            synchronized (TweetController.class) {
                // If no Instance available create new One
                if(sInstance == null) {
                    sInstance = new TweetController();
                }
            }
        }
        return sInstance;
    }

    public void setTweetsListener(TweetsListener listener) {
        mNewTweetListener = listener;
    }

    public interface TweetsListener {
        public void onTweetsChanged(ArrayList<TweetModel> tweets);
    }

    public void setTweetPublishListener(TweetPublishListener listener) {
        mTweetPublishListener = listener;
    }

    public interface TweetPublishListener {
        public void onTweetPublished(boolean succces);
    }
}
