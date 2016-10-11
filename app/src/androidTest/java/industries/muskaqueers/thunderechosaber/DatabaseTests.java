package industries.muskaqueers.thunderechosaber;

import android.support.test.runner.AndroidJUnit4;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import industries.muskaqueers.thunderechosaber.DB.DatabaseHelper;
import industries.muskaqueers.thunderechosaber.DB.MLADatabaseHelper;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;

/**
 * Created by vincekearney on 24/09/2016.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseTests {
    private DatabaseManager testManager;

    @Before
    public void setUp() throws Exception {
        testManager = new DatabaseManager(getTargetContext());
        DatabaseHelper.setLocalDB(testManager);
    }

    @Test
    public void test_mla_database() throws Exception {
        // Here I am simply adding one to make sure that when we delete all it really has worked.
        Assert.assertNotNull(DatabaseHelper.getMlaHelper().addMLA("1", "Test", "MLA", "testimageurl", "VMJK", "Mikhail", "CTO", "Belfast"));
        // For testing purposes we will start by wiping the DB of MLAs
        DatabaseHelper.getMlaHelper().deleteAll();
        // Making sure that the deleteAll works
        assertThat(DatabaseHelper.getMlaHelper().getAllObjects().size(), is(0));

        // Test adding counsellors to DB
        MLA newC = DatabaseHelper.getMlaHelper().addMLA("2", "Vince", "Kearney", "vinceimageurl", "VK", "Kearney", "CTO", "Lurgan");
        Assert.assertNotNull(newC);
        MLA newC1 = DatabaseHelper.getMlaHelper().addMLA("3", "Andrew", "Cunningham", "andrewimageurl", "AC", "Cunningham", "CEO", "Hillsborough");
        Assert.assertNotNull(newC1);
        MLA newC2 = DatabaseHelper.getMlaHelper().addMLA("4", "Marc", "Nevin", "marcimageurl", "MN", "Nevin", "COO", "Lisburn");
        Assert.assertNotNull(newC2);

        // Make sure the DB contains the 3 that were just created
        assertThat(DatabaseHelper.getMlaHelper().getAllObjects().size(), is(3));

        // Search for a MLA with ID = newC1.getMLA_ID()
        MLA searchForNewC1 = DatabaseHelper.getMlaHelper().fetchMlaWithID(newC1.getMLA_ID());
        Assert.assertNotNull(searchForNewC1);

        // Now let's make sure that we can add a TwitterHandle
        String testHandle = "@VinceBoiiiii";
        DatabaseHelper.getMlaHelper().updateTwitterHandle(searchForNewC1, testHandle);
        String mlaHandler = DatabaseHelper.getMlaHelper().fetchMlaWithID(searchForNewC1.getMLA_ID()).getTwitterHandle();
        Assert.assertEquals(mlaHandler, testHandle);

        // Make sure we can delete a specific MLA
        DatabaseHelper.getMlaHelper().deleteMLA(newC.getMLA_ID());
        Assert.assertNull(DatabaseHelper.getMlaHelper().fetchMlaWithID(newC.getMLA_ID()));
        // Now to finish make sure the DB contains only 2
        assertThat(DatabaseHelper.getMlaHelper().getAllObjects().size(), is(2));

        // Now let's clear the DB before we go using the app
        DatabaseHelper.getMlaHelper().deleteAll();
        assertThat(DatabaseHelper.getMlaHelper().getAllObjects().size(), is(0));
    }

    @Test
    public void test_party_database() throws Exception {
        // Add party so that we can check the deleteAll() works
        Assert.assertNotNull(DatabaseHelper.getPartyHelper().addParty("123", "Thunder Echo Sabre", "@TESMAV", "www.whogivesafuck.com"));
        // For testing, wipe the current DB
        DatabaseHelper.getPartyHelper().deleteAll();
        // Make sure the Party DB was wiped
        assertThat(DatabaseHelper.getPartyHelper().getAllObjects().size(), is(0));

        // Let's add some parties
        Party partyVince = DatabaseHelper.getPartyHelper().addParty("1", "V", "@V", "www.V.com");
        Assert.assertNotNull(partyVince);
        Party partyMarc = DatabaseHelper.getPartyHelper().addParty("2", "M", "@M", "www.M.com");
        Assert.assertNotNull(partyMarc);
        Party partyAndrew = DatabaseHelper.getPartyHelper().addParty("3", "A", "@A", "www.A.com");
        Assert.assertNotNull(partyAndrew);

        // Check we successfully added the 3 parties
        assertThat(DatabaseHelper.getPartyHelper().getAllObjects().size(), is(3));

        // Make sure we can fetch a party with respect to the ID
        Assert.assertNotNull(DatabaseHelper.getPartyHelper().fetchParty(partyVince.getPartyId()));

        // Delete a specific party
        DatabaseHelper.getPartyHelper().deleteParty(partyVince.getPartyId());
        // If we search for the party we just deleted we should get null
        Assert.assertNull(DatabaseHelper.getPartyHelper().fetchParty(partyVince.getPartyId()));
        assertThat(DatabaseHelper.getPartyHelper().getAllObjects().size(), is(2));

        // To finsih, wipe the DB of test Parties
        DatabaseHelper.getPartyHelper().deleteAll();
        assertThat(DatabaseHelper.getPartyHelper().getAllObjects().size(), is(0));
    }
}
