package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.TwitterEvent;
import industries.muskaqueers.thunderechosaber.R;
import industries.muskaqueers.thunderechosaber.Utils.TwitterThread;

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

        TwitterThread twitterThread = new TwitterThread("@AndyAllen88");
        twitterThread.start();

        return view;
    }

    public void onEvent(TwitterEvent.RecentTweets event) {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        socialAdapter = new SocialAdapter();
        recyclerView.setAdapter(socialAdapter);
        socialAdapter.setTweetList(event.getRecentTweets());
        socialAdapter.notifyDataSetChanged();
    }

}
