package fr.wcs.wildtwitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.GONE;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private final String TAG = Constants.TAG;

    private ArrayList<TweetModel> mTweets;
    private Context mContext;
    private FirebaseStorage mFirebaseStorage;

    public TweetsAdapter(Context context,ArrayList<TweetModel> tweets) {
        mTweets = tweets;
        mContext = context;
        mFirebaseStorage = FirebaseStorage.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tweet_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        TweetModel tweet = mTweets.get(position);
        holder.textViewAuthor.setText(tweet.getAuthor());
        holder.textViewMessage.setText(tweet.getMessage());
        SimpleDateFormat sfd = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        String date = sfd.format(new Date(tweet.getDate()));
        holder.textViewDate.setText(date);
        if(tweet.getMessageImage().isEmpty()) {
            holder.imageViewImage.setVisibility(GONE);
        }
        else {
            holder.imageViewImage.setVisibility(View.VISIBLE);
            GlideApp.with(mContext)
                    .load(tweet.getMessageImage())
                    .into(holder.imageViewImage);
        }

        GlideApp.with(mContext)
                .load(tweet.getAuthorAvatar())
                .into(holder.avatarView);
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewMessage;
        ImageView imageViewImage;
        CircleImageView avatarView;
        TextView textViewDate;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageViewImage = itemView.findViewById(R.id.imageViewTweet);
            avatarView = itemView.findViewById(R.id.avatarViewTweet);
            textViewDate = itemView.findViewById(R.id.textViewDate);
        }
    }

    private void setImage(ImageView imageView, String url) {

    }
}
