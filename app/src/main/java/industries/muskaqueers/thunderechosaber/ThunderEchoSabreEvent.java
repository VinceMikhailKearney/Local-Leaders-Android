package industries.muskaqueers.thunderechosaber;

/**
 * Created by vincekearney on 30/09/2016.
 */

public class ThunderEchoSabreEvent {
    public enum eventBusEventType {
        UPDATE_MLAS, ON_CLICK_MLA
    }
    private eventBusEventType eventType;
    private MLA mla;

    public ThunderEchoSabreEvent(eventBusEventType type) {
        this.eventType = type;
    }

    public void setMLA(MLA mla) {
        this.mla = mla;
    }

    public MLA getMLA() {
        return this.mla;
    }

    public eventBusEventType getEventType() {
        return this.eventType;
    }
}
