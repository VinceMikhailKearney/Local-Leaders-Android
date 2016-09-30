package industries.muskaqueers.thunderechosaber;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by vincekearney on 25/09/2016.
 */

public abstract class PasrserUtils {
    private static final String TAG = "PasrserUtils";

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

    public static Long versionNumber(HashMap<String, Object> hashMap, String key) {
        return (Long) hashMap.get(key);
    }

    private static String stringFromKey(HashMap<String, Object> map, String key) {
        return map.get(key).toString();
    }

    public static String findHandleFor(String firstName, String lastName) {
        String handle = "";
        BufferedReader reader = new BufferedReader(new InputStreamReader(ThunderEchoSaberApplication.getAppContext().getResources().openRawResource(R.raw.elected_candidates)));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                String[] rowData = line.split(",");
                if (rowData[11].equalsIgnoreCase(lastName) && rowData[12].equalsIgnoreCase(firstName)) {
                    handle = rowData[13];
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

        return handle;
    }
}
