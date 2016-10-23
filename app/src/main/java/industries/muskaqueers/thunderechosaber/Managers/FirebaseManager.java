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
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseMlaReference;
    public DatabaseReference firebasePartyReference;
    private ProcessImage imageProcessor;
    private int imageDownloadCount;
    private int imageThreshold;

    public FirebaseManager() {
        // Register on the EventBus
        EventBus.getDefault().register(this);
        imageDownloadCount = 0;

        this.imageProcessor = new ProcessImage();

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

                imageThreshold = dataSnapShotMap.size();
                if (DatabaseManager.mlaHelper().size() == dataSnapShotMap.size())
                    return; // This is hardcoded right now just to save myself bother. We really ought to sort this out properly

                ParserUtils.getMLAsFromMap((HashMap) dataSnapshot.getValue(), "mlas");
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
            Log.d(TAG, "onEvent: Received a process mlas event. The array size = " + event.getMlaList().size());

            for (MLA mla : event.getMlaList()) {
                MLA addMLA = DatabaseManager.mlaHelper().addMLA(mla.getMLA_ID(),
                        mla.getFirstName(),
                        mla.getLastName(),
                        mla.getImageURL(),
                        mla.getPartyAbbreviation(),
                        mla.getPartyName(),
                        mla.getTitle(),
                        mla.getConstituency());

                // Now that the MLA is in the DB, let's update the TwitterHandle
                DatabaseManager.mlaHelper().updateTwitterHandle(addMLA, ParserUtils.findHandleFor(mla.getFirstName(), mla.getLastName()));
                // Async download the image and store in DB against the MLA
                imageProcessor.getDataFromImage(mla.getImageURL(), mla.getMLA_ID(), ProcessImage.type.MLA);
            }

            EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
            
        } else if(event.getEventType() == DatabaseEvent.type.DownloadedImage) {
            imageDownloadCount++;
            if((imageDownloadCount != 0 && imageDownloadCount % 10 == 0) || imageDownloadCount == imageThreshold) {
                EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
            }
        }
    }

    private void addPartiesToDatabase(List<Party> parties) {
        for(Party party : parties) {
            Party addParty = DatabaseManager.partyHelper().addParty(party.getPartyId(),
                    party.getName(),
                    party.getTwitterHandle(),
                    party.getImageURL());

            Log.i(TAG, "addPartiesToDatabase: Party " + addParty);
            imageProcessor.getDataFromImage(party.getImageURL(), party.getPartyId(), ProcessImage.type.Party);
        }
    }
}
