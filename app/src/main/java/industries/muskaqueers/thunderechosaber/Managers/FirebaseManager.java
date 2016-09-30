package industries.muskaqueers.thunderechosaber.Managers;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.ThunderEchoSabreEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.PasrserUtils;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseDataReference;
    private MLADatabaseHelper MLA_DB_Helper;

    public FirebaseManager() {
        this.MLA_DB_Helper = new MLADatabaseHelper();
        this.firebaseDataReference = FirebaseDatabase.getInstance().getReference("MLASJSON");
        this.firebaseDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.w(TAG, "onDataChange: " + "Did not ask for the right data. Check the getReference() method");
                    return;
                }
                Log.d(TAG, "onDataChange: Version = " + PasrserUtils.versionNumber((HashMap) dataSnapshot.getValue(), "version"));

                Log.d(TAG, "onDataChange: MLAs size = " + MLA_DB_Helper.getAllMLAs().size());
                if (MLA_DB_Helper.getAllMLAs().size() == 108)
                    return; // This is hardcoded right now just to save myself bother. We really ought to sort this out properly

                addMlasToDatabase(PasrserUtils.getMLAsFromMap((HashMap) dataSnapshot.getValue(), "mlas"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value --> " + databaseError);
            }
        });
    }

    private void addMlasToDatabase(List<MLA> mlas) {
        for (MLA mla : mlas) {
            MLA addMLA = this.MLA_DB_Helper.addMLA(mla.getMLA_ID(),
                    mla.getFirstName(),
                    mla.getLastName(),
                    mla.getImageURL(),
                    mla.getPartyAbbreviation(),
                    mla.getPartyName(),
                    mla.getTitle(),
                    mla.getConstituency());

            // Now that the MLA is in the DB, let's update the TwitterHandle
            this.MLA_DB_Helper.updateTwitterHandle(addMLA, PasrserUtils.findHandleFor(mla.getFirstName(), mla.getLastName()));
        }

        EventBus.getDefault().post(new ThunderEchoSabreEvent(ThunderEchoSabreEvent.eventBusEventType.UPDATE_MLAS));
    }
}
