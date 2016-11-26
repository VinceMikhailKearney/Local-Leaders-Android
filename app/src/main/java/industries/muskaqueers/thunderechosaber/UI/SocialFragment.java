package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.twitter.sdk.android.tweetui.TweetView;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.TwitterEvent;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by Andrew on 9/23/16.
 */

public class SocialFragment extends Fragment {

    private static final String TAG = "Social Fragment";

    private RecyclerView recyclerView;
    private SocialAdapter socialAdapter;

    // ---------- Lifecycle Methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        try {
            TwitterManager.getTweetsForUser("@_AndrewAAC");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void onEventMainThread(TwitterEvent.RecentTweets event) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        socialAdapter = new SocialAdapter(event.getRecentTweets());
        recyclerView.setAdapter(socialAdapter);
        socialAdapter.notifyDataSetChanged();
    }

}
