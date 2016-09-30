package industries.muskaqueers.thunderechosaber;

/**
 * Created by vincekearney on 30/09/2016.
 */

public class ThunderEchoSabreEvent {
    public enum eventBusEventType {
        UPDATE_MLAS
    }
    private eventBusEventType eventType;

    public ThunderEchoSabreEvent(eventBusEventType type) {
        this.eventType = type;
    }

    public eventBusEventType getEventType() {
        return this.eventType;
    }
}
