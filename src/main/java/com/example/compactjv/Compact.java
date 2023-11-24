package com.example.compactjv;

import java.io.*;

public class Compact {

    public void compress(String filePath, String algorithm) {
        String command = "start cmd.exe /K compact /C /F /EXE:" + algorithm + " /s:" + filePath;
        runCommand(command);
    }

    private void runCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        try {
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
