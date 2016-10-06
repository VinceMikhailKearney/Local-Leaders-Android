package industries.muskaqueers.thunderechosaber.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by vincekearney on 21/09/2016.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    /* ---- TAG and Helper strings ---- */
    private static final String TAG = "DatabaseManager";
    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_BLOB = "BLOB";
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String formatTextType = String.format(" %s, ", TYPE_TEXT);
    private static final String formatTextTypeEnd = String.format(" %s", TYPE_TEXT);
    private static final String formatBlobType = String.format(" %s, ", TYPE_BLOB);
    private static final String formatBlobTypeEnd = String.format(" %s", TYPE_BLOB);
    private static final String formatIntegerType = String.format(" %s, ", TYPE_INTEGER);
    private static final String formatIntegerTypeEnd = String.format(" %s", TYPE_BLOB);

    /* ---- Database ---- */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ThunderEchoSabre.db";

    /* ---- MLA Table and Columns (in order) ---- */
    public final String MLAS_TABLE = "MLAs";
    public final String MLA_ID = "mla_id";
    public final String MLA_FIRST_NAME = "first_name";
    public final String MLA_LAST_NAME = "last_name";
    public final String MLA_IMAGE_URL = "image_url";
    public final String MLA_IMAGE_BITMAP = "image_bitmap";
    public final String MLA_PARTY_ABBREVIATION = "party_abbreviation";
    public final String MLA_PARTY_NAME = "party_name";
    public final String MLA_TITLE = "title";
    public final String MLA_TWITTER_HANDLE = "twitter_handle";
    public final String MLA_CONSTITUENCY = "constituency";

    /* ---- Party Table and Columns (in order) ---- */
    public final String PARTY_TABLE = "PartyInfo";
    public final String PARTY_ID = "party_id";
    public final String PARTY_NAME = "party_name";
    public final String PARTY_TWITTER_HANDLE = "party_twitter_handle";
    public final String PARTY_IMAGE_URL = "party_image_url";
    public final String PARTY_IMAGE_DATA = "image_data";

    /* ---- Create table SQL string ---- */
    private final String CREATE_MLA_TABLE =
            "CREATE TABLE IF NOT EXISTS " + MLAS_TABLE + "("
                    + MLA_ID + formatTextType
                    + MLA_FIRST_NAME + formatTextType
                    + MLA_LAST_NAME + formatTextType
                    + MLA_IMAGE_URL + formatTextType
                    + MLA_IMAGE_BITMAP + formatBlobType
                    + MLA_PARTY_ABBREVIATION + formatTextType
                    + MLA_PARTY_NAME + formatTextType
                    + MLA_TITLE + formatTextType
                    + MLA_TWITTER_HANDLE + formatTextType
                    + MLA_CONSTITUENCY + formatTextTypeEnd + ")";

    private final String CREATE_PARTY_TABLE =
            "CREATE TABLE IF NOT EXISTS " + PARTY_TABLE + "("
                    + PARTY_ID + formatTextType
                    + PARTY_NAME + formatTextType
                    + PARTY_TWITTER_HANDLE + formatTextType
                    + PARTY_IMAGE_URL + formatTextType
                    + PARTY_IMAGE_DATA + formatBlobTypeEnd + ")";

    /**
     * Above ^^^
     * When creating the SQL database table we need to form a string that matches an exact pattern.
     * I found this just a slightly easier way to read it when doing it in ToDooey :D.
     * Essentially it just makes sure that the table is not already present in the DB (not to override it)
     * then gives the table a name, assigns the columns that we want to use with their respective data type.
     */

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.i(TAG, "Setting up DatabaseManager.");
        // After we init the DatabaseManager it calls onCreate() and sets up the DB for us
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Creating the MLA table --> " + CREATE_MLA_TABLE);
        Log.i(TAG, "Creating the MLA table --> " + CREATE_PARTY_TABLE);
        // Here is the best way to see the string that creates the DB table
        db.execSQL(CREATE_MLA_TABLE);
        db.execSQL(CREATE_PARTY_TABLE);
    }

    /**
     * For onUpgrade()
     * Note: No point in using for Dev - We are better using 'Clear Data' in android settings.
     * Then just running it so the version number does not need to increase.
     * For release we need to migrate everything.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If we ever upgrade, which we won't for now, we do the shiz here.
        Log.w(DatabaseManager.class.getName(), "Upgrading Database from " + oldVersion + " to " + newVersion);
        db.execSQL("ALTER TABLE " + MLAS_TABLE);
        // Then anything else like ADD/DELETE/MODIFY columns in table
    }
}
