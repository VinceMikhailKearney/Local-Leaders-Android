package industries.muskaqueers.thunderechosaber.Managers;

import android.util.Log;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import industries.muskaqueers.thunderechosaber.DB.CounsellorDatabaseHelper;
import industries.muskaqueers.thunderechosaber.ThunderEchoSaberApplication;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager {
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseDataReference;
    private CounsellorDatabaseHelper counsellorDatabaseHelper;

    public FirebaseManager() {
        this.counsellorDatabaseHelper = new CounsellorDatabaseHelper(ThunderEchoSaberApplication.getLocalDatabaseManager());
        this.firebaseDataReference = FirebaseDatabase.getInstance().getReference("test");
        this.firebaseDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                String dataSnapshotString = dataSnapshot.getValue().toString();
                Log.d(TAG, "DataSnapshot value --> " + dataSnapshotString);
                createCounsellor(dataSnapshotString);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value --> " + databaseError);
            }
        });
    }

    public void createCounsellor(String jsonResponse) {
        try {
            JSONObject jsonObject = new JSONObject(jsonResponse);
            JSONObject memberObject = jsonObject.getJSONObject("vince");
            String name = memberObject.optString("name");
            String age = memberObject.optString("age");
            String hero = memberObject.optString("hero");

            this.counsellorDatabaseHelper.addCounsellor(name, age, hero);
//            Log.i(TAG, "Counsellor count == " + this.counsellorDatabaseHelper.getAllCounsellors().size());
//            Log.i(TAG, "The counsellor object created - " + newCounsellor + "       The hero --> " + newCounsellor.getHero());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
