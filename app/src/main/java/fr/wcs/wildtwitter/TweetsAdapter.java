package fr.wcs.wildtwitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import agency.tango.android.avatarview.views.AvatarView;

import static android.view.View.GONE;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

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
        if(tweet.getMessageImage().isEmpty()) {
            holder.imageViewImage.setVisibility(GONE);
        }
        else {
            StorageReference ref = mFirebaseStorage.getReferenceFromUrl("gs://wildtwitter-eb3bc.appspot.com/nikola_tesla_2037575.jpg");
            holder.imageViewImage.setVisibility(View.VISIBLE);
            GlideApp.with(mContext)
                    .load(ref)
                    .into(holder.imageViewImage);
        }
        StorageReference avatarRef = mFirebaseStorage.getReferenceFromUrl("gs://wildtwitter-eb3bc.appspot.com/Avatars/796VNhzEGFc67Q48qbp3hzUc7As1");
        GlideApp.with(mContext)
                .load(avatarRef)
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
        AvatarView avatarView;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
            imageViewImage = itemView.findViewById(R.id.imageViewTweet);
            avatarView = itemView.findViewById(R.id.avatarViewTweet);
        }
    }
}
