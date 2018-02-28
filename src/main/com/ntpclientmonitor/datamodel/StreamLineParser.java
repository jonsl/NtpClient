package com.ntpclientmonitor.datamodel;

import com.ntpclientmonitor.datamodel.Observable;
import com.ntpclientmonitor.datamodel.Observer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class StreamLineParser extends Thread implements Observable {
    private final String type;
    private ArrayList<Observer> observers = new ArrayList<>();
    private InputStream inputStream;

    StreamLineParser(String type) {
        this.type = type;
    }

    StreamLineParser(InputStream inputStream, String type) {
        this.type = type;
        this.inputStream = inputStream;
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
        for (Observer observer : observers) {
            observer.onNotify();
        }
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        try {
            // print any error info
            BufferedReader bre = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bre.readLine()) != null) {
                parseLine(line);
            }
            bre.close();
            notifyObservers();
        } catch (Exception exception) {
            System.err.println("exception: " + exception.getLocalizedMessage());
        }
    }

    public void parseLine(String line) {
    }
}