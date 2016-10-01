package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.ThunderEchoSaberApplication;

/**
 * Created by vincekearney on 22/09/2016.
 */

public class MLADatabaseHelper {

    private static final String TAG = "MLADatabaseHelper";
    public enum getOrDelete {FETCH, DELETE}
    private static DatabaseManager localDB;

    /**
     * Constructor for setting up the database helper in app
     */
    public MLADatabaseHelper() {
        Log.i(TAG, "Setting up MLADatabaseHelper in app");
        this.localDB = ThunderEchoSaberApplication.getLocalDatabaseManager();
    }

    /**
     * Constructor for setting up the database helper for testing
     *
     * @param manager - When running a test we don't set up the application, therefore we need to set the localDB manually
     */
    public MLADatabaseHelper(DatabaseManager manager) {
        Log.i(TAG, "Setting up MLADatabaseHelper for testing");
        this.localDB = manager;
    }

    /**
     * Method for adding a MLA to the DB
     * Parameters are self explanatory
     *
     * @return - Returns a MLA object
     */
    public MLA addMLA(String mlaID, String firstName, String lastName, String imageURL,
                      String partyAbbreviation, String partyName, String title, String constituency) {
        Log.i(TAG, "Adding a MLA to DB");

        // First lets make sure that we haven't already added a MLA with their ID
        if (fetchMLA(mlaID) == null) {
            ContentValues mlaValues = new ContentValues();
            mlaValues.put(localDB.COLUMN_NAME_MLA_ID, mlaID);
            mlaValues.put(localDB.COLUMN_NAME_MLA_FIRST_NAME, firstName);
            mlaValues.put(localDB.COLUMN_NAME_MLA_LAST_NAME, lastName);
            mlaValues.put(localDB.COLUMN_NAME_MLA_IMAGE_URL, imageURL);
            mlaValues.put(localDB.COLUMN_NAME_MLA_IMAGE_BITMAP, new byte[]{}); // Image bitmap is updated later
            mlaValues.put(localDB.COLUMN_NAME_MLA_PARTY_ABBREVIATION, partyAbbreviation);
            mlaValues.put(localDB.COLUMN_NAME_MLA_PARTY_NAME, partyName);
            mlaValues.put(localDB.COLUMN_NAME_MLA_TITLE, title);
            mlaValues.put(localDB.COLUMN_NAME_MLA_TWITTER_HANDLE, ""); // The twitter handle is updated later
            mlaValues.put(localDB.COLUMN_NAME_MLA_CONSTITUENCY, constituency);

            // Open the db - I.e. return a writeable instance of the database so that we can save to it
            openThisDB().insert(localDB.MLAS_TABLE, null, mlaValues);
            // Close the database - Back to readable
            closeDBManger();
        }

        return fetchMLA(mlaID);
    }

    public void updateTwitterHandle(MLA mla, String handle) {
        Log.i(TAG, "\nMLA ID -> " + mla.getMLA_ID() + "\nHandle ->" + handle + "\n");
        ContentValues newTwitterHandle = new ContentValues();
        newTwitterHandle.put(localDB.COLUMN_NAME_MLA_TWITTER_HANDLE, handle);

        String sqlSearch = String.format("%s = %s%s%s",localDB.COLUMN_NAME_MLA_ID,"'",mla.getMLA_ID(),"'");
        openThisDB().update(localDB.MLAS_TABLE, newTwitterHandle, sqlSearch, null);
    }

    public void updateImageData(MLA mla, byte[] byteArray) {
        Log.i(TAG, "\nMLA ID -> " + mla.getMLA_ID() + "\nByte Array ->" + byteArray + "\n");
        ContentValues newImageData = new ContentValues();
        newImageData.put(localDB.COLUMN_NAME_MLA_IMAGE_BITMAP, byteArray);

        String sqlSearch = String.format("%s = %s%s%s",localDB.COLUMN_NAME_MLA_ID,"'",mla.getMLA_ID(),"'");
        openThisDB().update(localDB.MLAS_TABLE, newImageData, sqlSearch, null);
    }

    /* ---- Fetch/Delete MLA ---- */
    public MLA fetchMLA(String id) {
        return fetchOrDelete(id, getOrDelete.FETCH);
    }

    public void deleteMLA(String id) {
        fetchOrDelete(id, getOrDelete.DELETE);
    }

    public MLA fetchOrDelete(String id, getOrDelete state) {
        MLA item = null;
        Cursor cursor = fetchCursorForMLA(id);

        if (cursor.getCount() == 0)
            return null;

        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String mlaID = cursor.getString(0);
            if (state == getOrDelete.DELETE) {
                Log.i(TAG, "Deleting MLA with ID: " + id);
                openThisDB().delete(localDB.MLAS_TABLE, localDB.COLUMN_NAME_MLA_ID + " = \"" + mlaID + "\"", null);
            } else {
                item = createMLAFrom(cursor);
                Log.i(TAG, "Got MLA with ID: " + mlaID);
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
    public List<MLA> getAllMLAs() {
        Log.i(TAG, "Asking for all MLA items.");
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + localDB.MLAS_TABLE;
        return fetchMLAsWithQuery(query);
    }

    private List<MLA> fetchMLAsWithQuery(String query) {
        List<MLA> allMLAs = new ArrayList<>();
        Cursor cursor = openThisDB().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            MLA MLA = createMLAFrom(cursor);
            allMLAs.add(MLA);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
        return allMLAs;
    }

    /* ---- Public Delete Methods ---- */
    public void deleteAllMLAs() {
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + localDB.MLAS_TABLE;
        Cursor cursor = openThisDB().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String mlaID = cursor.getString(0);
            Log.i(TAG, "Deleting all MLAs. This ID - " + mlaID);
            openThisDB().delete(localDB.MLAS_TABLE, localDB.COLUMN_NAME_MLA_ID + " = \"" + mlaID + "\"", null);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
    }

    /* ---- Private Convenience Methods ---- */
    private MLA createMLAFrom(Cursor cursor) {
        MLA MLA = new MLA();
        MLA.setMLA_ID(cursor.getString(0));
        MLA.setFirstName(cursor.getString(1));
        MLA.setLastName(cursor.getString(2));
        MLA.setImageURL(cursor.getString(3));
        byte[] imageData = cursor.getBlob(4);
        MLA.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        MLA.setPartyAbbreviation(cursor.getString(5));
        MLA.setPartyName(cursor.getString(6));
        MLA.setTitle(cursor.getString(7));
        MLA.setTwitterHandle(cursor.getString(8));
        MLA.setConstituency(cursor.getString(9));
        return MLA;
    }

    private SQLiteDatabase openThisDB() {
        return localDB.getWritableDatabase();
    }

    private void closeDBManger() {
        // Need to close down the DatabaseManager (SQLiteOpenHelper).
        localDB.close();
    }

    private Cursor fetchCursorForMLA(String id) {
        String searchString = String.format("%s%s%s", "'", id, "'");
        String query = "SELECT * FROM " + localDB.MLAS_TABLE + " WHERE " + localDB.COLUMN_NAME_MLA_ID + " = " + searchString;
        return openThisDB().rawQuery(query, null);
    }
}
