package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import industries.muskaqueers.thunderechosaber.LLApplication;
import industries.muskaqueers.thunderechosaber.MLA;


/**
 * Created by vincekearney on 08/10/2016.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
    public enum getOrDelete {FETCH, DELETE}
    private String tableName;
    private String searchingForString;

    /**
     * Retrieve the localDB through the getter method.
     * Done this way so that we can also set a Database when running tests
     */
    private static Database localDB;
    private static MLADatabaseHelper mlaHelper;
    private static PartyDatabaseHelper partyHelper;

    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        return null;
    }

    public Object createObjectFrom(Cursor cursor) {
        return null;
    }

    public static void setLocalDB(Database manager) {
        localDB = manager;
        mlaHelper = new MLADatabaseHelper();
        partyHelper = new PartyDatabaseHelper();
    }

    public void setLocalTableName(String table) {
        this.tableName = table;
    }

    public void setSearchingForString(String string) {
        this.searchingForString = string;
    }

    public static MLADatabaseHelper mlaHelper() {
        if(mlaHelper == null)
            return LLApplication.getLocalMlaHelper();

        return mlaHelper;
    }

    public static PartyDatabaseHelper partyHelper() {
        if(partyHelper == null)
            return LLApplication.getLocalPartyHelper();

        return partyHelper;
    }

    public void add(ContentValues values) {
        // Open the db - I.e. return a writeable instance of the database so that we can save to it
        openDatabase().insert(this.tableName, null, values);
        // Close the database - Back to readable
        closeDatabase();
    }

    public void update(ContentValues values, String search) {
        if(values == null) {
            Log.w(TAG, "update: Seems that we have null values");
            return;
        }
        openDatabase().update(this.tableName, values, search, null);
        closeDatabase();
    }

    public ContentValues newValues(String columnName, Object data) {
        ContentValues values = new ContentValues();
        if (data instanceof String)
            values.put(columnName, (String) data);
        else if (data instanceof byte[])
            values.put(columnName, (byte[]) data);

        return values;
    }

    public Object fetchOrDeleteWithId(String id, DatabaseManager.getOrDelete state) {
        Object item = null;
        Cursor cursor = cursorUsingString(id);

        if (cursor.getCount() == 0)
            return null;
        else if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String objectID = cursor.getString(0);
            if (state == DatabaseManager.getOrDelete.DELETE) {
                openDatabase().delete(this.tableName, this.searchingForString + " = \"" + objectID + "\"", null);
            } else {
                item = createObjectFrom(cursor);
            }
        } else { // We didn't fetch just ONE item - Which we of course expect to.
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        }

        cursor.close();
        closeDatabase();
        return item;
    }

    public MLA fetchMlaWithConstituency(String constituency) {
        setSearchingForString(getLocalDatabase().MLA_CONSTITUENCY);
        MLA mla;
        Cursor cursor = cursorUsingString(constituency);

        if (cursor.getCount() == 0)
            return null;
        else if (cursor.moveToFirst() && cursor.getCount() == 1)
            mla = (MLA) createObjectFrom(cursor);
        else
            throw new IllegalStateException("Only meant to return one.");

        cursor.close();
        closeDatabase();
        return mla;
    }

    private Cursor cursorUsingString(String string) {
        String search = String.format("%s%s%s", "'", string, "'");
        String query = "SELECT * FROM " + this.tableName + " WHERE " + this.searchingForString + " = " + search;
        return createCursor(query);
    }

    private Cursor createCursor(String query) {
        return openDatabase().rawQuery(query, null);
    }

    public List<Object> getAllObjects() {
        String query = String.format("SELECT * FROM " + this.tableName);
        List<Object> allObjects = new ArrayList<>();
        Cursor cursor = openDatabase().rawQuery(query, null);

        if(cursor.getCount() == 0)
            return allObjects; // Return the empty array

        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allObjects = createObjectAndAddToList(allObjects, cursor);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
        return allObjects;
    }

    public void deleteAll() {
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + this.tableName;
        Cursor cursor = openDatabase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String objectID = cursor.getString(0);
            openDatabase().delete(this.tableName, this.searchingForString + " = \"" + objectID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
    }

    public Database getLocalDatabase() {
        if(localDB == null)
            localDB = LLApplication.getDatabase();
        return localDB;
    }

    private SQLiteDatabase openDatabase() {
        if (localDB == null)
            localDB = LLApplication.getDatabase();
        return localDB.getWritableDatabase();
    }

    private void closeDatabase() {
        localDB.close();
    }
}
