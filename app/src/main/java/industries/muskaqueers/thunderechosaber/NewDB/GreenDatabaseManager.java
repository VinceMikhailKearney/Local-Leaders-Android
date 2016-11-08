package industries.muskaqueers.thunderechosaber.NewDB;


import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.LLApplication;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.Utils.TwitterThread;

/**
 * Created by andrewcunningham on 11/8/16.
 */

public class GreenDatabaseManager {

    private static final String TAG = "GreenDatabaseManager";

    private static DaoSession daoSession;
    private static MLADbDao mlaTable;
    private static PartyDBDao partyTable;

    private boolean mlasUpdated = false;
    private boolean partiesUpdated = false;

    public GreenDatabaseManager(LLApplication application) {
        daoSession = application.getDaoSession();
        mlaTable = daoSession.getMLADbDao();
        partyTable = daoSession.getPartyDBDao();
    }

    public static MLADbDao getMlaTable() { return  mlaTable; }

    public static PartyDBDao getPartyTable() { return partyTable; }

    public static int getMLADBSize() {
        return (int) mlaTable.count();
    }

    public static int getPartyDBSize() {
        return (int) partyTable.count();
    }

    public static boolean addMLA(MLADb mlaDb) {
        mlaTable.insert(mlaDb);
        return true;
    }

    public static boolean addParty(PartyDB partyDB) {
        partyTable.insert(partyDB);
        return true;
    }

    public static void updateTwitterHandles() {
        TwitterThread twitterThread = new TwitterThread();
        twitterThread.run();
    }

    public void onEvent(NewDatabaseEvent.FinsihedMLAUpdates finsihedMLAUpdates) {
        mlasUpdated = true;
        if (partiesUpdated == true){
            updateTwitterHandles();
            mlasUpdated = false;
            partiesUpdated = false;
        }
    }

    public void onEvent(NewDatabaseEvent.FinishedPartyUpdates finishedPartyUpdates){
        partiesUpdated = true;
        if (mlasUpdated == true){
            updateTwitterHandles();
            partiesUpdated = false;
            mlasUpdated = false;
        }
    }

}
