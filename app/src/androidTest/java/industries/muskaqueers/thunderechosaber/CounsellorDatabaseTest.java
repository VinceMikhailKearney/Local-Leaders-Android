package industries.muskaqueers.thunderechosaber;

import android.support.test.runner.AndroidJUnit4;
import static android.support.test.InstrumentationRegistry.getTargetContext;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

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
        testHelper.deleteAllCounsellors();
        List<Counsellor> counsellors = testHelper.getAllCounsellors();
        assertThat(counsellors.size(), is(0));
    }
}
