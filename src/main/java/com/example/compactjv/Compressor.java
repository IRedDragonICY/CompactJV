package com.example.compactjv;

import java.io.*;

public class Compressor {
    private static final String CMD = "cmd.exe";
    private static final String START_COMMAND = "start /B " + CMD + " /c compact";

    public void compress(String filePath, String algorithm) {
        runCommand(START_COMMAND + " /A /C /I /S /F /EXE:" + algorithm + " /s:\"" + filePath + "\"");
    }

    public void decompress(String filePath) {
        runCommand(START_COMMAND + " /U /I /A /F /s:\"" + filePath + "\"");
    }

    private void runCommand(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder(CMD, "/c", command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            while (reader.readLine() != null) {
            }
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
