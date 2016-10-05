package industries.muskaqueers.thunderechosaber.Events;

import java.util.List;

/**
 * Created by andrewcunningham on 10/5/16.
 */

public class TwitterEvent {

    public TwitterEvent(){}

    public static class RecentTweets {

        private List<String> recentTweets;

        public RecentTweets(List<String> recentTweets) {
            this.recentTweets = recentTweets;
        }

        public List<String> getRecentTweets() {
            return recentTweets;
        }
    }
}
