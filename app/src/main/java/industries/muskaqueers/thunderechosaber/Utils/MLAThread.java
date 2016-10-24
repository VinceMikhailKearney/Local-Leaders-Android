package industries.muskaqueers.thunderechosaber.Utils;

import android.util.Log;

import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Events.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.ParserUtils;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 17/10/2016.
 */

public class MLAThread extends Thread {

    private List<MLA> mlaArray;
    private ProcessImage imageProcessor;
    private int downloadImageCount;
    private int threadNumber;

    public MLAThread(List<MLA> array, int threadNumber) {
        this.mlaArray = array;
        this.imageProcessor = new ProcessImage(threadNumber);
        this.downloadImageCount = 0;
        this.threadNumber = threadNumber;
        EventBus.getDefault().register(this);
    }

    public void run()
    {
        for (MLA mla : mlaArray) {
            DatabaseManager.mlaHelper().addMLA(mla.getMLA_ID(),
                    mla.getFirstName(),
                    mla.getLastName(),
                    mla.getImageURL(),
                    mla.getPartyAbbreviation(),
                    mla.getPartyName(),
                    mla.getTitle(),
                    mla.getConstituency());

            // Now that the MLA is in the DB, let's update the TwitterHandle
            DatabaseManager.mlaHelper().updateTwitterHandle(mla, ParserUtils.findHandleFor(mla.getFirstName(), mla.getLastName()));
            // Now that the MLA is in the DB, let's update the EmailAddress
            DatabaseManager.mlaHelper().updateEmailAddress(mla, ParserUtils.findEmailFor(mla.getFirstName(), mla.getLastName()));
            // Async download the image and store in DB against the MLA
            imageProcessor.getDataFromImage(mla.getImageURL(), mla.getMLA_ID(), ProcessImage.type.MLA);
        }

        EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
    }

    public void onEvent(DatabaseEvent event) {
        if(event.getEventType() == DatabaseEvent.type.DownloadedImage && this.threadNumber == event.getThreadNumber()) {
            downloadImageCount++;
            if(downloadImageCount == 10 || downloadImageCount == (DatabaseManager.mlaHelper().getTotalMlaCount() - 100))
                EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs)); 
        }
    }
}
