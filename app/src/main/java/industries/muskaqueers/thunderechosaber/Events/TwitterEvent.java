package industries.muskaqueers.thunderechosaber.Events;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andrewcunningham on 10/5/16.
 */

public class TwitterEvent {

    public TwitterEvent(){}

    public static class RecentTweets {

        private ArrayList<Tweet> recentTweets;

        public RecentTweets(ArrayList<Tweet> recentTweets) {
            this.recentTweets = recentTweets;
        }

        public ArrayList<Tweet> getRecentTweets() {
            return recentTweets;
        }
    }
}
