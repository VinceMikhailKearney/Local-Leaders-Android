package industries.muskaqueers.thunderechosaber;

import android.support.test.runner.AndroidJUnit4;

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
    private static final String TAG = "CounsellorDatabaseTest";
    private DatabaseManager testManager;
    private CounsellorDatabaseHelper testHelper;

    @Before
    public void setUp() throws Exception {
        testManager = new DatabaseManager(getTargetContext());
        testHelper = new CounsellorDatabaseHelper(testManager);
    }

    @Test
    public void test_counsellor_database() throws Exception {
        // Here I am simply adding one to make sure that when we delete all it really has worked.
        Assert.assertNotNull(testHelper.addCounsellor("Test", "MLA", "testimageurl", "VMJK", "Mikhail", "CTO", "Belfast"));
        // For testing purposes we will start by wiping the DB of counsellors
        testHelper.deleteAllCounsellors();
        // Making sure that the deleteAll works
        assertThat(testHelper.getAllCounsellors().size(), is(0));

        // Test adding counsellors to DB
        Counsellor newC = testHelper.addCounsellor("Vince", "Kearney", "vinceimageurl", "VK", "Kearney", "CTO", "Lurgan");
        Assert.assertNotNull(newC);
        Counsellor newC1 = testHelper.addCounsellor("Andrew", "Cunningham", "andrewimageurl", "AC", "Cunningham", "CEO", "Hillsborough");
        Assert.assertNotNull(newC1);
        Counsellor newC2 = testHelper.addCounsellor("Marc", "Nevin", "marcimageurl", "MN", "Nevin", "COO", "Lisburn");
        Assert.assertNotNull(newC2);

        // Make sure the DB contains the 3 that were just created
        assertThat(testHelper.getAllCounsellors().size(), is(3));

        // Search for a counsellor with ID = newC1.getCounsellorID()
        Counsellor searchForNewC1 = testHelper.counsellor(newC1.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.FETCH);
        Assert.assertNotNull(searchForNewC1);

        // Make sure we can delete a specific counsellor
        testHelper.counsellor(newC.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.DELETE);
        Assert.assertNull(testHelper.counsellor(newC.getCounsellorID(), CounsellorDatabaseHelper.getOrDelete.FETCH));
        // Now to finsih make sure the DB contains only 2
        assertThat(testHelper.getAllCounsellors().size(), is(2));
    }
}
