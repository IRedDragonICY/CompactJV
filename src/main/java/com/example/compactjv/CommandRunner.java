package com.example.compactjv;

import java.io.*;

public class CommandRunner {
    private static final String CMD = "cmd.exe";

    public static void runCommand(String command) {
        try {
            Process process = startCommandProcess(command);
            drainCommandOutput(process);
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String runCommandWithOutput(String command) {
        StringBuilder output = new StringBuilder();
        try {
            Process process = startCommandProcess(command);
            output.append(readCommandOutput(process));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private static Process startCommandProcess(String command) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder(CMD, "/c", command);
        return processBuilder.start();
    }

    private static void drainCommandOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

        while (reader.readLine() != null) {
        }
    }


    private static String readCommandOutput(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder output = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            output.append(line).append("\n");
        }
        return output.toString();
    }
}
