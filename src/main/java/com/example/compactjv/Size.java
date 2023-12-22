package com.example.compactjv;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Size {
    private static final int SIZE_KB = 1024;
    private static final int SIZE_MB = SIZE_KB * 1024;
    private static final int SIZE_GB = SIZE_MB * 1024;

    @Getter
    private final long size;
    @Getter
    private final long sizeOnDisk;

    public String getSizeFormatted() {
        return formatSize(size);
    }

    public String getSizeOnDiskFormatted() {
        return formatSize(sizeOnDisk);
    }

    private String formatSize(long size) {
        if (size < SIZE_KB) {
            return size + " bytes";
        } else if (size < SIZE_MB) {
            return String.format("%.2f KB", (double) size / SIZE_KB);
        } else if (size < SIZE_GB) {
            return String.format("%.2f MB", (double) size / SIZE_MB);
        } else {
            return String.format("%.2f GB", (double) size / SIZE_GB);
        }
    }
}
