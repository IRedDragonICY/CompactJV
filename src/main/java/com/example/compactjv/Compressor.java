package com.example.compactjv;

public class Compressor {
    private static final String START_COMMAND = "start /B cmd.exe /c compact";
    public void compress(String filePath, String algorithm) {
        CommandRunner.runCommand(START_COMMAND + " /A /C /I /S /F /EXE:" + algorithm + " /s:\"" + filePath + "\"", false);
    }

    public void decompress(String filePath) {
        CommandRunner.runCommand(START_COMMAND + " /U /I /A /F /s:\"" + filePath + "\"", false);
    }
}
