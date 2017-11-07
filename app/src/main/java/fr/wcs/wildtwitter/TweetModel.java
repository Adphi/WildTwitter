package fr.wcs.wildtwitter;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetModel {
    private String mAuthor;
    private String mAuthorAvatar;
    private String mMessage;
    private String mMessageImage;

    public TweetModel(String author, String authorAvatar, String message, String messageImage) {
        mAuthor = author;
        mAuthorAvatar = authorAvatar;
        mMessage = message;
        mMessageImage = messageImage;
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
