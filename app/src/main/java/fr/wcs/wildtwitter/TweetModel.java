package fr.wcs.wildtwitter;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetModel {
    private String mAuthor;
    private String mMessage;

    public TweetModel(String author, String message) {
        mAuthor = author;
        mMessage = message;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getMessage() {
        return mMessage;
    }

    public void setMessage(String message) {
        mMessage = message;
    }

    @Override
    public String toString() {
        return "TweetModel{" +
                "mAuthor='" + mAuthor + '\'' +
                ", mMessage='" + mMessage + '\'' +
                '}';
    }
}
