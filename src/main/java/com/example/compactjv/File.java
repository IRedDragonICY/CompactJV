package com.example.compactjv;

import java.io.*;


public class File {
    private static final String CMD = "cmd.exe";
    private static final String QUERY_COMMAND = "compact /Q /A /I /S:";
    private String filePath;
    private Long totalCompressed;
    private Long totalDecompressed;
    private Compressor compressor = new Compressor();

    public String getFilePath() {
        return filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
    public Long getTotalCompressed() {
        return totalCompressed;
    }
    public void setTotalCompressed(Long totalCompressed) {
        this.totalCompressed = totalCompressed;
    }
    public Long getTotalDecompressed() {
        return totalDecompressed;
    }
    public void setTotalDecompressed(Long totalDecompressed) {
        this.totalDecompressed = totalDecompressed;
    }

    public void compress(String filePath, String algorithm) {
        compressor.compress(filePath, algorithm);
    }

    public void decompress(String filePath) {
        compressor.decompress(filePath);
    }

    public boolean isCompressed(String filePath) {
        String output = CommandRunner.runCommand(QUERY_COMMAND + "\"" + filePath + "\"", true);
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
        String output = CommandRunner.runCommand(QUERY_COMMAND + "\"" + filePath + "\"", true);
        return getSizeFromOutput(output);
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
    public void getTotalFolderCompressed(String filePath) {
        String output = CommandRunner.runCommand("compact /Q /A /I /S:\"" + filePath + "\"", true);
        String Lines[] = output.split("\\n");
        for (String line : Lines) {
            if (line.contains(" are compressed")) {
                String[] parts = line.split(" ");
                Long totalCompressed = Long.parseLong(parts[0].replace(".", ""));
                Long totalDecompressed = Long.parseLong(parts[4].replace(".", ""));
                setTotalCompressed(totalCompressed);
                setTotalDecompressed(totalDecompressed);
                break;
            }
        }
    }

    public boolean isValidDirectory(String path) {
        if (path != null && !path.isEmpty()) {
            java.io.File file = new java.io.File(path);
            return file.exists() && file.isDirectory();
        }
        return false;
    }
}
