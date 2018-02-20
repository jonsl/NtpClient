package com.ntpclientmonitor.src.datamodel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

enum OsType {WINDOWS, LINUX};

public class SystemUtils {

    public String getOsCommandPrefix() {
        String typeString = System.getProperty("os.name");
        if (typeString.startsWith("Linux")) {
            return "";
        } else if (typeString.startsWith("Windows")) {
            return "cmd /c ";
        }
        return null;
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
                Character selection = line.charAt(0);
                String strings[] = line.substring(1).split("\\s+");
                if (strings.length != 10) {
                    throw new RuntimeException("wrong number of peer entries: " + strings.length);
                }
                peers.add(new Peer(
                                selection,
                                strings[0],
                                strings[1],
                                Integer.parseInt(strings[2]),
                                strings[3].charAt(0),
                                Integer.parseInt(strings[4]),
                                Integer.parseInt(strings[5]),
                                Integer.parseInt(strings[6]),
                                Double.parseDouble(strings[7]),
                                Double.parseDouble(strings[8]),
                                Double.parseDouble(strings[9])
                        )
                );
            }
            reader.close();
            return peers;
        } catch (Exception exception) {
            System.err.println(exception.getLocalizedMessage());
        }
        return null;
    }

    public class Peer {
        char selection;
        String remote;
        String refid;
        int st;
        char t;
        int when;
        int poll;
        int reach;
        double delay;
        double offset;
        double jitter;

        Peer(char selection, String remote, String refid, int st, char t,
             int when, int poll, int reach, double delay, double offset, double jitter) {
            this.selection = selection;
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

        public char getSelection() {
            return selection;
        }

        public String getRemote() {
            return remote;
        }

        public String getRefid() {
            return refid;
        }

        public int getSt() {
            return st;
        }

        public char getT() {
            return t;
        }

        public int getWhen() {
            return when;
        }

        public int getPoll() {
            return poll;
        }

        public int getReach() {
            return reach;
        }

        public double getDelay() {
            return delay;
        }

        public double getOffset() {
            return offset;
        }

        public double getJitter() {
            return jitter;
        }
    }
}