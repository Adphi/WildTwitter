package fr.wcs.wildtwitter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private ArrayList<TweetModel> mTweets;
    private Context mContext;

    public TweetsAdapter(Context context,ArrayList<TweetModel> tweets) {
        mTweets = tweets;
        mContext = context;
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
    }

    @Override
    public int getItemCount() {
        return mTweets.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewAuthor;
        TextView textViewMessage;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewMessage = itemView.findViewById(R.id.textViewMessage);
        }
    }
}
