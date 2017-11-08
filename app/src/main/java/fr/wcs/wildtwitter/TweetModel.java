package fr.wcs.wildtwitter;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetModel {
    private String mAuthor;
    private String mAuthorUid;
    private String mAuthorAvatar;
    private String mMessage;
    private String mMessageImage;
    private long mDate;

    public TweetModel() {
    }

    public TweetModel(String author, String authorUid, String authorAvatar, String message, String messageImage, long date) {
        mAuthor = author;
        mAuthorUid = authorUid;
        mAuthorAvatar = authorAvatar;
        mMessage = message;
        mMessageImage = messageImage;
        mDate = date;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthorAvatar() {
        return mAuthorAvatar;
    }

    public void setAuthorAvatar(String authorAvatar) {
        mAuthorAvatar = authorAvatar;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    public String getMessageImage() {
        return mMessageImage;
    }

    public void setMessageImage(String messageImage) {
        mMessageImage = messageImage;
    }

    public String getAuthorUid() {
        return mAuthorUid;
    }

    public void setAuthorUid(String authorUid) {
        mAuthorUid = authorUid;
    }

    public long getDate() {
        return mDate;
    }

    public void setDate(long date) {
        mDate = date;
    }

    @Override
    public String toString() {
        return "TweetModel{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mAuthorAvatar='" + mAuthorAvatar + '\'' +
                ", mMessage='" + mMessage + '\'' +
                ", mMessageImage='" + mMessageImage + '\'' +
                '}';
    }
}
