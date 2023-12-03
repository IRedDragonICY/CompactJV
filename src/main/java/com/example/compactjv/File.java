package com.example.compactjv;

import java.io.*;


public class File {
    private static final String CMD = "cmd.exe";
    private static final String QUERY_COMMAND = "compact /Q /A /I /S:";
    private String filePath;

    private Compressor compressor = new Compressor();

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void compress(String filePath, String algorithm) {
        compressor.compress(filePath, algorithm);
    }

    public void decompress(String filePath) {
        compressor.decompress(filePath);
    }

    public boolean isCompressed(String filePath) {
        String output = runCommandWithOutput(QUERY_COMMAND + "\"" + filePath + "\"");
        String Lines[] = output.split("\\n");
        for (String line : Lines) {
            if (line.contains(" are compressed")) {
                String[] parts = line.split(" ");
                Long total = Long.parseLong(parts[0].replace(".", ""));
                return total != 0;
            }
        }
        return false;
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
                System.out.println(line);
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
