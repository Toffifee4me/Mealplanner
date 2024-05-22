package Events;

import java.util.EventListener;

public interface MyEventListener extends EventListener {
    public void handleMyEvent(MyEvent event);
}
