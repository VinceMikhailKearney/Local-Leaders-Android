package industries.muskaqueers.thunderechosaber;

import android.app.Application;
import android.content.Context;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.Managers.ServerManager;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.Database.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.Database.DaoMaster;
import io.fabric.sdk.android.Fabric;

public class LLApplication extends Application {

    private static final String TAG = "LLApplication";

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = Credentials.getTwitterKey();
    private static final String TWITTER_SECRET = Credentials.getTwitterSecret();
    private static Context appContext;
    // Twitter
    public static TwitterAuthConfig authConfig;
    public static Twitter twitter;
    public static TwitterManager twitterManager;
    // Local Database
    private static industries.muskaqueers.thunderechosaber.Database.DaoSession daoSession;
    private static GreenDatabaseManager databaseManager;

    private boolean mlasUpdated = false;
    private boolean partiesUpdated = false;

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);

        // Let's set up new DB
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "notes-db");
        org.greenrobot.greendao.database.Database db = helper.getWritableDb();
        daoSession = new DaoMaster(db).newSession();
        databaseManager = new GreenDatabaseManager(this);

        this.appContext = getApplicationContext();
        // Now set up twitter
        authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        twitter = Twitter.getInstance();
        twitterManager = new TwitterManager();

        ServerManager serverManager = new ServerManager(this);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        EventBus.getDefault().unregister(this);
    }

    public static Context getAppContext() {
        return appContext;
    }

    public static industries.muskaqueers.thunderechosaber.Database.DaoSession getDaoSession() { return daoSession; }

    public void onEvent(NewDatabaseEvent.FinsihedMLAUpdates finsihedMLAUpdates) {
        mlasUpdated = true;
        if (partiesUpdated == true){
            GreenDatabaseManager.updateTwitterHandles();
            mlasUpdated = false;
            partiesUpdated = false;
        }
    }

    public void onEvent(NewDatabaseEvent.FinishedPartyUpdates finishedPartyUpdates){
        partiesUpdated = true;
        if (mlasUpdated == true){
            GreenDatabaseManager.updateTwitterHandles();
            partiesUpdated = false;
            mlasUpdated = false;
        }
    }
}
