package industries.muskaqueers.thunderechosaber.Utils;

import android.os.AsyncTask;
import android.util.Log;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.NewDB.MLADbDao;
import industries.muskaqueers.thunderechosaber.NewDB.PartyDB;

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
        for (PartyDB partyDB : GreenDatabaseManager.getPartyTable().loadAll()) {
            Log.d(TAG, "AAC --> party name " + partyDB.getName());
//            String firstName = partyDB.getName().substring(0, partyDB.getName().indexOf(" "));
//            String lastName = partyDB.getName().substring(partyDB.getName().indexOf(" "), partyDB.getName().length());
//
//            MLADb mlaDb = mlaDbDao.queryBuilder()
//                    .where(MLADbDao.Properties.FirstName.eq(firstName))
//                    .where(MLADbDao.Properties.LastName.eq(lastName)).unique();
//
//            if (mlaDb != null) {
//                mlaDb.setTwitterHandle(partyDB.getTwitterHandle());
//                mlaDbDao.update(mlaDb);
//            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventBus.getDefault().post(new NewDatabaseEvent.CompletedMLAUpdates());
    }
}
