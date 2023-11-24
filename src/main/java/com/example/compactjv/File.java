package com.example.compactjv;

import java.io.*;

public class File {

    public void compress(String filePath, String algorithm) {
        String command = "start cmd.exe /K compact /C /F /EXE:" + algorithm + " /s:" + filePath;
        System.out.printf(command);
        runCommand(command);
    }

    public void decompress(String filePath) {
        String command = "start cmd.exe /K compact /U /s:" + filePath;
        runCommand(command);
    }
    public boolean isCompressed(String filePath) {
        String command = "compact /Q /S:" + filePath;
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command, filePath);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("are compressed")) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public long calculateFolderSize(java.io.File folder) {
        long length = 0;
        java.io.File[] files = folder.listFiles();

        if (files != null) {
            for (java.io.File file : files) {
                length += (file.isFile()) ? file.length() : calculateFolderSize(file);
            }
        }
        return length;
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
