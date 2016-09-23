package industries.muskaqueers.thunderechosaber;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Twitter Manager
 * <p/>
 * This class can be used to make all of our API calls.
 */
public class TwitterManager {
    private static final String TAG = "TwitterManager";

    /**
     * Get Tweets for specific person
     *
     * @param username - username of which tweets will be found for. If the user has typed in '@' we
     *                 will rip this out in order to get the correct results.
     * @throws IOException
     */
    public static void getTweetsForUser(String username) throws IOException {
        if (username.charAt(0) == '@') {
            username = username.substring(1);
        }

        final List<Tweet> tweetList = new ArrayList<>();
        SearchService searchService = ThunderEchoSaberApplication.twitter.core.getApiClient().getSearchService();
        Call<Search> call = searchService.tweets(username, null, null, null, null, null, null, null, null, null);
        call.enqueue(new Callback<Search>() {
                         @Override
                         public void success(Result<Search> result) {
                             final List<Tweet> tweets = result.data.tweets;
                             for (Tweet tweet : tweets) {
                                 tweetList.add(tweet);
                             }
//                             MainActivity.fillOutList(tweetList);
                         }

                         @Override
                         public void failure(TwitterException exception) {
                             exception.printStackTrace();
                         }
                     }
        );
    }

    /**
     * Tweet a user
     *
     * @param context  - We need to provide a context in order to show the TweetComposer.
     * @param username - If the user has not typed in '@' we  will add this in order contact the
     *                 correct user.
     */
    public static void tweetUser(Context context, String username) {
        if (username.charAt(0) != '@')
            username = "@" + username;
        TweetComposer.Builder builder = new TweetComposer.Builder(context)
                .text("." + username + " ");
        builder.show();
    }
}
