package com.example.compactjv;

import java.io.*;

public class CommandRunner {
    private static final String CMD = "cmd.exe";

    public static String runCommand(String command, boolean withOutput) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(CMD, "/c", command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return withOutput ? output.toString() : null;
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
