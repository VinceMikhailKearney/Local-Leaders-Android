package industries.muskaqueers.thunderechosaber;

import android.app.Application;
import android.content.Context;

import com.firebase.client.Firebase;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import industries.muskaqueers.thunderechosaber.DB.Database;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.DB.PartyDatabaseHelper;
import industries.muskaqueers.thunderechosaber.Managers.FirebaseManager;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import io.fabric.sdk.android.Fabric;

public class LLApplication extends Application {
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = Credentials.getTwitterKey();
    private static final String TWITTER_SECRET = Credentials.getTwitterSecret();
    private static Context appContext;
    // Twitter
    public static TwitterAuthConfig authConfig;
    public static Twitter twitter;
    public static TwitterManager twitterManager;
    // Local Database
    private static Database database;
    private static MLADatabaseHelper mlaDatabaseHelper;
    private static PartyDatabaseHelper partyDatabaseHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        // First thing, lets set up the DB
        this.database = new Database(this);
        this.mlaDatabaseHelper = new MLADatabaseHelper();
        this.partyDatabaseHelper = new PartyDatabaseHelper();
        // Then set up the context
        this.appContext = getApplicationContext();
        // Now set up twitter
//        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
//        twitter = Twitter.getInstance();
//        twitterManager = new TwitterManager();
        // Lastly Firebase
        Firebase.setAndroidContext(this);
        // We just need to init the FirebaseManager here to pull the information down. We don't need an instance of it.
        new FirebaseManager();
    }

    public static MLADatabaseHelper getLocalMlaHelper() {
        return mlaDatabaseHelper;
    }

    public static PartyDatabaseHelper getLocalPartyHelper() {
        return partyDatabaseHelper;
    }

    public static Database getDatabase() {
        return database;
    }

    public static Context getAppContext() {
        return appContext;
    }
}
