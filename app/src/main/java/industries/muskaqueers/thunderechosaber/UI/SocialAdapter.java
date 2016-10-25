package industries.muskaqueers.thunderechosaber.UI;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.R;

/**
 * Created by vincekearney on 24/09/2016.
 */

public class SocialAdapter extends RecyclerView.Adapter<SocialAdapter.MLAViewHolder> {

    private static final String TAG = "LeadersAdapter";
    private List<String> tweetList;
    private MLA mla;

    public SocialAdapter(MLA mla, List<String> tweetList) {
        this.mla = mla;
        setTweetList(tweetList);
    }

    public SocialAdapter(List<String> tweetList){
        Log.d(TAG, "We have got a tweetList: " + tweetList);
        this.mla = null;
        setTweetList(tweetList);
    }

    public SocialAdapter(){}

    public void setTweetList(List<String> tweetList) {
        this.tweetList = tweetList;
        this.notifyDataSetChanged();
    }

    @Override
    public MLAViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_tweet, parent, false);
        return new MLAViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MLAViewHolder holder, int position) {
        holder.setTweet(mla, getItem(position));
    }

    @Override
    public int getItemCount() {
        return this.tweetList.size();
    }

    public String getItem(int position) {
        return this.tweetList.get(position);
    }

    // ---------- Adapter ViewHolder
    public class MLAViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MLA viewMLA;
        private TextView nameTextView, tweetBodyView;
        private CircleImageView profilePicture;
        private String name, tweetBody;
        private Bitmap bitmap;

        public MLAViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            tweetBodyView = (TextView) itemView.findViewById(R.id.tweet_body);
            profilePicture = (CircleImageView) itemView.findViewById(R.id.profile_picture);
            itemView.setOnClickListener(this);
        }

        public void setTweet(MLA mla, String tweetBody) {
            this.viewMLA = mla;
            this.tweetBody = tweetBody;
            if(mla!=null) {
                this.name = mla.getFullName();
                this.bitmap = mla.getImageBitmap();


                if (bitmap != null) {
                    Log.d(TAG, "setMLA: Bitmap is NOT null. MLA Name = " + mla.getFullName());
                    this.profilePicture.setImageBitmap(this.bitmap);
                }
            } else {
                Log.d(TAG, "AAC --> APPARENTLY, MLA is null");
            }

            setTextViews();
        }

        private void setTextViews() {
            if(mla!=null)this.nameTextView.setText(name);
            this.tweetBodyView.setText(tweetBody);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Do Something", Toast.LENGTH_SHORT).show();
        }
    }
}
