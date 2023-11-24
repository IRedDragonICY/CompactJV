package com.example.compactjv;

public class Size {
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
}
