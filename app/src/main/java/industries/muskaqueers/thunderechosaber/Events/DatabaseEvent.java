package industries.muskaqueers.thunderechosaber.Events;

import java.util.List;

import industries.muskaqueers.thunderechosaber.MLA;

/**
 * Created by vincekearney on 30/09/2016.
 */

public class DatabaseEvent {
    public enum type {
        UpdateMLAs, OnClickMla, UpdateParties, DownloadedImage, ProcessMLAs
    }
    private type eventType;
    private MLA mla;
    private List<MLA> mlaList;

    public DatabaseEvent(type type) {
        this.eventType = type;
    }

    public void setMLA(MLA mla) {
        this.mla = mla;
    }

    public MLA getMLA() {
        return this.mla;
    }

    public List<MLA> getMlaList() {
        return mlaList;
    }

    public void setMlaList(List<MLA> mlaList) {
        this.mlaList = mlaList;
    }

    public type getEventType() {
        return this.eventType;
    }
}
