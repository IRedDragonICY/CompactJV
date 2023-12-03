package com.example.compactjv;

public class Compressor {
    private static final String START_COMMAND = "start /B cmd.exe /c compact";
    private CommandRunner commandRunner = new CommandRunner();

    public void compress(String filePath, String algorithm) {
        commandRunner.runCommand(START_COMMAND + " /A /C /I /S /F /EXE:" + algorithm + " /s:\"" + filePath + "\"");
    }

    public void decompress(String filePath) {
        commandRunner.runCommand(START_COMMAND + " /U /I /A /F /s:\"" + filePath + "\"");
    }
}
