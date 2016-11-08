package industries.muskaqueers.thunderechosaber.Utils;

import android.util.Log;

import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 17/10/2016.
 */

public class MLAThread extends Thread {

    private final static String TAG = "MLA";

    private List<MLADb> mlaArray;

    public MLAThread(List<MLADb> array) {
        this.mlaArray = array;
    }

    public void run()
    {
        for (MLADb mla : mlaArray) {
            DatabaseManager.mlaHelper().addMLA(mla.getMLA_ID(),
                    mla.getFirstName(),
                    mla.getLastName(),
                    mla.getImageURL(),
                    mla.getPartyAbbreviation(),
                    mla.getPartyName(),
                    mla.getTitle(),
                    mla.getConstituency());

            if (GreenDatabaseManager.addMLA(mla)) Log.d(TAG, "AAC --> Success");
            else Log.d(TAG, "AAC --> Something went wrong storing the MLA");

            // Now that the MLA is in the DB, let's update the TwitterHandle
//            DatabaseManager.mlaHelper().updateTwitterHandle(mla, ParserUtils.findHandleFor(mla.getFirstName(), mla.getLastName()));
            // Now that the MLA is in the DB, let's update the EmailAddress
//            DatabaseManager.mlaHelper().updateEmailAddress(mla, ParserUtils.findEmailFor(mla.getFirstName(), mla.getLastName()));
        }

        EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
    }
}
