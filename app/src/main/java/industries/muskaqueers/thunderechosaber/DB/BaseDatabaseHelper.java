package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import industries.muskaqueers.thunderechosaber.ThunderEchoSaberApplication;

/**
 * Created by vincekearney on 08/10/2016.
 */

public class BaseDatabaseHelper {

    private static final String TAG = "BaseDatabaseHelper";
    public enum getOrDelete {FETCH, DELETE}

    /**
     * Retrieve the localDB through the getter method.
     * Done this way so that we can also set a Database when running tests
     */
    private static DatabaseManager localDB;

    public String getTableName() {
        return null;
    }

    public String getColumnIdName() {
        return null;
    }

    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        return null;
    }

    public Object createObjectFrom(Cursor cursor) {
        return null;
    }

    public void setLocalDB(DatabaseManager manager) {
        localDB = manager;
    }

    public void add(ContentValues values) {
        // Open the db - I.e. return a writeable instance of the database so that we can save to it
        openDatabase().insert(getTableName(), null, values);
        // Close the database - Back to readable
        closeDatabase();
    }

    public void update(ContentValues values, String search) {
        if(values == null) {
            Log.w(TAG, "update: Seems that we have null values");
            return;
        }
        openDatabase().update(getTableName(), values, search, null);
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

    public Object fetchOrDelete(String id, BaseDatabaseHelper.getOrDelete state) {
        Object item = null;
        Cursor cursor = fetchCursor(id);

        if (cursor.getCount() == 0)
            return null;

        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String objectID = cursor.getString(0);
            if (state == BaseDatabaseHelper.getOrDelete.DELETE) {
                openDatabase().delete(getTableName(), getColumnIdName() + " = \"" + objectID + "\"", null);
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

    private Cursor fetchCursor(String id) {
        String searchString = String.format("%s%s%s", "'", id, "'");
        String query = "SELECT * FROM " + getTableName() + " WHERE " + getColumnIdName() + " = " + searchString;
        return openDatabase().rawQuery(query, null);
    }

    public List<Object> getAllObjects() {
        String query = String.format("SELECT * FROM " + getTableName());
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
        String query = "SELECT * FROM " + getTableName();
        Cursor cursor = openDatabase().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String objectID = cursor.getString(0);
            openDatabase().delete(getTableName(), getColumnIdName() + " = \"" + objectID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDatabase();
    }

    public DatabaseManager getLocalDatabase() {
        if(localDB == null)
            localDB = ThunderEchoSaberApplication.getLocalDatabaseManager();
        return localDB;
    }

    private SQLiteDatabase openDatabase() {
        if (localDB == null)
            localDB = ThunderEchoSaberApplication.getLocalDatabaseManager();
        return localDB.getWritableDatabase();
    }

    private void closeDatabase() {
        localDB.close();
    }
}
