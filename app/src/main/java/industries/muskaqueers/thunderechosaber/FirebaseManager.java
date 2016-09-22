package industries.muskaqueers.thunderechosaber;

import android.util.Log;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by vincekearney on 20/09/2016.
 */
public class FirebaseManager
{
    private static final String TAG = "FirebaseManager";
    public DatabaseReference firebaseDataReference;

    public FirebaseManager()
    {
        this.firebaseDataReference = FirebaseDatabase.getInstance().getReference("test");
        this.firebaseDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot value --> " + dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(TAG, "Failed to read value --> " + databaseError);
            }
        });
    }
}
