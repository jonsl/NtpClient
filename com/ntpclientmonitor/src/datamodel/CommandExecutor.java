package com.ntpclientmonitor.src.datamodel;

public class CommandExecutor {
    private final String command;

    public CommandExecutor(String command) {
        this.command = command;
    }

    public int exec(StreamLineParser inputParser, StreamLineParser errorParser) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            // init parsers
            if (inputParser == null) {
                inputParser = new StreamLineParser("OUT");
            }
            inputParser.setInputStream(proc.getInputStream());
            if (errorParser == null) {
                errorParser = new StreamLineParser("ERROR");
            }
            errorParser.setInputStream(proc.getErrorStream());
            // start parsers
            inputParser.start();
            errorParser.start();
            // wait for process and return exit code
            return proc.waitFor();
        } catch (Throwable t) {
            t.printStackTrace();
        }
        return -1;
    }

    public String getCommand() {
        return command;
    }
}