package industries.muskaqueers.thunderechosaber.Utils;

import android.os.AsyncTask;
import android.util.Log;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.NewDB.MLADbDao;
import industries.muskaqueers.thunderechosaber.NewDB.PartyDB;
import industries.muskaqueers.thunderechosaber.ParserUtils;

/**
 * Created by vincekearney on 24/10/2016.
 */

public class TwitterThread extends AsyncTask {

    private static final String TAG = "TwitterThread";


    public TwitterThread() {
    }

    @Override
    protected Object doInBackground(Object[] params) {
        MLADbDao mlaDbDao = GreenDatabaseManager.getMlaTable();
        for (MLADb mlaDb : mlaDbDao.loadAll()) {
            String twitterHandle = ParserUtils.findHandleFor(mlaDb.getFirstName(), mlaDb.getLastName());
            String emailAddress = ParserUtils.findEmailFor(mlaDb.getFirstName(), mlaDb.getLastName());

            if (mlaDb != null) {
                mlaDb.setTwitterHandle(twitterHandle);
                mlaDb.setEmailAddress(emailAddress);
                mlaDbDao.update(mlaDb);
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventBus.getDefault().post(new NewDatabaseEvent.CompletedMLAUpdates());
    }
}
