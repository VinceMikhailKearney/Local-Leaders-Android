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

public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";
    public enum getOrDelete {FETCH, DELETE}
    private String tableName;
    private String columnId;
    private String columnConstituency;

    /**
     * Retrieve the localDB through the getter method.
     * Done this way so that we can also set a Database when running tests
     */
    private static DatabaseManager localDB;
    private static MLADatabaseHelper mlaHelper;
    private static PartyDatabaseHelper partyHelper;

    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        return null;
    }

    public Object createObjectFrom(Cursor cursor) {
        return null;
    }

    public void setLocalDB(DatabaseManager manager) {
        localDB = manager;
        mlaHelper = new MLADatabaseHelper();
        partyHelper = new PartyDatabaseHelper();
    }

    public void setLocalTableName(String table) {
        this.tableName = table;
    }

    public void setLocalColumnId(String id) {
        this.columnId = id;
    }

    public void setLocalColumnConstituency(String columnConstituency) {
        this.columnConstituency = columnConstituency;
    }

    public static MLADatabaseHelper getMlaHelper() {
        if(mlaHelper == null)
            return LLApplication.getLocalMlaHelper();

        return mlaHelper;
    }

    public static PartyDatabaseHelper getPartyHelper() {
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

    public Object fetchOrDelete(String id, DatabaseHelper.getOrDelete state) {
        Object item = null;
        Cursor cursor = cursorUsingID(id);

        if (cursor.getCount() == 0)
            return null;

        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String objectID = cursor.getString(0);
            if (state == DatabaseHelper.getOrDelete.DELETE) {
                openDatabase().delete(this.tableName, this.columnId + " = \"" + objectID + "\"", null);
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
        MLA mla;
        Cursor cursor = cursorUsingConstituency(constituency);

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

    private Cursor cursorUsingID(String id) {
        String searchString = String.format("%s%s%s", "'", id, "'");
        String query = "SELECT * FROM " + this.tableName + " WHERE " + this.columnId + " = " + searchString;
        return createCursor(query);
    }

    private Cursor cursorUsingConstituency(String constituency) {
        String search = String.format("%s%s%s", "'", constituency, "'");
        String query = "SELECT * FROM " + this.tableName + " WHERE " + this.columnConstituency + search;
        return createCursor(query);
    }

    private Cursor createCursor(String query) {
        return openDatabase().rawQuery(query, null);
    }

    public List<Object> getAllObjects() {
        String query = String.format("SELECT * FROM " + this.tableName);
        List<Object> allObjects = new ArrayList<>();
        Cursor cursor = openDatabase().rawQuery(query, null);
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
            openDatabase().delete(this.tableName, this.columnId + " = \"" + objectID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
    }

    public DatabaseManager getLocalDatabase() {
        if(localDB == null)
            localDB = LLApplication.getLocalDatabaseManager();
        return localDB;
    }

    private SQLiteDatabase openDatabase() {
        if (localDB == null)
            localDB = LLApplication.getLocalDatabaseManager();
        return localDB.getWritableDatabase();
    }

    private void closeDatabase() {
        localDB.close();
    }
}
