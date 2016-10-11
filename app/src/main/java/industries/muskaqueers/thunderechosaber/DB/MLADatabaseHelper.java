package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.List;

import industries.muskaqueers.thunderechosaber.MLA;

/**
 * Created by vincekearney on 22/09/2016.
 */

public class MLADatabaseHelper extends DatabaseManager {

    private static final String TAG = "MLADatabaseHelper";

    public MLADatabaseHelper() {
        setLocalTableName(getLocalDatabase().MLAS_TABLE);
        setLocalColumnId(getLocalDatabase().MLA_ID);
        setLocalColumnConstituency(getLocalDatabase().MLA_CONSTITUENCY);
    }

    /**
     * Method for adding a MLA to the DB
     * Parameters are self explanatory
     *
     * We set the twitter handle and image data blank here as we update after we get the right information
     * @return - Returns a MLA object
     */
    public MLA addMLA(String mlaID, String firstName, String lastName, String imageURL,
                      String partyAbbreviation, String partyName, String title, String constituency) {
        Log.i(TAG, "Adding a MLA to DB");

        // First lets make sure that we haven't already added a MLA with their ID
        if (fetchMlaWithID(mlaID) == null) {
            ContentValues mlaValues = new ContentValues();
            mlaValues.put(getLocalDatabase().MLA_ID, mlaID);
            mlaValues.put(getLocalDatabase().MLA_FIRST_NAME, firstName);
            mlaValues.put(getLocalDatabase().MLA_LAST_NAME, lastName);
            mlaValues.put(getLocalDatabase().MLA_IMAGE_URL, imageURL);
            mlaValues.put(getLocalDatabase().MLA_IMAGE_BITMAP, new byte[]{}); // Image bitmap is updated later
            mlaValues.put(getLocalDatabase().MLA_PARTY_ABBREVIATION, partyAbbreviation);
            mlaValues.put(getLocalDatabase().MLA_PARTY_NAME, partyName);
            mlaValues.put(getLocalDatabase().MLA_TITLE, title);
            mlaValues.put(getLocalDatabase().MLA_TWITTER_HANDLE, ""); // The twitter handle is updated later
            mlaValues.put(getLocalDatabase().MLA_CONSTITUENCY, constituency);

            add(mlaValues);
        }

        return fetchMlaWithID(mlaID);
    }

    /**
     * Method for updating the twitter handle of an MLA in the DB
     * @param mla - The MLA we are updating
     * @param handle - Twitter handle for MLA
     */
    public void updateTwitterHandle(MLA mla, String handle) {

        Log.i(TAG, "\nMLA ID -> " + mla.getMLA_ID() + "\nHandle ->" + handle + "\n");

        String sqlSearch = String.format("%s = %s%s%s",getLocalDatabase().MLA_ID,"'",mla.getMLA_ID(),"'");
        update(newValues(getLocalDatabase().MLA_TWITTER_HANDLE, handle), sqlSearch);
    }

    /**
     * Method for updating the Image Data of an MLA in the DB
     * @param mla - The MLA we are updating
     * @param byteArray - The byteArray we fetched from the URL
     */
    public void updateImageData(MLA mla, byte[] byteArray) {

        Log.i(TAG, "\nMLA ID -> " + mla.getMLA_ID() + "\nByte Array ->" + byteArray + "\n");

        String sqlSearch = String.format("%s = %s%s%s",getLocalDatabase().MLA_ID,"'",mla.getMLA_ID(),"'");
        update(newValues(getLocalDatabase().MLA_IMAGE_BITMAP, byteArray), sqlSearch);
    }

    /* ---- Fetch/Delete MLA ---- */

    /**
     * Convenience method to return a MLA with a matching ID
     * @param id - ID of the MLA we are fetching
     * @return - MLA
     */
    public MLA fetchMlaWithID(String id) {
        return (MLA) fetchOrDeleteWithId(id, DatabaseManager.getOrDelete.FETCH);
    }

    /**
     * Convenience method that just makes it easier to delete a MLA
     * @param id - ID of the MLA we want to delete
     */
    public void deleteMLA(String id) {
        fetchOrDeleteWithId(id, DatabaseManager.getOrDelete.DELETE);
    }

    @Override
    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        list.add(createObjectFrom(cursor));
        return list;
    }

    @Override
    public Object createObjectFrom(Cursor cursor) {
        MLA MLA = new MLA();
        MLA.setMLA_ID(cursor.getString(0));
        MLA.setFirstName(cursor.getString(1));
        MLA.setLastName(cursor.getString(2));
        MLA.setImageURL(cursor.getString(3));
        byte[] imageData = cursor.getBlob(4);
        if(imageData.length != 0)
            MLA.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        MLA.setPartyAbbreviation(cursor.getString(5));
        MLA.setPartyName(cursor.getString(6));
        MLA.setTitle(cursor.getString(7));
        MLA.setTwitterHandle(cursor.getString(8));
        MLA.setConstituency(cursor.getString(9));
        return MLA;
    }
}
