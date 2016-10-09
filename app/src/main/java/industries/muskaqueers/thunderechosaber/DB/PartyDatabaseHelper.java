package industries.muskaqueers.thunderechosaber.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.util.List;

import industries.muskaqueers.thunderechosaber.Party;

/**
 * Created by vincekearney on 06/10/2016.
 */

public class PartyDatabaseHelper extends BaseDatabaseHelper {

    private static final String TAG = "PartyDatabaseHelper";

    @Override
    public String getTableName() {
        return getLocalDatabase().PARTY_TABLE;
    }

    @Override
    public String getColumnIdName() {
        return getLocalDatabase().PARTY_ID;
    }

    public Party addParty(String id, String name, String twitter, String url) {
        Log.i(TAG, "addParty: Adding new party to DB. Name = " + name);

        if(fetchParty(id) == null) {
            ContentValues partyValues = new ContentValues();
            partyValues.put(getLocalDatabase().PARTY_ID, id);
            partyValues.put(getLocalDatabase().PARTY_NAME, name);
            partyValues.put(getLocalDatabase().PARTY_TWITTER_HANDLE, twitter);
            partyValues.put(getLocalDatabase().PARTY_IMAGE_URL, url);
            partyValues.put(getLocalDatabase().PARTY_IMAGE_DATA, new byte[]{});

            add(partyValues);
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

        String sqlSearch = String.format("%s = %s%s%s",getLocalDatabase().PARTY_ID,"'",party.getPartyId(),"'");
        update(newValues(getLocalDatabase().PARTY_IMAGE_DATA, byteArray), sqlSearch);
    }

    /* ---- Fetch/Delete Party ---- */

    /**
     * Convenience method to return a Party with a matching ID
     * @param id - ID of the Party we are fetching
     * @return - Party
     */
    public Party fetchParty(String id) {
        return (Party) fetchOrDelete(id, BaseDatabaseHelper.getOrDelete.FETCH);
    }

    /**
     * Convenience method that just makes it easier to delete a Party
     * @param id - ID of the Party we want to delete
     */
    public void deleteParty(String id) {
        fetchOrDelete(id, BaseDatabaseHelper.getOrDelete.DELETE);
    }

    @Override
    public List<Object> createObjectAndAddToList(List<Object> list, Cursor cursor) {
        list.add(createObjectFrom(cursor));
        return list;
    }

    @Override
    public Object createObjectFrom(Cursor cursor) {
        Party party = new Party();
        party.setPartyId(cursor.getString(0));
        party.setName(cursor.getString(1));
        party.setTwitterHandle(cursor.getString(2));
        party.setImageURL(cursor.getString(3));
        byte[] imageData = cursor.getBlob(4);
        party.setImageBitmap(BitmapFactory.decodeByteArray(imageData, 0, imageData.length));
        return party;
    }
}
