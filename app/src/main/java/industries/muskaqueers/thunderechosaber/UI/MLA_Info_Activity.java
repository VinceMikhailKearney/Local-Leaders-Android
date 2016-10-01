package industries.muskaqueers.thunderechosaber.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.R;

public class MLA_Info_Activity extends AppCompatActivity {

    private static final String TAG = "MLA_Info_Activity";
    public static final String MLA_EXTRA = "MLA_EXTRA";

    private MLA mla;
    private CircleImageView profilePicture;
    private TextView name, partyAbrv, title, partyName, constituency;

    // ---------- Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mla_info);

        mla = (MLA) getIntent().getSerializableExtra(MLA_EXTRA);
        profilePicture = (CircleImageView) findViewById(R.id.profile_picture);
        name = (TextView) findViewById(R.id.name);
        partyAbrv = (TextView)  findViewById(R.id.party_abrv);
        title = (TextView) findViewById(R.id.title);
        partyName = (TextView) findViewById(R.id.party_name);
        constituency = (TextView) findViewById(R.id.contituency);
    }

    @Override
    protected void onStart() {
        super.onStart();
        profilePicture.setImageBitmap(mla.getImageBitmap());
        name.setText(mla.getFullName());
        partyAbrv.setText(mla.getPartyAbbreviation());
        title.setText(mla.getTitle());
        partyName.setText(mla.getPartyName());
        constituency.setText(mla.getConstituency());

    }
}
