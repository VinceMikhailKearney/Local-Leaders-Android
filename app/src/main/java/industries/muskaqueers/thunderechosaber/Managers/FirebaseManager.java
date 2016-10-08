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
import industries.muskaqueers.thunderechosaber.DB.PartyDatabaseHelper;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.ThunderEchoSabreEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.PasrserUtils;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseMlaReference;
    public DatabaseReference firebasePartyReference;
    private MLADatabaseHelper MLA_DB_Helper;
    private PartyDatabaseHelper partyDatabaseHelper;
    private ProcessImage imageProcessor;

    public FirebaseManager() {
        this.imageProcessor = new ProcessImage();
        this.MLA_DB_Helper = new MLADatabaseHelper();
        this.partyDatabaseHelper = new PartyDatabaseHelper();

        this.firebaseMlaReference = FirebaseDatabase.getInstance().getReference("MLASJSON");
        this.firebaseMlaReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() == null) {
                    Log.w(TAG, "onDataChange MLA: " + "Did not ask for the right data. Check the getReference() method");
                    return;
                }

                Log.d(TAG, "onDataChange MLA: Version = " + PasrserUtils.versionNumber((HashMap) dataSnapshot.getValue(), "version"));

                if (MLA_DB_Helper.getAllMLAs().size() == 108)
                    return; // This is hardcoded right now just to save myself bother. We really ought to sort this out properly

                addMlasToDatabase(PasrserUtils.getMLAsFromMap((HashMap) dataSnapshot.getValue(), "mlas"));
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

                try {
                    addPartiesToDatabase(PasrserUtils.getPartiesFromArray((List<Object>) dataSnapshot.getValue()));
                } catch (NullPointerException e) {
                    Log.w(TAG, "onDataChange: Well something fucked up",e);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * After getting MLAs from HashMap we save each of them to the DB. Updating the twitter handle and image data of each one as well.
     * @param mlas - Array of all MLAs in the map
     */
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
            // Async download the image and store in DB against the MLA
            imageProcessor.getDataFromImage(mla.getImageURL(), mla.getMLA_ID());
        }

        EventBus.getDefault().post(new ThunderEchoSabreEvent(ThunderEchoSabreEvent.eventBusEventType.UPDATE_MLAS));
    }

    private void addPartiesToDatabase(List<Party> parties) {
        for(Party party : parties) {
            Party addParty = this.partyDatabaseHelper.addParty(party.getPartyId(),
                    party.getName(),
                    party.getTwitterHandle(),
                    party.getImageURL());

            Log.i(TAG, "addPartiesToDatabase: Party " + addParty);
//            imageProcessor.getDataFromImage(party.getImageURL(), party.getPartyId());
        }
    }
}
