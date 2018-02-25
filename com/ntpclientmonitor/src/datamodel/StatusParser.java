package com.ntpclientmonitor.src.datamodel;

import java.util.ArrayList;
import java.util.List;

public class StatusParser extends StreamLineParser {
    private List<Peer> peers = new ArrayList<>();

    public StatusParser() {
        super("OUTPUT");
    }

    public List<Peer> getPeers() {
        return peers;
    }

    @Override
    public void parseLine(String line) {
        super.parseLine(line);
        String strings[] = line.substring(1).split("\\s+");
        if (strings.length != 10) {
            return;
        }
        peers.add(new Peer(line.substring(0, 1), strings[0], strings[1], strings[2],
                strings[3], strings[4], strings[5], strings[6], strings[7], strings[8], strings[9]));
    }

    public class Peer {
        private String select;
        private String remote;
        private String refid;
        private String stratum;
        private String type;
        private String when;
        private String poll;
        private String reach;
        private String delay;
        private String offset;
        private String jitter;

        Peer(String select, String remote, String refid, String stratum, String type, String when, String poll, String reach, String delay, String offset, String jitter) {
            this.select = select;
            this.remote = remote;
            this.refid = refid;
            this.stratum = stratum;
            this.type = type;
            this.when = when;
            this.poll = poll;
            this.reach = reach;
            this.delay = delay;
            this.offset = offset;
            this.jitter = jitter;
        }

        public String getSelect() {
            return select;
        }

        public String getRemote() {
            return remote;
        }

        public String getRefid() {
            return refid;
        }

        public String getStratum() {
            return stratum;
        }

        public String getType() {
            return type;
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