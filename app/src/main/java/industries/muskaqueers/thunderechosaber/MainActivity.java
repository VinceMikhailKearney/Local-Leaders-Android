package industries.muskaqueers.thunderechosaber;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.CollectionTimeline;
import com.twitter.sdk.android.tweetui.SearchTimeline;
import com.twitter.sdk.android.tweetui.Timeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.UserTimeline;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.network.HttpRequest;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static Context context;
    private static EditText username;
    private static Button findTweetsButton;
    private static ListView tweetsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        username = (EditText) findViewById(R.id.usernameEditText);
        findTweetsButton = (Button) findViewById(R.id.findTweetsButton);
        tweetsListView = (ListView) findViewById(R.id.tweetListView);

        findTweetsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!username.getText().toString().trim().isEmpty()) {
                    final List<Tweet> tweets;
                    try {
                        TwitterManager.getTweetsForUser(username.getText().toString().trim());
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please enter valid username", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public static void fillOutList(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            Toast.makeText(username.getContext(), "No tweets for this user", Toast.LENGTH_SHORT).show();
        } else {
            List<String> tweetBodies = new ArrayList<String>();
            for (Tweet tweet : tweets) {
                tweetBodies.add(tweet.text);
            }
            String[] tweetsArr = new String[tweetBodies.size()];
            tweetsArr = tweetBodies.toArray(tweetsArr);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1,
                    tweetsArr);

            tweetsListView.setAdapter(arrayAdapter);
        }

    }

}
