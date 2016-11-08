package industries.muskaqueers.thunderechosaber;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;

/**
 * Created by vincekearney on 25/09/2016.
 */

public abstract class ParserUtils {
    private static final String TAG = "ParserUtils";
    private static final int TWITTER_ROW_DATA = 13;
    private static final int EMAIL_ROW_DATA = 7;

    /**
     *
     * @param hashMap
     * @param key
     * @return
     */
    public static List<MLADb> getMLAsFromMapNew(HashMap<String, Object> hashMap, String key){
        ArrayList<HashMap> allMLAsJSON = (ArrayList) hashMap.get(key);
        List<MLADb> MLAsFromMap = new ArrayList<>();
        for(HashMap mlaJSON : allMLAsJSON){
            MLADb newMLA = new MLADb();
            newMLA.setMLA_ID(stringFromKey(mlaJSON, "MemberPersonId"));
            newMLA.setFirstName(stringFromKey(mlaJSON, "MemberFirstName"));
            newMLA.setLastName(stringFromKey(mlaJSON, "MemberLastName"));
            newMLA.setImageURL(stringFromKey(mlaJSON, "MemberImgUrl"));
            newMLA.setPartyAbbreviation(stringFromKey(mlaJSON, "PartyAbbreviation"));
            newMLA.setPartyName(stringFromKey(mlaJSON, "PartyName"));
            newMLA.setTitle(stringFromKey(mlaJSON, "MemberTitle"));
            newMLA.setConstituency(stringFromKey(mlaJSON, "ConstituencyName"));
            MLAsFromMap.add(newMLA);
        }
        return MLAsFromMap;
    }

    public static List<Party> getPartiesFromArray(List<Object> partyArray) throws NullPointerException {
        List<Party> allParties = new ArrayList<>();
        for (int i = 0; i < partyArray.size(); i++) {
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
}
