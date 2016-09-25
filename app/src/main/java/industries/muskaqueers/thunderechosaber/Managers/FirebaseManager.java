package industries.muskaqueers.thunderechosaber.Managers;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.JSONUtils;
import industries.muskaqueers.thunderechosaber.ThunderEchoSaberApplication;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager implements JSONUtils.JSONListener {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseDataReference;
    private MLADatabaseHelper MLA_DB_Helper;
    private JSONUtils utils;

    public FirebaseManager() {
        utils = new JSONUtils(this);
        this.MLA_DB_Helper = new MLADatabaseHelper(ThunderEchoSaberApplication.getLocalDatabaseManager());
        this.firebaseDataReference = FirebaseDatabase.getInstance().getReference("MLASJSON");
        this.firebaseDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() == null) {
                    Log.w(TAG, "onDataChange: " + "Did not ask for the right data. Check the getReference() method");
                    return;
                }
                Log.d(TAG, "onDataChange: Version = " + utils.versionNumber((HashMap) dataSnapshot.getValue(), "version"));
                utils.getMLAsFromMap((HashMap) dataSnapshot.getValue(), "mlas");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value --> " + databaseError);
            }
        });
    }

    @Override
    public void CreateMLA(MLA mla) {
        MLA testMLA = MLA_DB_Helper.
                addMLA(
                mla.getMLA_ID(),
                mla.getFirstName(),
                mla.getLastName(),
                mla.getImageURL(),
                mla.getPartyAbbreviation(),
                mla.getPartyName(),
                mla.getTitle(),
                mla.getConstituency());

        Log.d(TAG, "CreateMLA: Name = " + testMLA.getFullName());
    }
}
