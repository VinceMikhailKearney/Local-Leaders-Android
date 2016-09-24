package industries.muskaqueers.thunderechosaber;

import android.app.Application;
import android.util.Log;

import com.firebase.client.Firebase;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Managers.FirebaseManager;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import io.fabric.sdk.android.Fabric;

/**
 * Created by Andrew on 9/18/16.
 */
public class ThunderEchoSaberApplication extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3nPktRGEgNNDNxCDYnPQH4bw6";
    private static final String TWITTER_SECRET = "51ROaR0TmxPRKwaZQRwv4IwvuRmgMZBS9spHsqL5gPmuaeEZRI";
    public static TwitterAuthConfig authConfig;
    public static Twitter twitter;
    public static TwitterManager twitterManager;
    private static FirebaseManager firebaseManager;
    private static DatabaseManager localDatabaseManager;

    @Override
    public void onCreate() {
        super.onCreate();
        // First thing, lets set up the DB
        this.localDatabaseManager = new DatabaseManager(this);
        // Now set up twitter
        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        twitter = Twitter.getInstance();
        twitterManager = new TwitterManager();
        // Lastly Firebase
        Firebase.setAndroidContext(this);
        this.firebaseManager = new FirebaseManager();
    }

    public static FirebaseManager getFirebaseManager() {
        return firebaseManager;
    }

    public static DatabaseManager getLocalDatabaseManager() {
        return localDatabaseManager;
    }
}
