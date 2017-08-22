package industries.muskaqueers.thunderechosaber;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import industries.muskaqueers.thunderechosaber.Database.MLADb;
import industries.muskaqueers.thunderechosaber.Database.PartyDB;

/**
 * Created by vincekearney on 25/09/2016.
 */

public abstract class ParserUtils {
    private static final String TAG = "ParserUtils";

    private static final String ID = "key";
    private static final String FIRST_NAME = "firstName";
    private static final String LAST_NAME = "lastName";
    private static final String IMAGE_URL = "imageURL";
    private static final String PARTY_ABRV = "party";
    private static final String PARTY_NAME = "partyName";
    private static final String TITLE = "title";
    private static final String CONSTITUENCY = "constituency";
    private static final String TWITTER_HANDLE = "twitter";
    private static final String EMAIL_ADDRESS = "email";

    private static final int TWITTER_ROW_DATA = 13;
    private static final int EMAIL_ROW_DATA = 7;

    public static List<MLADb> getMLAsFromJSONArray(JSONArray jsonArray) {
        ArrayList<MLADb> mlaList = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            MLADb newMLA = new MLADb();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                newMLA.setMLA_ID(stringFromObject(jsonObject, ID));
                newMLA.setFirstName(stringFromObject(jsonObject, FIRST_NAME));
                newMLA.setLastName(stringFromObject(jsonObject, LAST_NAME));
                newMLA.setImageURL(stringFromObject(jsonObject, IMAGE_URL));
                newMLA.setPartyAbbreviation(stringFromObject(jsonObject, PARTY_ABRV));
                newMLA.setPartyName(stringFromObject(jsonObject, PARTY_NAME));
                newMLA.setTitle(stringFromObject(jsonObject, TITLE));
                newMLA.setConstituency(stringFromObject(jsonObject, CONSTITUENCY));
                newMLA.setTwitterHandle(stringFromObject(jsonObject, TWITTER_HANDLE));
                newMLA.setEmailAddress(stringFromObject(jsonObject, EMAIL_ADDRESS));
                mlaList.add(newMLA);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mlaList;
    }

    public static List<PartyDB> getPartiesFromArray(JSONArray jsonArray) throws NullPointerException {
        ArrayList<PartyDB> allParties = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            PartyDB newParty = new PartyDB();
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // For now the easiest way to access a party in the DB is via it's name. I prefer to interact with the ID attribute.
                newParty.setPartyId(stringFromObject(jsonObject, "name"));
                newParty.setName(stringFromObject(jsonObject, "name"));
                newParty.setTwitterHandle(stringFromObject(jsonObject, "twitter_handle"));
                newParty.setImageURL(stringFromObject(jsonObject, "image_url"));
                allParties.add(newParty);
            } catch (JSONException exception) {
                exception.printStackTrace();
            }
        }
        return allParties;
    }

    public static String findHandleFor(String firstName, String lastName) {
        return findDataFor(TWITTER_ROW_DATA, firstName, lastName);
    }

    public static String findEmailFor(String firstName, String lastName) {
        return findDataFor(EMAIL_ROW_DATA, firstName, lastName);
    }

    /**
     * Find the twitter handle for a MLA
     *
     * @param firstName - First name of the MLA
     * @param lastName  - Last name of the MLA
     * @return - Twitter handle/Email that matches the full name of the MLA
     */
    public static String findDataFor(int DATA, String firstName, String lastName) {
        String data = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(LLApplication.getAppContext().getResources().openRawResource(R.raw.elected_candidates)));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData[11].equalsIgnoreCase(lastName) && rowData[12].equalsIgnoreCase(firstName)) {
                    data = rowData[DATA];
                }
            }
        } catch (IOException error) {
            error.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Log.d(TAG, "findDataFor: " + firstName + " " + lastName + " = " + data);
        return data;
    }

    public static Long versionNumber(HashMap<String, Object> hashMap, String key) {
        return (Long) hashMap.get(key);
    }

    /**
     * Convenive method for getting the string value from a key in the map
     */
    private static String stringFromKey(HashMap<String, Object> map, String key) {
        return map.get(key).toString();
    }

    private static String stringFromObject(JSONObject json, String key) {
        String string = "";
        try {
            string = json.get(key).toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return string;
    }

}
