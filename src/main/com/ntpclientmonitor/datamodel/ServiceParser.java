package com.ntpclientmonitor.datamodel;

public class ServiceParser extends StreamLineParser {
    private String serviceName;
    private String serviceCaption;
    private String serviceDescription;
    private String servicePathName;
    private String serviceStartMode;
    private String serviceState;

    public ServiceParser() {
        super("OUTPUT");
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getServiceCaption() {
        return serviceCaption;
    }

    public String getServiceDescription() {
        return serviceDescription;
    }

    public String getServicePathName() {
        return servicePathName;
    }

    public String getServiceStartMode() {
        return serviceStartMode;
    }

    public String getServiceState() {
        return serviceState;
    }

    @Override
    public void parseLine(String line) {
        if (line.length() > 0) {
            String[] tokens = line.split("=");
            switch (tokens[0]) {
                case "Caption":
                    serviceCaption = tokens[1];
                    break;
                case "Description":
                    serviceDescription = tokens[1];
                    break;
                case "Name":
                    serviceName = tokens[1];
                    break;
                case "PathName":
                    servicePathName = tokens[1];
                    break;
                case "StartMode":
                    serviceStartMode = tokens[1];
                    break;
                case "State":
                    serviceState = tokens[1];
                    break;
            }
        }
    }
}