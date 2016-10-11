package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.DB.DatabaseHelper;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.R;

public class MLAInfoActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MLAInfoActivity";
    public static final String MLA_EXTRA = "MLA_EXTRA";

    private MLA mla;
    private Party mlaParty;
    private Toolbar toolbar;
    private CircleImageView profilePicture;
    private ImageView coverPhoto;
    private TextView name, partyAbrv, title, partyName, constituency;
    private ImageButton tweetButton, emailButton;

    // ---------- Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_info);

        String mlaID = (String) getIntent().getSerializableExtra(MLA_EXTRA);
        mla = DatabaseHelper.getMlaHelper().fetchMLA(mlaID);

        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        coverPhoto = (ImageView) findViewById(R.id.cover_photo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        name = (TextView) findViewById(R.id.name);
        partyAbrv = (TextView)  findViewById(R.id.party_abrv);
        title = (TextView) findViewById(R.id.title);
        partyName = (TextView) findViewById(R.id.party_name);
        constituency = (TextView) findViewById(R.id.constituency);
        tweetButton = (ImageButton) findViewById(R.id.tweet_button);
        emailButton = (ImageButton) findViewById(R.id.email_button);

        profilePicture.setImageBitmap(mla.getImageBitmap());
        this.mlaParty = DatabaseHelper.getPartyHelper().fetchParty(mla.getPartyAbbreviation().toUpperCase());
        if(this.mlaParty != null) {
            coverPhoto.setImageBitmap(this.mlaParty.getImageBitmap());
            Palette p = Palette.from(this.mlaParty.getImageBitmap()).generate();
            findViewById(R.id.contact_bar).setBackgroundColor(p.getVibrantSwatch().getRgb());
        } else{
            coverPhoto.setBackgroundResource(R.color.blue1);
        }
        name.setText(mla.getFullName());
        partyAbrv.setText(mla.getPartyAbbreviation().toUpperCase());
        title.setText(mla.getTitle());
        partyName.setText(mla.getPartyName());
        constituency.setText(mla.getConstituency());

        tweetButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tweet_button: {
                String twitterHandle = mla.getTwitterHandle();
                if (mla.getTwitterHandle().isEmpty()) // If the MLA does not have a twitter handle, set twitter handle to that of the party.
                    twitterHandle = this.mlaParty.getTwitterHandle();

                TwitterManager.tweetUser(v.getContext(), twitterHandle);
                break;
            }
            case R.id.email_button:
                Toast.makeText(MLAInfoActivity.this, "Coming soon...", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
