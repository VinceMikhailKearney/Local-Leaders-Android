package industries.muskaqueers.thunderechosaber.Utils;

import java.util.List;

import industries.muskaqueers.thunderechosaber.DB.DatabaseManager;
import industries.muskaqueers.thunderechosaber.Party;
import industries.muskaqueers.thunderechosaber.UI.ProcessImage;

/**
 * Created by vincekearney on 23/10/2016.
 */

public class PartyThread extends Thread {
    private List<Party> partyList;
    private ProcessImage processImage;

    public PartyThread(List<Party> array) {
        this.partyList = array;
        this.processImage = new ProcessImage();
    }

    public void run()
    {
        for(Party party : (this.partyList)) {
            DatabaseManager.partyHelper().addParty(party.getPartyId(),
                    party.getName(),
                    party.getTwitterHandle(),
                    party.getImageURL());

            processImage.getDataFromImage(party.getImageURL(), party.getPartyId(), ProcessImage.type.Party);
        }
    }
}
