package com.example.compactjv;

import java.io.*;
public class File {

    public void compress(String filePath, String algorithm) {
        String command = "start cmd.exe /K compact /C /F /EXE:" + algorithm + " /s:\"" + filePath + "\"";
        System.out.printf(command);
        runCommand(command);
    }

    public void decompress(String filePath) {
        String command = "start cmd.exe /K compact /U /F /s:\"" + filePath + "\"";
        runCommand(command);
    }
    public boolean isCompressed(String filePath) {
        String command = "compact /Q /S:\"" + filePath + "\"";
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
                if (line.contains("0 are compressed")) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public Size calculateFolderSize(String filePath) {
        String command = "compact /Q /S:\"" + filePath + "\"";
        ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", command);
        long size = 0;
        long sizeOnDisk = 0;
        System.out.println(filePath);
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("total bytes of data")) {
                    String[] parts = line.split(" ");
                    size = Long.parseLong(parts[0].replace(",", ""));
                    sizeOnDisk = Long.parseLong(parts[8].replace(",", ""));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Size(size, sizeOnDisk);
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
