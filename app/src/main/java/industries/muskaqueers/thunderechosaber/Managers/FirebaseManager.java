package industries.muskaqueers.thunderechosaber.Managers;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.NewDB.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.NewDB.MLADb;
import industries.muskaqueers.thunderechosaber.NewDB.PartyDB;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.Utils.MLAThread;
import industries.muskaqueers.thunderechosaber.Utils.PartyThread;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {

    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseMlaReference;
    public DatabaseReference firebasePartyReference;

    public FirebaseManager() {
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

                List<Object> mlaDataSnapShot = (List) dataSnapShotMap.get("mlas");
                if (GreenDatabaseManager.getMLADBSize() == mlaDataSnapShot.size()) {
                    Log.d(TAG, "AAC --> There are no difference in the size of MLAs, do not change edit table");
                    return;
                }
                List<MLADb> MLAsFromMap = ParserUtils.getMLAsFromMapNew(dataSnapShotMap, "mlas");
                Log.d(TAG, "AAC --> We got something from parsing the new data and the size of the list is: " + MLAsFromMap.size());
                addMLAsToDatabase(MLAsFromMap);
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
                if (GreenDatabaseManager.getPartyDBSize() == dataSnapShotArray.size()) {
                    Log.d(TAG, "AAC --> There are no difference in the size of Parties, do not change edit table");
                    return;
                }
                List<PartyDB> partiesFromMap = ParserUtils.getPartiesFromArray(dataSnapShotArray);
                Log.d(TAG, "AAC --> We got something from parsing the new data and the size of the list is: " + partiesFromMap.size());
                addPartiesToDatabase(partiesFromMap);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addMLAsToDatabase(List<MLADb> mlaDbs) {
        MLAThread thread = new MLAThread(mlaDbs);
        thread.execute();

    }

    private void addPartiesToDatabase(List<PartyDB> parties) {
        PartyThread thread = new PartyThread(parties);
        thread.execute();
    }
}
