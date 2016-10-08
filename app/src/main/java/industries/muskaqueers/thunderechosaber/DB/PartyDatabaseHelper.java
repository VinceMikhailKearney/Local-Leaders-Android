package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.ThunderEchoSaberApplication;

/**
 * Created by vincekearney on 06/10/2016.
 */

public class PartyDatabaseHelper {

    private static final String TAG = "PartyDatabaseHelper";
    private static DatabaseManager localDB;
    public enum getOrDelete {FETCH, DELETE}

    public PartyDatabaseHelper() {
        Log.i(TAG, "Setting up PartyDatabaseHelper in app");
        this.localDB = ThunderEchoSaberApplication.getLocalDatabaseManager();
    }

    /**
     * Constructor for setting up the database helper for testing
     *
     * @param manager - When running a test we don't set up the application, therefore we need to set the localDB manually
     */
    public PartyDatabaseHelper(DatabaseManager manager) {
        Log.i(TAG, "Setting up PartyDatabaseHelper for testing");
        this.localDB = manager;
    }

    public Party addParty(String id, String name, String twitter, String url) {
        Log.i(TAG, "addParty: Adding new party to DB. Name = " + name);

        if(fetchParty(id) == null) {
            ContentValues partyValues = new ContentValues();
            partyValues.put(localDB.PARTY_ID, id);
            partyValues.put(localDB.PARTY_NAME, name);
            partyValues.put(localDB.PARTY_TWITTER_HANDLE, twitter);
            partyValues.put(localDB.PARTY_IMAGE_URL, url);

            openThisDB().insert(localDB.PARTY_TABLE, null, partyValues);
            closeDBManger();
        }

        return fetchParty(id);
    }

    /**
     * Method for updating the Image Data of an Party in the DB
     * @param party - The Party we are updating
     * @param byteArray - The byteArray we fetched from the URL
     */
    public void updateImageData(Party party, byte[] byteArray) {
        Log.i(TAG, "\nParty ID -> " + party.getPartyId() + "\nByte Array ->" + byteArray + "\n");
        ContentValues newImageData = new ContentValues();
        newImageData.put(localDB.PARTY_IMAGE_DATA, byteArray);

        String sqlSearch = String.format("%s = %s%s%s",localDB.PARTY_ID,"'",party.getPartyId(),"'");
        openThisDB().update(localDB.PARTY_TABLE, newImageData, sqlSearch, null);
        closeDBManger();
    }

    /* ---- Fetch/Delete Party ---- */

    /**
     * Convenience method to return a Party with a matching ID
     * @param id - ID of the Party we are fetching
     * @return - Party
     */
    public Party fetchParty(String id) {
        return fetchOrDelete(id, PartyDatabaseHelper.getOrDelete.FETCH);
    }

    /**
     * Convenience method that just makes it easier to delete a Party
     * @param id - ID of the Party we want to delete
     */
    public void deleteParty(String id) {
        fetchOrDelete(id, PartyDatabaseHelper.getOrDelete.DELETE);
    }

    /**
     * Convenience method that allows us to either fetch or delete a single Party in the DB
     * @param id - ID of the Party
     * @param state - Whether to Fetch of Delete the Party
     * @return - Returns the Party if we fetch or nothing if the ID does not match anything in the DB. If we are deleting we return nothing.
     */
    public Party fetchOrDelete(String id, PartyDatabaseHelper.getOrDelete state) {
        Party party = null;
        Cursor cursor = fetchCursorForParty(id);

        if (cursor.getCount() == 0)
            return null;

        if (cursor.moveToFirst() && cursor.getCount() == 1) {
            String partyID = cursor.getString(0);
            if (state == PartyDatabaseHelper.getOrDelete.DELETE) {
                Log.i(TAG, "Deleting Party with ID: " + id);
                openThisDB().delete(localDB.PARTY_TABLE, localDB.PARTY_ID + " = \"" + partyID + "\"", null);
            } else {
                party = createPartyFrom(cursor);
                Log.i(TAG, "Got Party with ID: " + partyID);
            }
        } else // We didn't fetch just ONE item - Which we of course expect to.
        {
            throw new IllegalStateException("Only supposed to fetch one. Count was -> " + cursor.getCount());
        }

        cursor.close();
        closeDBManger();
        return party;
    }

    /* ---- Fetch methods ---- */

    /**
     * Method for retrieving all Parties from DB
     * @return - Array of all Parties
     */
    public List<Party> getAllParties() {
        Log.i(TAG, "Asking for all Party items.");
        // Query sets to select ALL from the To-Do table.
        String query = "SELECT * FROM " + localDB.PARTY_TABLE;
        return fetchPartiesWithQuery(query);
    }

    /**
     * Convenience method for fetching parties from DB with respect to a query
     * @param query - The SQL query we wish to execute
     * @return - An array of parties that matches the query
     */
    private List<Party> fetchPartiesWithQuery(String query) {
        List<Party> allParties = new ArrayList<>();
        Cursor cursor = openThisDB().rawQuery(query, null);
        // Starting at the first row, continue to move until past the last row.
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Party party = createPartyFrom(cursor);
            allParties.add(party);
            cursor.moveToNext();
        }

        cursor.close();
        closeDBManger();
        return allParties;
    }

    /* ---- Private Convenience Methods ---- */

    /**
     * Creates a Party with the values contained in a cursor
     * @param cursor - The curosr containing the information of the Party we are creating
     * @return - Party
     */
    private Party createPartyFrom(Cursor cursor) {
        Party party = new Party();
        party.setPartyId(cursor.getString(0));
        party.setName(cursor.getString(1));
        party.setTwitterHandle(cursor.getString(2));
        party.setImageURL(cursor.getString(3));
//        byte[] imageData = cursor.getBlob(4);
//        party.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        return party;
    }

    /**
     * @return A writeable instance of the DatabaseManager
     */
    private SQLiteDatabase openThisDB() {
        return localDB.getWritableDatabase();
    }

    private void closeDBManger() {
        // Need to close down the DatabaseManager (SQLiteOpenHelper).
        localDB.close();
    }

    /**
     * @param id - The id of the Party that we are fetching
     * @return A cursor that contains the information corresponding to the party with the id passed in as a parameter
     */
    private Cursor fetchCursorForParty(String id) {
        String searchString = String.format("%s%s%s", "'", id, "'");
        String query = "SELECT * FROM " + localDB.PARTY_TABLE + " WHERE " + localDB.PARTY_ID + " = " + searchString;
        return openThisDB().rawQuery(query, null);
    }
}
