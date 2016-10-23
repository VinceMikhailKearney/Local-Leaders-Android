package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import industries.muskaqueers.thunderechosaber.LLApplication;


/**
 * Created by vincekearney on 08/10/2016.
 */

public class DatabaseManager {

    private static final String TAG = "DatabaseManager";
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

    public int size() {
        return this.getAllObjects().size();
    }

    public void add(ContentValues values) {
        // Open the db - I.e. return a writeable instance of the database so that we can save to it
        writeableDatabase().insert(this.tableName, null, values);
    }

    public void update(ContentValues values, String search) {
        if(values == null) {
            Log.w(TAG, "update: Seems that we have null values");
            return;
        }
        writeableDatabase().update(this.tableName, values, search, null);
    }

    public ContentValues newValues(String columnName, Object data) {
        ContentValues values = new ContentValues();
        if (data instanceof String)
            values.put(columnName, (String) data);
        else if (data instanceof byte[])
            values.put(columnName, (byte[]) data);

        return values;
    }

    public void delete(String string) {
        Cursor cursor = cursorUsingString(string);
        if (cursor.moveToFirst() && cursor.getCount() == 1)
            writeableDatabase().delete(this.tableName, this.searchingForString + " = \"" + cursor.getString(0) + "\"", null);
        else
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        cursor.close();
    }

    public Object fetch(String string) {
        Object item;
        Cursor cursor = cursorUsingString(string);
        if (cursor.getCount() == 0)
            return null;
        else if (cursor.moveToFirst() && cursor.getCount() == 1)
            item = createObjectFrom(cursor);
        else
            throw new IllegalStateException("Only meant to return one. Count was == " + cursor.getCount());
        cursor.close();
        return item;
    }

    private Cursor cursorUsingString(String string) {
        String search = String.format("%s%s%s", "'", string, "'");
        String query = "SELECT * FROM " + this.tableName + " WHERE " + this.searchingForString + " = " + search;
        return readableDatabase().rawQuery(query, null);
    }

    public List<Object> getAllObjects() {
        String query = String.format("SELECT * FROM " + this.tableName);
        List<Object> allObjects = new ArrayList<>();
        Cursor cursor = readableDatabase().rawQuery(query, null);

        if(cursor.getCount() == 0)
            return allObjects; // Return the empty array

        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            allObjects = createObjectAndAddToList(allObjects, cursor);
            cursor.moveToNext();
        }

        cursor.close();
        return allObjects;
    }

    public void deleteAll() {
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + this.tableName;
        Cursor cursor = readableDatabase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String objectID = cursor.getString(0);
            writeableDatabase().delete(this.tableName, this.searchingForString + " = \"" + objectID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
    }

    public Database getLocalDatabase() {
        if(localDB == null)
            localDB = LLApplication.getDatabase();
        return localDB;
    }

    private SQLiteDatabase writeableDatabase() {
        if (localDB == null)
            localDB = LLApplication.getDatabase();
        return localDB.getWritableDatabase();
    }

    private SQLiteDatabase readableDatabase() {
        if (localDB == null)
            localDB = LLApplication.getDatabase();
        return localDB.getReadableDatabase();
    }
}
