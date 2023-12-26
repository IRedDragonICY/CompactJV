package com.example.compactjv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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

    public static String runCommandWithDebug(String command, boolean withOutput) {
        StringBuilder output = new StringBuilder();
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(CMD, "/c", command);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
                System.out.println(line);
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return withOutput ? output.toString() : null;
    }
}
