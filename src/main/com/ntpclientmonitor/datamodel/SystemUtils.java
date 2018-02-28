package com.ntpclientmonitor.datamodel;

public class SystemUtils {
    private static SystemUtils ourInstance = new SystemUtils();
    //
    private OperatingSystemType operatingSystemType;

    private SystemUtils() {
        // determine os type
        String typeString = System.getProperty("os.name");
        if (typeString.startsWith("Linux")) {
            this.operatingSystemType = OperatingSystemType.LINUX;
        } else if (typeString.startsWith("Windows")) {
            this.operatingSystemType = OperatingSystemType.WINDOWS;
        } else {
            this.operatingSystemType = OperatingSystemType.UNKNOWN;
        }
    }

    public static SystemUtils getInstance() {
        return ourInstance;
    }

    public OperatingSystemType getOperatingSystemType() {
        return operatingSystemType;
    }

    public enum OperatingSystemType {
        UNKNOWN,
        WINDOWS,
        LINUX
    }
}