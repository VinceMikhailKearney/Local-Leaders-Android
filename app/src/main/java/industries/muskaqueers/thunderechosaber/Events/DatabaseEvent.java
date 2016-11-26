package industries.muskaqueers.thunderechosaber.Events;

import java.util.List;

import industries.muskaqueers.thunderechosaber.Database.MLADb;

/**
 * Created by vincekearney on 30/09/2016.
 */

public class DatabaseEvent {
    public enum type {
        UpdateMLAs, UpdateParties, DownloadedImage, ProcessMLAs
    }
    private type eventType;
    private MLA mla;
    private List<MLA> mlaList;
    private List<MLADb> mlaListNew;


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

    public List<MLADb> getMlaListNew(){
        return mlaListNew;
    }

    public DatabaseEvent setMlaList(List<MLA> mlaList) {
        this.mlaList = mlaList;
        return this;
    }

    public DatabaseEvent setMlaListNew(List<MLADb> mlaList) {
        this.mlaListNew = mlaList;
        return this;
    }

    public type getEventType() {
        return this.eventType;
    }
}
