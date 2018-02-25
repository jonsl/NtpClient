package com.ntpclientmonitor.src.datamodel;

import com.sun.istack.internal.NotNull;

public class CommandExecutor {
    private final String command;

    public CommandExecutor(String command) {
        this.command = command;
    }

    public int exec(@NotNull final StreamLineParser inputParser) {
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            // init parsers
            inputParser.setInputStream(proc.getInputStream());
            StreamLineParser errorParser = new StreamLineParser(proc.getErrorStream(), "ERROR");
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