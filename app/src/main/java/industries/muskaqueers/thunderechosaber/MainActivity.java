package industries.muskaqueers.thunderechosaber;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private static Context context;
    private static EditText username;
    private static Button findTweetsButton, tweetUserButton;
    private static ListView tweetsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        username = (EditText) findViewById(R.id.usernameEditText);
        findTweetsButton = (Button) findViewById(R.id.findTweetsButton);
        tweetUserButton = (Button) findViewById(R.id.tweetUserButton);
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
                } else
                    Toast.makeText(MainActivity.this, "Please enter valid username", Toast.LENGTH_SHORT).show();
            }
        });

        tweetUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TwitterManager.tweetUser(context, username.getText().toString().trim());
            }
        });
    }

    public static void fillOutList(List<Tweet> tweets) {
        if (tweets.isEmpty()) {
            Toast.makeText(username.getContext(), "No tweets for this user", Toast.LENGTH_SHORT).show();
        } else {
            List<String> tweetBodies = new ArrayList<String>();
            for (Tweet tweet : tweets)
                tweetBodies.add(tweet.text);

            String[] tweetsArr = new String[tweetBodies.size()];
            tweetsArr = tweetBodies.toArray(tweetsArr);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(context,
                    android.R.layout.simple_list_item_1,
                    tweetsArr);
            tweetsListView.setAdapter(arrayAdapter);
        }
    }

    public static class CounsellorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private Counsellor counsellor;
        private TextView nameTextView, ageTextView, heroTextView;
        private String name, age, hero;

        public CounsellorViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            ageTextView = (TextView) itemView.findViewById(R.id.age);
            heroTextView = (TextView) itemView.findViewById(R.id.hero);
            itemView.setOnClickListener(this);
        }

        public void setName(String name) {
            this.name = name;
            this.nameTextView.setText(name);
        }

        public void setAge(String age) {
            this.age = age;
            this.ageTextView.setText(age);
        }

        public void setHero(String hero) {
            this.hero = hero;
            this.heroTextView.setText(hero);
        }

        public void setCounsellor(Counsellor counsellor) {
            this.counsellor = counsellor;
        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "onClick: ");
        }
    }
}
