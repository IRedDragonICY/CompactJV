package com.example.compactjv;

import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Disk {
    private double totalSpace;
    private double freeSpace;
    private char label;

    public static int countDisks() {
        String output = CommandRunner.runCommand("wmic logicaldisk get name", true);
        return Objects.requireNonNull(output).split("\n").length - 1;
    }

    public static Disk[] getDisks() {
        String output = CommandRunner.runCommand("wmic logicaldisk get name, freespace, size", true);
        String[] lines = Objects.requireNonNull(output).split("\n");
        Disk[] disks = new Disk[lines.length - 1];
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].split("\\s+");
            disks[i - 1] = new Disk(Double.parseDouble(parts[2]), Double.parseDouble(parts[1]), parts[0].charAt(0));
        }
        return disks;
    }
}
