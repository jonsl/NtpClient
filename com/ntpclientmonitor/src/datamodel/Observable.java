package com.ntpclientmonitor.src.datamodel;

public interface Observable {
    /**
     * add observer to receive notifications
     *
     * @param observer object to receive events
     */
    void addObserver(Observer observer);

    /**
     * remove observer to no longer receive notifications
     *
     * @param observer observer to remove
     */
    void removeObserver(Observer observer);

    /**
     * notify all observers
     */
    void notifyObservers();
}