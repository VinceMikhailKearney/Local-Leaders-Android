package industries.muskaqueers.thunderechosaber;

/**
 * Created by vincekearney on 30/09/2016.
 */

public class DatabaseEvent {
    public enum type {
        UpdateMLAs, OnClickMla, UpdateParties
    }
    private type eventType;
    private MLA mla;

    public DatabaseEvent(type type) {
        this.eventType = type;
    }

    public void setMLA(MLA mla) {
        this.mla = mla;
    }

    public MLA getMLA() {
        return this.mla;
    }

    public type getEventType() {
        return this.eventType;
    }
}
