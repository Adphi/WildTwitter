package fr.wcs.wildtwitter.Models;

import java.util.ArrayList;

/**
 * Created by adphi on 06/11/17.
 */

public class UserModel {
    private String mName;
    private String mUid;
    private String mAvatarUrl;
    private ArrayList<UserModel> mFollowing = new ArrayList<>();
    private ArrayList<UserModel> mFollowers = new ArrayList<>();
}
