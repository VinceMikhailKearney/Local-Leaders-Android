package industries.muskaqueers.thunderechosaber;

import android.support.test.runner.AndroidJUnit4;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;

/**
 * Created by vincekearney on 24/09/2016.
 */

@RunWith(AndroidJUnit4.class)
public class MLADatabaseTest {
    private DatabaseManager testManager;
    private MLADatabaseHelper testHelper;

    @Before
    public void setUp() throws Exception {
        testManager = new DatabaseManager(getTargetContext());
        testHelper = new MLADatabaseHelper(testManager);
    }

    @Test
    public void test_mla_database() throws Exception {
        // Here I am simply adding one to make sure that when we delete all it really has worked.
        Assert.assertNotNull(testHelper.addMLA("1", "Test", "MLA", "testimageurl", "VMJK", "Mikhail", "CTO", "Belfast"));
        // For testing purposes we will start by wiping the DB of MLAs
        testHelper.deleteAllMLAs();
        // Making sure that the deleteAll works
        assertThat(testHelper.getAllMLAs().size(), is(0));

        // Test adding counsellors to DB
        MLA newC = testHelper.addMLA("2", "Vince", "Kearney", "vinceimageurl", "VK", "Kearney", "CTO", "Lurgan");
        Assert.assertNotNull(newC);
        MLA newC1 = testHelper.addMLA("3", "Andrew", "Cunningham", "andrewimageurl", "AC", "Cunningham", "CEO", "Hillsborough");
        Assert.assertNotNull(newC1);
        MLA newC2 = testHelper.addMLA("4", "Marc", "Nevin", "marcimageurl", "MN", "Nevin", "COO", "Lisburn");
        Assert.assertNotNull(newC2);

        // Make sure the DB contains the 3 that were just created
        assertThat(testHelper.getAllMLAs().size(), is(3));

        // Search for a MLA with ID = newC1.getMLA_ID()
        MLA searchForNewC1 = testHelper.fetchMLA(newC1.getMLA_ID());
        Assert.assertNotNull(searchForNewC1);

        // Now let's make sure that we can add a TwitterHandle
        String testHandle = "@VinceBoiiiii";
        testHelper.updateTwitterHandle(searchForNewC1, testHandle);
        String mlaHandler = testHelper.fetchMLA(searchForNewC1.getMLA_ID()).getTwitterHandle();
        Assert.assertEquals(mlaHandler, testHandle);

        // Make sure we can delete a specific MLA
        testHelper.deleteMLA(newC.getMLA_ID());
        Assert.assertNull(testHelper.fetchMLA(newC.getMLA_ID()));
        // Now to finish make sure the DB contains only 2
        assertThat(testHelper.getAllMLAs().size(), is(2));
    }
}
