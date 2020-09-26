package fr.ulity.core_v3.modules.eventsCheckers;

public abstract class EventChecker<E> {
    public abstract void execute(E event);
}
