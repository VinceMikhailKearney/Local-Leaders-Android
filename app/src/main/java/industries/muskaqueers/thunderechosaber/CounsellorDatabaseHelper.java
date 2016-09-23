package industries.muskaqueers.thunderechosaber;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by vincekearney on 22/09/2016.
 */

public class CounsellorDatabaseHelper {

    private static final String TAG = "CounsellorDBHelper";
    public enum getOrDelete {FETCH, DELETE}
    private static DatabaseManager localDB;

    public CounsellorDatabaseHelper() {
        this.localDB = ThunderEchoSaberApplication.getLocalDatabaseManager();
    }

    public Counsellor addCounsellor(String name, String age, String hero) {
        Log.i(TAG, "Adding a Counsellor to DB");

        String counsellorId = UUID.randomUUID().toString();
        ContentValues counsellorValues = new ContentValues();
        counsellorValues.put(localDB.COLUMN_NAME_COUNSELLOR_ID, counsellorId);
        counsellorValues.put(localDB.COLUMN_NAME_COUNSELLOR_NAME, name);
        counsellorValues.put(localDB.COLUMN_NAME_COUNSELLOR_PARTY, age);
        counsellorValues.put(localDB.COLUMN_NAME_COUNSELLOR_TITLE, hero);

        // Open the db - I.e. return a writeable instance of the database so that we can save to it
        openThisDB().insert(localDB.COUNSELLORS_TABLE, null, counsellorValues);
        // Close the database - Back to readable
        closeDBManger();

        return counsellor(counsellorId, getOrDelete.FETCH);
    }

    /* ---- Fetch/Delete Counsellor ---- */
    public Counsellor counsellor(String id, getOrDelete state) {
        Counsellor item = null;
        Cursor cursor = fetchCounsellor(id);
        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String counsellorID = cursor.getString(0);
            if (state == getOrDelete.DELETE) {
                Log.i(TAG, "Deleting Counsellor with ID: " + id);
                openThisDB().delete(localDB.COUNSELLORS_TABLE, localDB.COLUMN_NAME_COUNSELLOR_ID + " = \"" + counsellorID + "\"", null);
            } else {
                item = createCounsellorFrom(cursor);
                Log.i(TAG, "Got Counsellor with ID: " + counsellorID);
            }
        } else // We didn't fetch just ONE item - Which we of course expect to.
        {
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        }

        cursor.close();
        closeDBManger();
        return item;
    }

    /* ---- Fetch methods ---- */
    public List<Counsellor> getAllCounsellors() {
        Log.i(TAG, "Asking for all ToDo items.");
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + localDB.COUNSELLORS_TABLE;
        return fetchCounsellorsWithQuery(query);
    }

    private List<Counsellor> fetchCounsellorsWithQuery(String query) {
        List<Counsellor> allCounsellors = new ArrayList<>();
        Cursor cursor = openThisDB().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Counsellor counsellor = createCounsellorFrom(cursor);
            allCounsellors.add(counsellor);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
        return allCounsellors;
    }

    /* ---- Delete methods ---- */
    public void deleteAllCounsellors() {
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + localDB.COUNSELLORS_TABLE;
        Cursor cursor = openThisDB().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String counsellorID = cursor.getString(0);
            Log.i(TAG, "Deleting all to do's. This ID - " + counsellorID);
            openThisDB().delete(localDB.COUNSELLORS_TABLE, localDB.COLUMN_NAME_COUNSELLOR_ID + " = \"" + counsellorID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
    }

    /* ---- Convenience methods ---- */
    private Counsellor createCounsellorFrom(Cursor cursor) {
        Counsellor counsellor = new Counsellor();
        counsellor.setCounsellorID(cursor.getString(0));
        counsellor.setName(cursor.getString(1));
        counsellor.setAge(cursor.getString(2));
        counsellor.setHero(cursor.getString(3));
        return counsellor;
    }

    private SQLiteDatabase openThisDB() {
        return localDB.getWritableDatabase();
    }

    private void closeDBManger() {
        // Need to close down the DatabaseManager (SQLiteOpenHelper).
        localDB.close();
    }

    public Cursor fetchCounsellor(String id) {
        String searchString = String.format("%s%s%s", "'", id, "'");
        String query = "SELECT * FROM " + localDB.COUNSELLORS_TABLE + " WHERE " + localDB.COLUMN_NAME_COUNSELLOR_ID + " = " + searchString;
        return openThisDB().rawQuery(query, null);
    }
}
