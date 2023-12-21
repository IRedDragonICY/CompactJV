package com.example.compactjv;

public class Disk {
    private double totalSpace;
    private double freeSpace;
    private char label;

    public double getTotalSpace() {
        return totalSpace;
    }

    public void setTotalSpace(double totalSpace) {
        this.totalSpace = totalSpace;
    }

    public double getFreeSpace() {
        return freeSpace;
    }

    public void setFreeSpace(double freeSpace) {
        this.freeSpace = freeSpace;
    }

    public char getLabel() {
        return label;
    }

    public void setLabel(char label) {
        this.label = label;
    }

    public Disk(double totalSpace, double freeSpace, char label) {
        this.totalSpace = totalSpace;
        this.freeSpace = freeSpace;
        this.label = label;
    }
    public static int countDisks() {
        String output = CommandRunner.runCommand("wmic logicaldisk get name", true);
        return output.split("\n").length - 1;
    }
    public static Disk[] getDisks() {
        String output = CommandRunner.runCommand("wmic logicaldisk get name, freespace, size", true);
        String[] lines = output.split("\n");
        Disk[] disks = new Disk[lines.length - 1];
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split("\\s+");
            disks[i - 1] = new Disk(Double.parseDouble(parts[2]), Double.parseDouble(parts[1]), parts[0].charAt(0));
        }
        return disks;
    }

}
