package fr.wcs.wildtwitter.Utils;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by adphi on 10/11/17.
 */

public class FirebaseHelper {

    private static final String TAG = Constants.TAG;

    private static volatile FirebaseHelper sInstance = null;

    private static FirebaseDatabase sFirebaseDatabase = null;
    private static FirebaseAuth sFirebaseAuth = null;
    private static FirebaseStorage sFirebaseStorage = null;

    private ImageUploadedListener mImageUploadedListener = null;

    public static FirebaseDatabase getDatabaseInstance(){
        if(sFirebaseDatabase == null) {
            sFirebaseDatabase = FirebaseDatabase.getInstance();
            sFirebaseDatabase.setPersistenceEnabled(true);
        }
        return sFirebaseDatabase;
    }

    public static FirebaseAuth getaAuthInstance(){
        if(sFirebaseAuth == null) {
            sFirebaseAuth = FirebaseAuth.getInstance();
        }
        return sFirebaseAuth;
    }

    public static FirebaseStorage getStorageInstance(){
        if(sFirebaseStorage == null) {
            sFirebaseStorage = FirebaseStorage.getInstance();
        }
        return sFirebaseStorage;
    }

    private FirebaseHelper() {
        if(sInstance != null) {
            throw new RuntimeException("Use getInstance() to get the single instance of this class.");
        }
    }

    public static FirebaseHelper getInstance(){
        // Check for the first time
        if(sInstance == null) {
            // Check for the second time
            synchronized (FirebaseHelper.class) {
                // If no Instance available create new One
                if(sInstance == null) {
                    sInstance = new FirebaseHelper();
                }
            }
        }
        return sInstance;
    }

    public void uploadImage(StorageReference reference, Drawable drawable) {
        // Security
        if(sFirebaseStorage == null) return;
        // Upload Image
        Bitmap avatar = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        avatar.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();


        UploadTask uploadTask = reference.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                mImageUploadedListener.onFailure(exception.getMessage().toString());

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
                mImageUploadedListener.onSuccess(downloadUrl);
            }
        });
    }

    public void setOnImageUploadedListener(ImageUploadedListener listener) {
        mImageUploadedListener = listener;
    }

    public interface ImageUploadedListener {
        void onSuccess(Uri imageUrl);
        void onFailure(String errorMessage);
    }

}
