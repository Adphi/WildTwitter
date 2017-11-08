package fr.wcs.wildtwitter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class TweetsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_tweets, container, false);

        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerViewTweets);

        final ArrayList<TweetModel> tweets = new ArrayList<>();
        /*tweets.add(new TweetModel("AZERTY", "","", "C'est vrachement cool", "zerzer", 0L));
        tweets.add(new TweetModel("df", "","", "RKIF? cool", "", 0L));
        tweets.add(new TweetModel("DCVO", "","", "DSpodfbk", "zee", 0L));
        tweets.add(new TweetModel("DOO", "","","DLKJV AEPDS", "", 0L));
        tweets.add(new TweetModel("DOKVX", "","","C'est vrachement cool", "", 0L));
        tweets.add(new TweetModel("DDD", "","","Dofjzpfcmomqjfdq", "", 0L));
        tweets.addAll(tweets);
        tweets.addAll(tweets);*/

        final TweetsAdapter tweetsAdapter = new TweetsAdapter(getActivity(), tweets);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(tweetsAdapter);

        DatabaseReference tweetsReference = FirebaseDatabase.getInstance().getReference("Tweets");
        tweetsReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data : dataSnapshot.getChildren()) {
                    TweetModel tweet = data.getValue(TweetModel.class);
                    if(!tweets.contains(tweet)) {
                        tweets.add(0, tweet);
                    }
                }
                tweetsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return rootView;
    }
}
