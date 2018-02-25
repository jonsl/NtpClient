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