package industries.muskaqueers.thunderechosaber.NewDB;

import android.app.Application;
import android.content.Context;

import industries.muskaqueers.thunderechosaber.DB.DaoSession;
import industries.muskaqueers.thunderechosaber.DB.MLADbDao;
import industries.muskaqueers.thunderechosaber.LLApplication;

/**
 * Created by andrewcunningham on 11/8/16.
 */

public class GreenDatabaseManager {

    private static DaoSession mlaTable;

    public GreenDatabaseManager(LLApplication application){
        mlaTable = application.getDaoSession();
    }

    public static boolean addMLA(MLADb mlaDb){
        mlaTable.insert(mlaDb);
        return true;
    }

}
