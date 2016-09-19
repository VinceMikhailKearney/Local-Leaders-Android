package industries.muskaqueers.thunderechosaber;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Andrew on 9/18/16.
 */
public class ThunderEchoSaberApplication extends Application
{
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "3nPktRGEgNNDNxCDYnPQH4bw6";
    private static final String TWITTER_SECRET = "51ROaR0TmxPRKwaZQRwv4IwvuRmgMZBS9spHsqL5gPmuaeEZRI";
    public static TwitterAuthConfig authConfig;
    public static Twitter twitter;
    public static TwitterManager twitterManager;

    @Override
    public void onCreate() {
        super.onCreate();
        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        twitter = Twitter.getInstance();
        twitterManager = new TwitterManager();
    }
}
