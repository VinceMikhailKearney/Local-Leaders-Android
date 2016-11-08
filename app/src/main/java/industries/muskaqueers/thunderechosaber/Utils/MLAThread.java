package industries.muskaqueers.thunderechosaber.Utils;

import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;

/**
 * Created by vincekearney on 17/10/2016.
 */

public class MLAThread extends AsyncTask {

    private final static String TAG = "MLA";

    private List<MLADb> mlaArray;

    public MLAThread(List<MLADb> array) {
        this.mlaArray = array;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        for (MLADb mla : mlaArray) {
            if (GreenDatabaseManager.addMLA(mla)) Log.d(TAG, "AAC --> Success");
            else Log.d(TAG, "AAC --> Something went wrong storing the MLA");
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        EventBus.getDefault().post(new NewDatabaseEvent.FinsihedMLAUpdates());

    }
}
