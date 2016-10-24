package industries.muskaqueers.thunderechosaber.Managers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.Utils.MLAThread;
import industries.muskaqueers.thunderechosaber.Utils.PartyThread;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseMlaReference;
    public DatabaseReference firebasePartyReference;
    private int i;

    public FirebaseManager() {
        // Register on the EventBus
        EventBus.getDefault().register(this);
        i = 0;

        this.firebaseMlaReference = FirebaseDatabase.getInstance().getReference("MLASJSON");
        this.firebaseMlaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.w(TAG, "onDataChange MLA: " + "Did not ask for the right data. Check the getReference() method");
                    return;
                }

                HashMap dataSnapShotMap = (HashMap) dataSnapshot.getValue();
                Log.d(TAG, "onDataChange MLA: Version = " + ParserUtils.versionNumber(dataSnapShotMap, "version"));

                List<Object> array = (List) dataSnapShotMap.get("mlas");
                DatabaseManager.mlaHelper().setTotalMlaCount(array.size());
                if (DatabaseManager.mlaHelper().size() == array.size())
                    return; // This is hardcoded right now just to save myself bother. We really ought to sort this out properly

                ParserUtils.getMLAsFromMap(dataSnapShotMap, "mlas");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value MLA --> " + databaseError);
            }
        });

        this.firebasePartyReference = FirebaseDatabase.getInstance().getReference("parties");
        this.firebasePartyReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.w(TAG, "onDataChange Party: " + "Did not ask for the right data. Check the getReference() method");
                    return;
                }

                List<Object> dataSnapShotArray = (List) dataSnapshot.getValue();
                if(DatabaseManager.partyHelper().size() == dataSnapShotArray.size())
                    return;

                try {
                    addPartiesToDatabase(ParserUtils.getPartiesFromArray(dataSnapShotArray));
                } catch (NullPointerException e) {
                    Log.w(TAG, "onDataChange: Well something fucked up",e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void onEvent(DatabaseEvent event) {
        if(event.getEventType() == DatabaseEvent.type.ProcessMLAs) {
            List<MLA> list = new ArrayList<>();
            for (MLA mla : event.getMlaList()) {
                list.add(mla);
                MLAThread thread = new MLAThread(list, i);
                thread.start();
                i++;
        }
    }

    private void addPartiesToDatabase(List<Party> parties) {
        PartyThread thread = new PartyThread(parties);
        thread.start();
    }
}
