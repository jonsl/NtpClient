package com.ntpclientmonitor.src.datamodel;

import javafx.application.Platform;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

public abstract class DataGroup implements Observable {
    //
    private ArrayList<Observer> observers = new ArrayList<>();

    protected DataGroup() {
    }

    protected void getData() {
        throw new NotImplementedException();
    }

    @Override
    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        Platform.runLater(
                () -> {
                    for (Observer observer : observers) {
                        observer.onNotify();
                    }
                }
        );
    }
}