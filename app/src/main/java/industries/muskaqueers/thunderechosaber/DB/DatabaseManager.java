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
    private static final String TYPE_INTEGER = "INTEGER";
    private static final String formatTextType = String.format(" %s, ", TYPE_TEXT);
    private static final String formatTextTypeEnd = String.format(" %s", TYPE_TEXT);
    private static final String formatIntegerType = String.format(" %s, ", TYPE_INTEGER);

    /* ---- Database ---- */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ThunderEchoSabre.db";

    /* ---- Table and Columns (in order) ---- */
    public final String COUNSELLORS_TABLE = "Counsellors";
    public final String COLUMN_NAME_COUNSELLOR_ID = "counsellor_id";
    public final String COLUMN_NAME_COUNSELLOR_FIRST_NAME = "first_name";
    public final String COLUMN_NAME_COUNSELLOR_LAST_NAME = "last_name";
    public final String COLUMN_NAME_COUNSELLOR_IMAGE_URL = "image_url";
    public final String COLUMN_NAME_COUNSELLOR_PARTY_ABBREVIATION = "party_abbreviation";
    public final String COLUMN_NAME_COUNSELLOR_PARTY_NAME = "party_name";
    public final String COLUMN_NAME_COUNSELLOR_TITLE = "title";
    public final String COLUMN_NAME_COUNSELLOR_TWITTER_HANDLE = "twitter_handle";
    public final String COLUMN_NAME_COUNSELLOR_CONSTITUENCY = "constituency";

    /* ---- Create table SQL string ---- */
    private final String CREATE_DATABASE =
            "CREATE TABLE IF NOT EXISTS " + COUNSELLORS_TABLE + "("
                    + COLUMN_NAME_COUNSELLOR_ID + formatTextType
                    + COLUMN_NAME_COUNSELLOR_FIRST_NAME + formatTextType
                    + COLUMN_NAME_COUNSELLOR_LAST_NAME + formatTextType
                    + COLUMN_NAME_COUNSELLOR_IMAGE_URL + formatTextType
                    + COLUMN_NAME_COUNSELLOR_PARTY_ABBREVIATION + formatTextType
                    + COLUMN_NAME_COUNSELLOR_PARTY_NAME + formatTextType
                    + COLUMN_NAME_COUNSELLOR_TITLE + formatTextType
                    + COLUMN_NAME_COUNSELLOR_TWITTER_HANDLE + formatTextType
                    + COLUMN_NAME_COUNSELLOR_CONSTITUENCY + formatTextTypeEnd + ")";

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
        Log.i(TAG, "Creating the database. Output --> " + CREATE_DATABASE);
        // Here is the best way to see the string that creates the DB table
        db.execSQL(CREATE_DATABASE);
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
        db.execSQL("ALTER TABLE " + COUNSELLORS_TABLE);
        // Then anything else like ADD/DELETE/MODIFY columns in table
    }
}
