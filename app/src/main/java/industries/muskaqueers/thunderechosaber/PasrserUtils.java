package industries.muskaqueers.thunderechosaber;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Created by vincekearney on 25/09/2016.
 */

public abstract class PasrserUtils {
    private static final String TAG = "PasrserUtils";

    /**
     * Get an array of MLAs from map that we get from Firebase
     * @param hashMap - The map that contains the information of all MLAs
     * @param key - The key of what we are looking to retrieve from the hash map (in this case it is 'mlas', which is an array)
     * @return - Array of MLAs from the map that we pass in
     */
    public static List<MLA> getMLAsFromMap(HashMap<String, Object> hashMap, String key) {
        ArrayList<Object> arrayList = (ArrayList) hashMap.get(key);
        List<MLA> allMLAs = new ArrayList<>();
        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, Object> mlaMap = (HashMap) arrayList.get(i);
            MLA newMLA = new MLA();
            newMLA.setMLA_ID(stringFromKey(mlaMap, "MemberPersonId"));
            newMLA.setFirstName(stringFromKey(mlaMap, "MemberFirstName"));
            newMLA.setLastName(stringFromKey(mlaMap, "MemberLastName"));
            newMLA.setImageURL(stringFromKey(mlaMap, "MemberImgUrl"));
            newMLA.setPartyAbbreviation(stringFromKey(mlaMap, "PartyAbbreviation"));
            newMLA.setPartyName(stringFromKey(mlaMap, "PartyName"));
            newMLA.setTitle(stringFromKey(mlaMap, "MemberTitle"));
            newMLA.setConstituency(stringFromKey(mlaMap, "ConstituencyName"));

            allMLAs.add(newMLA);
        }

        return allMLAs;
    }

    public static List<Party> getPartiesFromArray(List<Object> partyArray) throws NullPointerException {
        List<Party> allParties = new ArrayList<>();
        for(int i = 0; i < partyArray.size(); i++) {
            HashMap<String, Object> partyMap = (HashMap) partyArray.get(i);
            Party newParty = new Party();
            // For now the easiest way to access a party in the DB is via it's name. I prefer to interact with the ID attribute.
            /* ToDo - Will improve this later. */
            newParty.setPartyId(stringFromKey(partyMap, "name"));
            newParty.setName(stringFromKey(partyMap, "name"));
            newParty.setTwitterHandle(stringFromKey(partyMap, "twitter_handle"));
            newParty.setImageURL(stringFromKey(partyMap, "image_url"));

            allParties.add(newParty);
        }
        return allParties;
    }

    /**
     * Find the twitter handle for a MLA
     * @param firstName - First name of the MLA
     * @param lastName - Last name of the MLA
     * @return - Twitter handle that matches the full name of the MLA
     */
    public static String findHandleFor(String firstName, String lastName) {
        String twitterHandle = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(ThunderEchoSaberApplication.getAppContext().getResources().openRawResource(R.raw.elected_candidates)));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData[11].equalsIgnoreCase(lastName) && rowData[12].equalsIgnoreCase(firstName)) {
                    twitterHandle = rowData[13];
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

        Log.d(TAG, "findHandleFor: " + firstName + " " + lastName + " = " + twitterHandle);
        return twitterHandle;
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
}
