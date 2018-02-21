package com.ntpclientmonitor.src.datamodel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

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

    public ServiceInfo getServiceInfo(String serviceName) {
        try {
            Process p = Runtime.getRuntime().exec("wmic service " + serviceName + " get " +
                    "Caption, Description, Name, StartMode, State, PathName /format:list");
            BufferedReader bri = new BufferedReader
                    (new InputStreamReader(p.getInputStream()));
            ServiceInfo serviceInfo = new ServiceInfo();
            String line;
            while ((line = bri.readLine()) != null) {
                serviceInfo = parseLine(line, serviceInfo);
                System.out.println(line);
            }
            bri.close();
            // print any error info
            BufferedReader bre = new BufferedReader
                    (new InputStreamReader(p.getErrorStream()));
            while ((line = bre.readLine()) != null) {
                System.err.println(line);
            }
            bre.close();
            p.waitFor();
            return serviceInfo;
        } catch (Exception exception) {
            System.err.println("exception: " + exception.getLocalizedMessage());
        }
        return null;
    }

    private ServiceInfo parseLine(String line, ServiceInfo serviceInfo) {
        if (line.length() > 0) {
            String[] tokens = line.split("=");
            switch (tokens[0]) {
                case "Caption":
                    serviceInfo.caption = tokens[1];
                    break;
                case "Description":
                    serviceInfo.description = tokens[1];
                    break;
                case "Name":
                    serviceInfo.name = tokens[1];
                    break;
                case "PathName":
                    serviceInfo.pathName = tokens[1];
                    break;
                case "StartMode":
                    serviceInfo.startMode = tokens[1];
                    break;
                case "State":
                    serviceInfo.state = tokens[1];
                    break;
            }
        }
        return serviceInfo;
    }

    public List<Peer> getPeerList() {
        try {
            Process p = Runtime.getRuntime().exec("ntpq -pn");
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            // skip header
            reader.readLine();
            reader.readLine();
            // read peers list
            String line;
            ArrayList<Peer> peers = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String strings[] = line.substring(1).split("\\s+");
                if (strings.length != 10) {
                    return null;
                }
                peers.add(new Peer(line.substring(0, 1), strings[0], strings[1], strings[2],
                        strings[3], strings[4], strings[5], strings[6], strings[7], strings[8], strings[9]));
            }
            reader.close();
            return peers;
        } catch (Exception exception) {
            System.err.println(exception.getLocalizedMessage());
        }
        return null;
    }

    public enum OperatingSystemType {UNKNOWN, WINDOWS, LINUX}

    public class ServiceInfo {
        String name;
        String caption;
        String description;
        String pathName;
        String startMode;
        String state;

        public String getName() {
            return name;
        }

        public String getCaption() {
            return caption;
        }

        public String getDescription() {
            return description;
        }

        public String getPathName() {
            return pathName;
        }

        public String getStartMode() {
            return startMode;
        }

        public String getState() {
            return state;
        }
    }

    public class Peer {
        String s;
        String remote;
        String refid;
        String st;
        String t;
        String when;
        String poll;
        String reach;
        String delay;
        String offset;
        String jitter;

        Peer(String s, String remote, String refid, String st, String t,
             String when, String poll, String reach, String delay, String offset, String jitter) {
            this.s = s;
            this.remote = remote;
            this.refid = refid;
            this.st = st;
            this.t = t;
            this.when = when;
            this.poll = poll;
            this.reach = reach;
            this.delay = delay;
            this.offset = offset;
            this.jitter = jitter;
        }

        public String getS() {
            return s;
        }

        public String getRemote() {
            return remote;
        }

        public String getRefid() {
            return refid;
        }

        public String getSt() {
            return st;
        }

        public String getT() {
            return t;
        }

        public String getWhen() {
            return when;
        }

        public String getPoll() {
            return poll;
        }

        public String getReach() {
            return reach;
        }

        public String getDelay() {
            return delay;
        }

        public String getOffset() {
            return offset;
        }

        public String getJitter() {
            return jitter;
        }
    }
}