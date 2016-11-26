package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.Events.NewDatabaseEvent;
import industries.muskaqueers.thunderechosaber.Events.UIEvent;
import industries.muskaqueers.thunderechosaber.Database.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.Database.MLADb;
import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by Andrew on 9/23/16.
 */

public class LeadersFragment extends Fragment {

    private static final String TAG = "LeadersFragment";
    private RecyclerView mlaRecyclerView;
    private List<MLADb> mlaList = new ArrayList<>();
    private LeadersAdapter leadersAdapter;

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

        // Set the list of the fragment to all MLAs in the DB
        for (MLADb mla : GreenDatabaseManager.getMlaTable().loadAll())
            this.mlaList.add(mla);

        if(this.mlaList.size()!=0)
            EventBus.getDefault().post(new UIEvent.RemoveSpinner());

        this.leadersAdapter = new LeadersAdapter(this.mlaList);
        mlaRecyclerView.setLayoutManager(layoutManager);
        mlaRecyclerView.setAdapter(this.leadersAdapter);
        return view;
    }

    // ---------- Event Handlers ----------
    public void onEventMainThread(NewDatabaseEvent.CompletedMLAUpdates completedMLAUpdates){
        EventBus.getDefault().post(new UIEvent.RemoveSpinner());
        mlaList = GreenDatabaseManager.getMlaTable().loadAll();
        leadersAdapter.setMlaList(mlaList);
        leadersAdapter.notifyDataSetChanged();
    }
}
