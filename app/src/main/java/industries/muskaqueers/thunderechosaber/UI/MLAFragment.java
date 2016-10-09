package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.R;
import industries.muskaqueers.thunderechosaber.DatabaseEvent;

/**
 * Created by Andrew on 9/23/16.
 */

public class MLAFragment extends Fragment {

    private static final String TAG = "MLAFragment";
    private RecyclerView mlaRecyclerView;
    private List<MLA> mlaList = new ArrayList<>();
    private MLA_Adapter mlaAdapter;
    private MLADatabaseHelper mlaDatabaseHelper;

    // ---------- Lifecycle Methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mla, container, false);
        mlaRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());

        // Get an instance of the MLADBHelper
        this.mlaDatabaseHelper = new MLADatabaseHelper();
        // Set the list of the fragment to all MLAs in the DB
        for(Object mla : this.mlaDatabaseHelper.getAllObjects()) {
            this.mlaList.add((MLA) mla);
        }
        this.mlaAdapter = new MLA_Adapter(this.mlaList);
        mlaRecyclerView.setLayoutManager(layoutManager);
        mlaRecyclerView.setAdapter(this.mlaAdapter);

        return view;
    }

    /**
     * EventBus listener
     * @param event - Event object that states what the event is for along with containing information such as a specific MLA
     */
    public void onEvent(DatabaseEvent event) {
        if(event.getEventType() == DatabaseEvent.type.UpdateMLAs) {
            Log.d(TAG, "onEvent: Just got told to update mlas");

            this.mlaList.clear();
            for(Object mla : this.mlaDatabaseHelper.getAllObjects()) {
                this.mlaList.add((MLA) mla);
            }

            this.mlaAdapter.setMlaList(this.mlaList);
        } else if(event.getEventType() == DatabaseEvent.type.OnClickMla) {
            Log.d(TAG, "onEvent: Clicked MLA");
            MLA thisMLA = event.getMLA();
            if(thisMLA.getTwitterHandle().length() == 0) {
                Toast.makeText(getContext(), "MLA does not have a twitter handle", Toast.LENGTH_SHORT).show();
            } else {
                TwitterManager.tweetUser(getContext(), thisMLA.getTwitterHandle());
            }
        }
    }
}
