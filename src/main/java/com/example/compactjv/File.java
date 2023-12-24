package com.example.compactjv;

import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
public class File {
    private static final String CMD = "cmd.exe";
    private static final String QUERY_COMMAND = "compact /Q /A /I /S:";
    private String filePath;
    private Long totalCompressed;
    private Long totalDecompressed;
    private final Compressor compressor = new Compressor();
    private final int indexOfCompressed = 0;
    private final int indexOfDecompressed = 4;

    public void compress(String filePath, String algorithm) {
        compressor.compress(filePath, algorithm);
    }

    public void decompress(String filePath) {
        compressor.decompress(filePath);
    }

    public static String getCPUUsage() {
        String output = CommandRunner.runCommand("wmic cpu get loadpercentage", true);
        String[] lines = Objects.requireNonNull(output).split("\n");
        if(lines.length < 3) {
            return "0";
        }
        String[] parts = lines[2].split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return "0";
        }
        return parts[0];
    }
    public static String getMemoryUsage() {
        String output = CommandRunner.runCommand("wmic OS get FreePhysicalMemory,TotalVisibleMemorySize /Value", true);
        String[] lines = Objects.requireNonNull(output).split("\n");
        long[] memory = new long[2];
        for (String line : lines) {
            if (line.startsWith("FreePhysicalMemory")) {
                String value = line.split("=")[1];
                memory[0] = Long.parseLong(value);
            } else if (line.startsWith("TotalVisibleMemorySize")) {
                String value = line.split("=")[1];
                memory[1] = Long.parseLong(value);
            }
        }
        double percentage = 100 * (1 - (double)memory[0] / (double)memory[1]);
        return String.format("%.2f", percentage);
    }


    public long getTotalFilesInFolder() {
        String filePath = getFilePath();
        Path path = Paths.get(filePath);
        long totalFiles = 0;

        try {
            totalFiles = Files.walk(path)
                    .parallel()
                    .filter(p -> p.toFile().isFile())
                    .count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalFiles;
    }




    public boolean isCompressed(String filePath) {
        String output = CommandRunner.runCommand(QUERY_COMMAND + "\"" + filePath + "\"", true);
        return Arrays.stream(Objects.requireNonNull(output).split("\\n"))
                .filter(line -> line.contains(" are compressed"))
                .map(line -> Long.parseLong(line.split(" ")[0].replace(".", "")))
                .anyMatch(total -> total != 0);
    }


    public Size calculateFolderSize(String filePath) {
        String output = CommandRunner.runCommand(QUERY_COMMAND + "\"" + filePath + "\"", true);
        return getSizeFromOutput(Objects.requireNonNull(output));
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
        Arrays.stream(Objects.requireNonNull(output).split("\\n"))
                .filter(line -> line.contains(" are compressed"))
                .findFirst()
                .ifPresent(line -> {
                    String[] parts = line.split(" ");
                    setTotalCompressed(Long.parseLong(parts[indexOfCompressed].replace(".", "")));
                    setTotalDecompressed(Long.parseLong(parts[indexOfDecompressed].replace(".", "")));
                });
    }


    public boolean isValidDirectory(String path) {
        return path != null && !path.isEmpty() && new java.io.File(path).isDirectory();
    }

}
