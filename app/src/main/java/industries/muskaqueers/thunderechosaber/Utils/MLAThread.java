package industries.muskaqueers.thunderechosaber.Utils;

import java.util.List;

import de.greenrobot.event.EventBus;
import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.DatabaseEvent;
import industries.muskaqueers.thunderechosaber.MLA;
import industries.muskaqueers.thunderechosaber.PasrserUtils;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 17/10/2016.
 */

public class MLAThread extends Thread {

    private List<MLA> mlaArray;
    private ProcessImage imageProcessor;
    private int downloadImageCount;

    public MLAThread(List<MLA> array) {
        this.mlaArray = array;
        this.imageProcessor = new ProcessImage();
        this.downloadImageCount = 0;
    }

    public void run()
    {
        for (MLA mla : mlaArray) {
            MLA addMLA = DatabaseManager.mlaHelper().addMLA(mla.getMLA_ID(),
                    mla.getFirstName(),
                    mla.getLastName(),
                    mla.getImageURL(),
                    mla.getPartyAbbreviation(),
                    mla.getPartyName(),
                    mla.getTitle(),
                    mla.getConstituency());

            // Now that the MLA is in the DB, let's update the TwitterHandle
            DatabaseManager.mlaHelper().updateTwitterHandle(addMLA, PasrserUtils.findHandleFor(mla.getFirstName(), mla.getLastName()));
            // Async download the image and store in DB against the MLA
            imageProcessor.getDataFromImage(mla.getImageURL(), mla.getMLA_ID(), ProcessImage.type.MLA);
        }

        EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
    }

    public void onEvent(DatabaseEvent event) {
        if(event.getEventType() == DatabaseEvent.type.DownloadedImage) {
            this.downloadImageCount++;

            if(this.downloadImageCount == 10) {
                EventBus.getDefault().post(new DatabaseEvent(DatabaseEvent.type.UpdateMLAs));
            }
        }
    }
}
