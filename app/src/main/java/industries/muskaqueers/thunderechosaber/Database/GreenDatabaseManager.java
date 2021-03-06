package industries.muskaqueers.thunderechosaber.Database;


import industries.muskaqueers.thunderechosaber.LLApplication;
import industries.muskaqueers.thunderechosaber.Utils.TwitterThread;

/**
 * Created by andrewcunningham on 11/8/16.
 */

public class GreenDatabaseManager {

    private static final String TAG = "GreenDatabaseManager";

    private static DaoSession daoSession;
    private static MLADbDao mlaTable;
    private static PartyDBDao partyTable;

    public GreenDatabaseManager(LLApplication application) {
        daoSession = application.getDaoSession();
        mlaTable = daoSession.getMLADbDao();
        partyTable = daoSession.getPartyDBDao();
    }

    public static MLADbDao getMlaTable() {
        return mlaTable;
    }

    public static PartyDBDao getPartyTable() {
        return partyTable;
    }

    public static int getMLADBSize() {
        return (int) mlaTable.count();
    }

    public static int getPartyDBSize() {
        return (int) partyTable.count();
    }

    public static MLADb getMlaWithName(String firstName, String lastName) {
        return GreenDatabaseManager.getMlaTable().queryBuilder().where(MLADbDao.Properties.FirstName.eq(firstName), MLADbDao.Properties.LastName.eq(lastName)).unique();
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
        twitterThread.execute();
    }

}
