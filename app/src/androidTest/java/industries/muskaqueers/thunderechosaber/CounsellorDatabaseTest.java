package industries.muskaqueers.thunderechosaber;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import static android.support.test.InstrumentationRegistry.getTargetContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import industries.muskaqueers.thunderechosaber.DB.CounsellorDatabaseHelper;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;

/**
 * Created by vincekearney on 24/09/2016.
 */

@RunWith(AndroidJUnit4.class)
public class CounsellorDatabaseTest {
    private DatabaseManager testManager;
    private CounsellorDatabaseHelper testHelper;

    @Before
    public void setUp() throws Exception {
        testManager = new DatabaseManager(getTargetContext());
        testHelper = new CounsellorDatabaseHelper(testManager);
    }

    @Test
    public void test_counsellor_database() throws Exception {
        // For testing purposes we will start by wiping the DB of counsellors
        testHelper.deleteAllCounsellors();
        assertThat(testHelper.getAllCounsellors().size(), is(0));

        Log.i("Test", "Let's try adding counsellors to DB");
        Counsellor newC = testHelper.addCounsellor("Vince", "23", "Goku");
        Assert.assertNotNull(newC);
        Counsellor newC1 = testHelper.addCounsellor("Andrew", "21", "Vegeta");
        Assert.assertNotNull(newC1);
        Counsellor newC2 = testHelper.addCounsellor("Marc", "22", "Majin-Buu");
        Assert.assertNotNull(newC2);

        Counsellor searchForNewC1 = testHelper.counsellor(newC1.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.FETCH);
        Assert.assertNotNull(searchForNewC1);

        // At this point we have created and saved 3 counsellors to the DB, let's make sure that's what we have
        assertThat(testHelper.getAllCounsellors().size(), is(3));

        // Let's test that deleting a counsellor works
        testHelper.counsellor(newC.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.DELETE);
        assertThat(testHelper.getAllCounsellors().size(), is(2)); // Make sure when we fetch all it's only 2
        Assert.assertNull(testHelper.counsellor(newC.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.FETCH)); // Making sure the specifc counsellor removed is not in the DB
    }
}
