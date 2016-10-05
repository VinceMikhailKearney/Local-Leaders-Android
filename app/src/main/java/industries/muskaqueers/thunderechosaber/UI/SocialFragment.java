package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Events.TwitterEvent;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by Andrew on 9/23/16.
 */

public class SocialFragment extends Fragment {

    private static final String TAG = "Social Fragment";

    private RecyclerView recyclerView;
    private Social_Adapter social_adapter;
    private DatabaseManager databaseManager;

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


        try {
            TwitterManager.getTweetsForUser("@AndyAllen88");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    public void onEvent(TwitterEvent.RecentTweets event) {
        Log.d(TAG, "Recent Tweets event: " + event.getRecentTweets());
        social_adapter = new Social_Adapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(social_adapter);
        social_adapter.setTweetList(event.getRecentTweets());
        social_adapter.notifyDataSetChanged();
    }

}
