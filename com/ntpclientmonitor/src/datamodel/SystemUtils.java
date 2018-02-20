package com.ntpclientmonitor.src.datamodel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

enum OsType {WINDOWS, LINUX};

public class SystemUtils {

    public static OsType getOsType() {
        String typeString = System.getProperty("os.name");
        if (typeString.equals("Linux")) {

        } else if (typeString.equals("Windows")) {

        } else {

        }
        return OsType.LINUX;
    }

    public static String getCommandLocation(String command) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("cmd /c dir");
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bri.readLine()) != null) {
                System.out.println(line);
            }
            bri.close();
        } catch (Exception exception) {
            System.err.println(exception.getLocalizedMessage());
        }
        return null;
    }
}