package fr.wcs.wildtwitter.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import fr.wcs.wildtwitter.Controllers.TweetController;
import fr.wcs.wildtwitter.Models.TweetModel;
import fr.wcs.wildtwitter.R;
import fr.wcs.wildtwitter.UI.Activities.ImageFullScreenActivity;
import fr.wcs.wildtwitter.UI.Adapters.TweetsAdapter;
import fr.wcs.wildtwitter.Utils.Constants;


public class TweetsFragment extends Fragment {

    private final String TAG = Constants.TAG;

    private TweetsAdapter mTweetsAdapter;
    private ArrayList<TweetModel> mTweets = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewTweets);

        TweetController tweetController = TweetController.getInstance();
        mTweets = tweetController.getTweets();

        mTweetsAdapter = new TweetsAdapter(getActivity(), mTweets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mTweetsAdapter);

        mTweetsAdapter.setOnImageClickedListener(new TweetsAdapter.ImageClickedListener() {
            @Override
            public void onImageClickedListener(String imageUrl) {
                Intent intent = new Intent(getActivity(), ImageFullScreenActivity.class);
                intent.putExtra("ImageUrl", imageUrl);
                startActivity(intent);
            }
        });

        tweetController.setTweetsListener(new TweetController.TweetsListener() {
            @Override
            public void onTweetsChanged(ArrayList<TweetModel> tweets) {
                mTweets = tweets;
                mTweetsAdapter.notifyDataSetChanged();
            }
        });

        return rootView;
    }
}
