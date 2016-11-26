package industries.muskaqueers.thunderechosaber.UI;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.FrameLayout;
import android.widget.ListAdapter;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetView;

import java.util.ArrayList;

import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by andrewcunningham on 11/26/16.
 */

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.LLTweetView> {

    private ArrayList<Tweet> tweetArrayList = new ArrayList<>();

    public SocialAdapter(ArrayList<Tweet> tweetArrayList){
        this.tweetArrayList = tweetArrayList;
    }

    @Override
    public LLTweetView onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tweet, parent, false);
        return new LLTweetView(itemView);
    }

    @Override
    public void onBindViewHolder(LLTweetView holder, int position) {
        holder.bind(tweetArrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return tweetArrayList.size();
    }

    public class LLTweetView extends RecyclerView.ViewHolder{

        private FrameLayout mainLayout;

        public LLTweetView(View itemView) {
            super(itemView);
            mainLayout = (FrameLayout) itemView.findViewById(R.id.frame_layout);
        }

        public void bind(Tweet tweet){
            CompactTweetView tweetView = new CompactTweetView(itemView.getContext(), tweet);
            mainLayout.addView(tweetView);
        }
    }

}
