package com.example.compactjv.UI;

import com.example.compactjv.Controller;
import com.example.compactjv.ExecutorServiceManager;
import javafx.application.Platform;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;

public class DebuggingBoxUI extends Controller {
    private static final int MAX_TEXT_AREA_LENGTH = 5000;

    public DebuggingBoxUI() {
        super();
        debugTextArea.setFont(javafx.scene.text.Font.font("Consolas"));
        init(executorServiceManager);
    }

    private void init(ExecutorServiceManager executorServiceManager) {
        executorServiceManager.executeTask(() -> {
            OutputStream out = new ByteArrayOutputStream() {
                @Override
                public void write(byte[] b, int off, int len) {
                    appendToTextArea(new String(b, off, len));
                }
            };
            System.setOut(new PrintStream(out, true));
            System.setErr(new PrintStream(out, true));
        });
    }

    private void appendToTextArea(final String str) {
        Platform.runLater(() -> {
            appendText(str);
            debugTextArea.setScrollTop(Double.MAX_VALUE);
        });
    }

    private void appendText(String str) {
        String currentText = debugTextArea.getText();
        debugTextArea.setText(currentText.length() > MAX_TEXT_AREA_LENGTH ? currentText.substring(0, MAX_TEXT_AREA_LENGTH) : currentText);
        debugTextArea.appendText(str);
    }
}
