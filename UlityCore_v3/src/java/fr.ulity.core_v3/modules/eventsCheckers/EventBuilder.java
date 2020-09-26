package fr.ulity.core_v3.modules.eventsCheckers;

import org.bukkit.event.Cancellable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventBuilder {
    public List<EventChecker<?>> checkers = new ArrayList<>();
    public EventChecker finalChecker;

    public EventBuilder addChecker (EventChecker<?> ...chk) { checkers.addAll(Arrays.asList(chk)); return this; }
    public EventBuilder addChecker (EventChecker<?> chk) { checkers.add(chk); return this; }

    public EventBuilder setFinal (EventChecker fin) { this.finalChecker = fin; return this; }

    public EventBuilder removeChecker (EventChecker<?> ...chk) { checkers.removeAll(Arrays.asList(chk)); return this; }
    public EventBuilder removeChecker (EventChecker<?> chk) { checkers.remove(chk); return this; }


    public void execute (Cancellable event) {
        for (EventChecker checker : checkers) {
            checker.execute(event);
            if (event.isCancelled()) break;
        }
        if (finalChecker != null && !event.isCancelled())
            finalChecker.execute(event);
    }
}
