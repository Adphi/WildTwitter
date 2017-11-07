package fr.wcs.wildtwitter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class TweetsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewTweets);

        ArrayList<TweetModel> tweets = new ArrayList<>();
        tweets.add(new TweetModel("AZERTY", "", "C'est vrachement cool", "zerzer"));
        tweets.add(new TweetModel("df", "", "RKIF? cool", ""));
        tweets.add(new TweetModel("DCVO", "", "DSpodfbk", "zee"));
        tweets.add(new TweetModel("DOO", "","DLKJV AEPDS", ""));
        tweets.add(new TweetModel("DOKVX", "","C'est vrachement cool", ""));
        tweets.add(new TweetModel("DDD", "","Dofjzpfcmomqjfdq", ""));
        tweets.add(new TweetModel("AZERTY", "", "C'est vrachement cool", ""));
        tweets.add(new TweetModel("df", "", "RKIF? cool", ""));
        tweets.add(new TweetModel("DCVO", "", "DSpodfbk", ""));
        tweets.add(new TweetModel("DOO", "","DLKJV AEPDS", ""));
        tweets.add(new TweetModel("DOKVX", "","C'est vrachement cool", ""));
        tweets.add(new TweetModel("DDD", "","Dofjzpfcmomqjfdq", ""));
        tweets.add(new TweetModel("AZERTY", "", "C'est vrachement cool", ""));
        tweets.add(new TweetModel("df", "", "RKIF? cool", ""));
        tweets.add(new TweetModel("DCVO", "", "DSpodfbk", ""));
        tweets.add(new TweetModel("DOO", "","DLKJV AEPDS", ""));
        tweets.add(new TweetModel("DOKVX", "","C'est vrachement cool", ""));
        tweets.add(new TweetModel("DDD", "","Dofjzpfcmomqjfdq", ""));
        tweets.add(new TweetModel("AZERTY", "", "C'est vrachement cool", ""));
        tweets.add(new TweetModel("df", "", "RKIF? cool", ""));
        tweets.add(new TweetModel("DCVO", "", "DSpodfbk", ""));
        tweets.add(new TweetModel("DOO", "","DLKJV AEPDS", ""));
        tweets.add(new TweetModel("DOKVX", "","C'est vrachement cool", ""));
        tweets.add(new TweetModel("DDD", "","Dofjzpfcmomqjfdq", ""));


        TweetsAdapter tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(tweetsAdapter);

        return rootView;
    }
}
