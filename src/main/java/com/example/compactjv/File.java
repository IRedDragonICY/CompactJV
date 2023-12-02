package com.example.compactjv;

import java.io.*;

public class File {
    private static final String CMD = "cmd.exe";
    private static final String START_COMMAND = "start /B " + CMD + " /c compact";
    private static final String QUERY_COMMAND = "compact /Q /I /S:";
    private String filePath;

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void compress(String filePath, String algorithm) {
        runCommand(START_COMMAND + " /A /C /I /S /F /EXE:" + algorithm + " /s:\"" + filePath + "\"");
    }

    public void decompress(String filePath) {
        runCommand(START_COMMAND + " /U /I /A /F /s:\"" + filePath + "\"");
    }

    public boolean isCompressed(String filePath) {
        String output = runCommandWithOutput(QUERY_COMMAND + "\"" + filePath + "\"");
        return !output.contains("0 are compressed");
    }

    public Size calculateFolderSize(String filePath) {
        String output = runCommandWithOutput(QUERY_COMMAND + "\"" + filePath + "\"");
        return getSizeFromOutput(output);
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

    private String runCommandWithOutput(String command) {
        ProcessBuilder processBuilder = new ProcessBuilder(CMD, "/c", command);
        StringBuilder output = new StringBuilder();
        try {
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return output.toString();
    }

    private Size getSizeFromOutput(String output) {
        long size = 0;
        long sizeOnDisk = 0;
        String[] lines = output.split("\n");
        for (String line : lines) {
            if (line.contains("total bytes of data")) {
                String[] parts = line.split(" ");
                size = Long.parseLong(parts[0].replace(".", ""));
                sizeOnDisk = Long.parseLong(parts[8].replace(".", ""));
                break;
            }
        }
        return new Size(size, sizeOnDisk);
    }
    public boolean isValidDirectory(String path) {
        if (path != null && !path.isEmpty()) {
            java.io.File file = new java.io.File(path);
            return file.exists() && file.isDirectory();
        }
        return false;
    }
}
