package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.R;

public class MLA_Info_Activity extends AppCompatActivity {

    private static final String TAG = "MLA_Info_Activity";
    public static final String MLA_EXTRA = "MLA_EXTRA";

    private MLA mla;
    private CircleImageView profilePicture;
    private TextView name, partyAbrv, title, partyName, constituency;
    private ImageButton tweetButton, emailButton;

    // ---------- Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_info);

        String mlaID = (String) getIntent().getSerializableExtra(MLA_EXTRA);
        MLADatabaseHelper dbh = new MLADatabaseHelper();
        mla = dbh.fetchMLA(mlaID);

        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        name = (TextView) findViewById(R.id.name);
        partyAbrv = (TextView)  findViewById(R.id.party_abrv);
        title = (TextView) findViewById(R.id.title);
        partyName = (TextView) findViewById(R.id.party_name);
        constituency = (TextView) findViewById(R.id.constituency);
        tweetButton = (ImageButton) findViewById(R.id.tweet_button);
        emailButton = (ImageButton) findViewById(R.id.email_button);

        profilePicture.setImageBitmap(mla.getImageBitmap());
        name.setText(mla.getFullName());
        partyAbrv.setText(mla.getPartyAbbreviation().toUpperCase());
        title.setText(mla.getTitle());
        partyName.setText(mla.getPartyName());
        constituency.setText(mla.getConstituency());

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TwitterManager.tweetUser(v.getContext(), mla.getTwitterHandle());
            }
        });
    }

}
