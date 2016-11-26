package industries.muskaqueers.thunderechosaber.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.Database.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.Database.PartyDB;

/**
 * Created by vincekearney on 23/10/2016.
 */

public class PartyThread extends AsyncTask {

    private static final String TAG = "PartyThread";

    private List<PartyDB> partyList;
//    private ProcessImage processImage;

    public PartyThread(List<PartyDB> array) {
        this.partyList = array;
//        this.processImage = new ProcessImage(0); // Does not apply here.
    }

    @Override
    protected Object doInBackground(Object[] params) {
        for (PartyDB partyDb : partyList) {
            if (GreenDatabaseManager.addParty(partyDb))
                Log.d(TAG, "Success adding party (" + partyDb.getName() + ")");
            else Log.d(TAG, "Something went wrong storing the Party (" + partyDb.getName() + ")");
//            processImage.getDataFromImage(partyDb.getImageURL(), partyDb.getPartyId(), ProcessImage.type.Party);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventBus.getDefault().post(new NewDatabaseEvent.FinishedPartyUpdates());
    }
}
