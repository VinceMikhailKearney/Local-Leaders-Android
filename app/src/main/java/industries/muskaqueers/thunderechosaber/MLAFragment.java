package industries.muskaqueers.thunderechosaber;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Andrew on 9/23/16.
 */

public class MLAFragment extends Fragment {

    private RecyclerView mlaRecyclerView;

    // ---------- Lifecycle Methods
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mla, container, false);

        mlaRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        // Create an adapter
        mlaRecyclerView.setLayoutManager(layoutManager);
        // mlaRecyclerView.setAdapter(Custom Adapter);

        return view;
    }

    // ---------- Adapter ViewHolder
    public static class CounsellorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Counsellor counsellor;
        private TextView nameTextView, partyTextView, positionTextView, ageTextView, heroTextView;
        private CircleImageView profilePicture;
        private String name, age, hero;

        public CounsellorViewHolder(View itemView) {
            super(itemView);
//            nameTextView = (TextView) itemView.findViewById(R.id.name);
//            ageTextView = (TextView) itemView.findViewById(R.id.age);
//            heroTextView = (TextView) itemView.findViewById(R.id.hero);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            partyTextView = (TextView) itemView.findViewById(R.id.party_name);
            positionTextView = (TextView) itemView.findViewById(R.id.position);
            profilePicture = (CircleImageView) itemView.findViewById(R.id.profile_picture);
            itemView.setOnClickListener(this);
        }

        public void setName(String name) {
            this.name = name;
            this.nameTextView.setText(name);
        }

        public void setAge(String age) {
            this.age = age;
            this.ageTextView.setText(age);
        }

        public void setHero(String hero) {
            this.hero = hero;
            this.heroTextView.setText(hero);
        }

        public void setCounsellor(Counsellor counsellor) {
            this.counsellor = counsellor;
        }

        @Override
        public void onClick(View view) {
        }
    }

}
