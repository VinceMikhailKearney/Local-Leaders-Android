package industries.muskaqueers.thunderechosaber.UI;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.LLApplication;
import industries.muskaqueers.thunderechosaber.Managers.TwitterManager;
import industries.muskaqueers.thunderechosaber.Database.GreenDatabaseManager;
import industries.muskaqueers.thunderechosaber.Database.MLADb;
import industries.muskaqueers.thunderechosaber.Database.MLADbDao;
import industries.muskaqueers.thunderechosaber.Database.PartyDB;
import industries.muskaqueers.thunderechosaber.Database.PartyDBDao;
import industries.muskaqueers.thunderechosaber.R;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "ProfileActivity";
    public static final String MLA_EXTRA = "MLA_EXTRA";
    public static final String MLA_IMG_PREFIX = "mla_with_id__";
    private static final int UP_ARROW = android.support.design.R.drawable.abc_ic_ab_back_material;

    private MLADb mla;
    private PartyDB mlaParty;
    private Toolbar toolbar;
    private LinearLayout contactBar;
    private CircleImageView profilePicture;
    private ImageView coverPhoto;
    private TextView name, partyAbrv, title, partyName, constituency;
    private ImageButton tweetButton, emailButton;
    private FrameLayout tweetButtonFrame, emailButtonFrame;

    // ---------- Lifecycle Methods ----------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        getIntentData();

        bindUI();

        String mlaId = String.format(MLA_IMG_PREFIX + mla.getMLA_ID().toString());
        int drawableID = LLApplication.getAppContext().getResources().getIdentifier(mlaId, "drawable", LLApplication.getAppContext().getPackageName());
        profilePicture.setImageResource(drawableID);

        // If independent - No party
        this.mlaParty = GreenDatabaseManager.getPartyTable().queryBuilder().where(PartyDBDao.Properties.PartyId.eq(mla.getPartyAbbreviation().toUpperCase())).unique();
        coverPhoto.setBackgroundResource(R.color.blue1);
        final Drawable upArrow = ContextCompat.getDrawable(this, UP_ARROW);
        upArrow.setColorFilter(getResources().getColor(R.color.tw__solid_white), PorterDuff.Mode.SRC_ATOP);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        name.setText(mla.getFirstName() + " " + mla.getLastName());
        partyAbrv.setText(mla.getPartyAbbreviation().toUpperCase());
        title.setText(mla.getTitle());
        partyName.setText(mla.getPartyName());
        constituency.setText(mla.getConstituency());

        int twitterAvailable = !mla.getTwitterHandle().isEmpty() ? View.VISIBLE : View.GONE;
        tweetButtonFrame.setVisibility(twitterAvailable);
        int emailAvailable = !mla.getEmailAddress().isEmpty() ? View.VISIBLE : View.GONE;
        emailButtonFrame.setVisibility(emailAvailable);

        tweetButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);
    }

    // ---------- OnClick Methods ----------
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tweet_button:
                tweetUser();
                break;
            case R.id.email_button:
                emailUser();
                break;
        }
    }

    private void tweetUser() {
        String twitterHandle = mla.getTwitterHandle();
        if (mla.getTwitterHandle().isEmpty())
            twitterHandle = this.mlaParty.getTwitterHandle();
        TwitterManager.tweetUser(this, twitterHandle);
    }

    private void emailUser() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{mla.getEmailAddress()});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Contacting via Local Leaders");
        intent.putExtra(Intent.EXTRA_TEXT, "Dear " + mla.getFirstName() + " " + mla.getLastName() + ", \n\n");
        startActivity(Intent.createChooser(intent, "Please choose an email client: "));
    }

    // ---------- Custom Methods ----------
    private void getIntentData() {
        String mlaID = (String) getIntent().getSerializableExtra(MLA_EXTRA);
        mla = GreenDatabaseManager.getMlaTable().queryBuilder().where(MLADbDao.Properties.MLA_ID.eq(mlaID)).unique();
        Log.d(TAG, "MLA INFO: " + mla.toString());
    }

    private void bindUI() {
        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        coverPhoto = (ImageView) findViewById(R.id.cover_photo);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        contactBar = (LinearLayout) findViewById(R.id.contact_bar);
        name = (TextView) findViewById(R.id.name);
        partyAbrv = (TextView) findViewById(R.id.party_abrv);
        title = (TextView) findViewById(R.id.title);
        partyName = (TextView) findViewById(R.id.party_name);
        constituency = (TextView) findViewById(R.id.constituency);
        tweetButton = (ImageButton) findViewById(R.id.tweet_button);
        emailButton = (ImageButton) findViewById(R.id.email_button);
        tweetButtonFrame = (FrameLayout) findViewById(R.id.tweet_frame);
        emailButtonFrame = (FrameLayout) findViewById(R.id.email_frame);

        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch(NullPointerException e){
            Log.d(TAG, "There was a problem setting the Home as Up button");
        }
    }


}
