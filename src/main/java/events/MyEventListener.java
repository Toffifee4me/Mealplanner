package events;

import java.util.EventListener;

public interface MyEventListener extends EventListener {
    void handleMyEvent(MyEvent event);
}
