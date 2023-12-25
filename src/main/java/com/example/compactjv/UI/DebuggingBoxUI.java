package com.example.compactjv.UI;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

public class DebuggingBoxUI {
    final TextArea debugTextArea;

    public DebuggingBoxUI(TextArea debugTextArea) {
        this.debugTextArea = debugTextArea;
        init();
    }

    public void init() {
        PrintStream out = new PrintStream(new OutputStream() {
            @Override
            public void write(int b) throws IOException {
                appendToTextArea(String.valueOf((char) b));
            }
        });

        System.setOut(out);
        System.setErr(out);
    }

    public void appendToTextArea(final String str) {
        Platform.runLater(() -> debugTextArea.appendText(str));
    }
}
