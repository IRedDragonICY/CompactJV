package com.example.compactjv;

public class Size extends File {
    private static final int SIZE_KB = 1024;
    private static final int SIZE_MB = SIZE_KB * 1024;
    private static final int SIZE_GB = SIZE_MB * 1024;

    private long size;
    private long sizeOnDisk;

    public Size(long size, long sizeOnDisk) {
        this.size = size;
        this.sizeOnDisk = sizeOnDisk;
    }

    public long getSize() {
        return size;
    }

    public long getSizeOnDisk() {
        return sizeOnDisk;
    }

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
