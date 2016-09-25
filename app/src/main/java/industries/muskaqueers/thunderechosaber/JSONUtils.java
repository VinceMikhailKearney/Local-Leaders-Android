package industries.muskaqueers.thunderechosaber;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vincekearney on 25/09/2016.
 */

public class JSONUtils {
    private JSONListener listener;

    public interface JSONListener {
        void CreateMLA(MLA mla);
    }

    public JSONUtils(JSONListener listener) {
        this.listener = listener;
    }

    public void getMLAsFromMap(HashMap<String, Object> hashMap, String key) {
        ArrayList<Object> arrayList = (ArrayList) hashMap.get(key);
        for(int i = 0; i < arrayList.size(); i++) {
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

            listener.CreateMLA(newMLA);
        }
    }

    public Long versionNumber(HashMap<String, Object> hashMap, String key){
        return (Long) hashMap.get(key);
    }

    private String stringFromKey(HashMap<String, Object>  map, String key) {
        return map.get(key).toString();
    }
}
