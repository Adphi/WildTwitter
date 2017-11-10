package fr.wcs.wildtwitter.Utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by adphi on 10/11/17.
 */

public class FirebaseHelper {
    private static FirebaseDatabase mFirebaseDatabase = null;
    private static FirebaseAuth mFirebaseAuth = null;
    private static FirebaseStorage mFirebaseStorage = null;

    public static FirebaseDatabase getDatabaseInstance(){
        if(mFirebaseDatabase == null) {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
            mFirebaseDatabase.setPersistenceEnabled(true);
        }
        return mFirebaseDatabase;
    }

    public static FirebaseAuth getaAuthInstance(){
        if(mFirebaseAuth == null) {
            mFirebaseAuth = FirebaseAuth.getInstance();
        }
        return mFirebaseAuth;
    }

    public static FirebaseStorage getStorageInstance(){
        if(mFirebaseStorage == null) {
            mFirebaseStorage = FirebaseStorage.getInstance();
        }
        return mFirebaseStorage;
    }

}
