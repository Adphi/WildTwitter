package fr.wcs.wildtwitter.UI;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.text.style.URLSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import fr.wcs.wildtwitter.GlideApp;
import fr.wcs.wildtwitter.Models.TweetModel;
import fr.wcs.wildtwitter.R;
import fr.wcs.wildtwitter.Utils.Constants;

import static android.view.View.GONE;

/**
 * Created by adphi on 06/11/17.
 */

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private final String TAG = Constants.TAG;

    private ArrayList<TweetModel> mTweets;
    private Context mContext;

    private Handler handler;
    private Runnable handlerTask;

    private ImageClickedListener mImageClickedListener = null;

    public TweetsAdapter(Context context, ArrayList<TweetModel> tweets) {
        mTweets = tweets;
        mContext = context;
        initializeTimer();
    }

    private void initializeTimer(){
        handler = new Handler();
        handlerTask = new Runnable() {
            @Override
            public void run() {
                notifyDataSetChanged();
                handler.postDelayed(handlerTask, DateUtils.MINUTE_IN_MILLIS);
            }
        };
        handlerTask.run();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.tweet_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final TweetModel tweet = mTweets.get(position);
        holder.textViewAuthor.setText(tweet.getAuthor());
        holder.textViewMessage.setText(tweet.getMessage());
        CharSequence date = DateUtils.getRelativeTimeSpanString(tweet.getDate());
        holder.textViewDate.setText(date);

        URLSpan spans[] = holder.textViewMessage.getUrls();
        for(URLSpan span: spans) {
            String sampleUrl = span.getURL();
            if(sampleUrl.contains("http")) {
                Log.d(TAG, "onBindViewHolder: " + sampleUrl);
            }
        }

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

        holder.imageViewImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mImageClickedListener != null) {
                    mImageClickedListener.onImageClickedListener(tweet.getMessageImage());
                }
            }
        });
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

    public void setOnImageClickedListener(ImageClickedListener listener) {
        mImageClickedListener = listener;
    }

    public interface ImageClickedListener {
        void onImageClickedListener(String imageUrl);
    }
}
