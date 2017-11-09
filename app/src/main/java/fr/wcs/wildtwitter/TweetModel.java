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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TweetModel that = (TweetModel) o;

        if (mDate != that.mDate) return false;
        if (mAuthor != null ? !mAuthor.equals(that.mAuthor) : that.mAuthor != null) return false;
        if (mAuthorUid != null ? !mAuthorUid.equals(that.mAuthorUid) : that.mAuthorUid != null)
            return false;
        if (mAuthorAvatar != null ? !mAuthorAvatar.equals(that.mAuthorAvatar) : that.mAuthorAvatar != null)
            return false;
        if (mMessage != null ? !mMessage.equals(that.mMessage) : that.mMessage != null)
            return false;
        return mMessageImage != null ? mMessageImage.equals(that.mMessageImage) : that.mMessageImage == null;
    }

    @Override
    public int hashCode() {
        int result = mAuthor != null ? mAuthor.hashCode() : 0;
        result = 31 * result + (mAuthorUid != null ? mAuthorUid.hashCode() : 0);
        result = 31 * result + (mAuthorAvatar != null ? mAuthorAvatar.hashCode() : 0);
        result = 31 * result + (mMessage != null ? mMessage.hashCode() : 0);
        result = 31 * result + (mMessageImage != null ? mMessageImage.hashCode() : 0);
        result = 31 * result + (int) (mDate ^ (mDate >>> 32));
        return result;
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
