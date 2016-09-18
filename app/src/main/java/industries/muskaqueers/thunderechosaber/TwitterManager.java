package industries.muskaqueers.thunderechosaber;

import android.util.Log;
import android.widget.TextView;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Twitter Manager
 *
 * This class can be used to make all of our API calls.
 */
public class TwitterManager {

    private static final String TAG = "TwitterManager";

    /**
     * getTweetsForUser
     * @param username - username of which tweets will be found for.
     * @throws IOException
     */
    public static void getTweetsForUser(String username) throws IOException {
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
                             MainActivity.fillOutList(tweetList);
                         }

                         @Override
                         public void failure(TwitterException exception) {
                             exception.printStackTrace();
                         }
                     }

        );
    }
}
