package industries.muskaqueers.thunderechosaber.Utils;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.NewDB.MLADbDao;
import industries.muskaqueers.thunderechosaber.NewDB.PartyDB;

/**
 * Created by vincekearney on 24/10/2016.
 */

public class TwitterThread extends Thread {


    public TwitterThread() {
    }

    public void run() {
        MLADbDao mlaDbDao = GreenDatabaseManager.getMlaTable();
        for (PartyDB partyDB : GreenDatabaseManager.getPartyTable().loadAll()) {
            String firstName = partyDB.getName().substring(0, partyDB.getName().indexOf(" "));
            String lastName = partyDB.getName().substring(partyDB.getName().indexOf(" "), partyDB.getName().length());

            MLADb mlaDb = mlaDbDao.queryBuilder()
                    .where(MLADbDao.Properties.FirstName.eq(firstName))
                    .where(MLADbDao.Properties.LastName.eq(lastName)).unique();

            if (mlaDb != null) {
                mlaDb.setTwitterHandle(partyDB.getTwitterHandle());
                mlaDbDao.update(mlaDb);
            }
        }
        EventBus.getDefault().post(new NewDatabaseEvent.CompletedMLAUpdates());
    }
}
