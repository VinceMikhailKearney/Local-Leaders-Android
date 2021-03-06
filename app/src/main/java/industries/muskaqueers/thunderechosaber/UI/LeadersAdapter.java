package industries.muskaqueers.thunderechosaber.UI;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.LLApplication;
import industries.muskaqueers.thunderechosaber.Database.MLADb;
import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by vincekearney on 24/09/2016.
 */

public class LeadersAdapter extends RecyclerView.Adapter<LeadersAdapter.MLAViewHolder> {

    private static final String TAG = "LeadersAdapter";
    private List<MLADb> mlaList;

    public LeadersAdapter(List<MLADb> list) {
        setMlaList(list);
    }

    public void setMlaList(List<MLADb> list) {
        this.mlaList = list;
        this.notifyDataSetChanged();
    }

    @Override
    public MLAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_mla, parent, false);
        return new MLAViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MLAViewHolder holder, int position) {
        holder.setMLA(getItem(position));
    }

    @Override
    public int getItemCount() {
        return this.mlaList.size();
    }

    public MLADb getItem(int position) {
        return this.mlaList.get(position);
    }

    // ---------- Adapter ViewHolder
    public class MLAViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MLADb viewMLA;
        private TextView nameTextView, partyTextView, positionTextView;
        private CircleImageView profilePicture;
        private String name, party, position;

        public MLAViewHolder(View itemView) {
            super(itemView);
            this.setIsRecyclable(false);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            partyTextView = (TextView) itemView.findViewById(R.id.party_name);
            positionTextView = (TextView) itemView.findViewById(R.id.position);
            profilePicture = (CircleImageView) itemView.findViewById(R.id.profile_picture);
            itemView.setOnClickListener(this);
        }

        public void setMLA(MLADb mla) {
            this.viewMLA = mla;
            this.name = mla.getFirstName() + " " + mla.getLastName();
            this.party = mla.getPartyName();
            this.position = mla.getTitle();
            String mlaId = String.format("mla_with_id__" + this.viewMLA.getMLA_ID().toString());
            int drawableID = LLApplication.getAppContext().getResources().getIdentifier(mlaId, "drawable", LLApplication.getAppContext().getPackageName());
            this.profilePicture.setImageResource(drawableID);

            setTextViews();
        }

        private void setTextViews() {
            this.nameTextView.setText(name);
            this.partyTextView.setText(party);
            this.positionTextView.setText(position);
        }

        @Override
        public void onClick(View view) {
            Intent showDetail = new Intent(view.getContext(), ProfileActivity.class);
            showDetail.putExtra(ProfileActivity.MLA_EXTRA, viewMLA.getMLA_ID());
            view.getContext().startActivity(showDetail);
        }
    }
}
