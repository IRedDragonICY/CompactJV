package com.example.compactjv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Disk {
    private Size totalSpace;
    private Size freeSpace;
    private char label;

    public static Disk[] getDisks() {
        String output = CommandRunner.runCommand("wmic logicaldisk get name,size,freespace", true);
        String[] lines = Objects.requireNonNull(output).split("\n");

        ArrayList<Disk> diskList = new ArrayList<>();
        for (int i = 1; i < lines.length; i++) {
            String[] parts = lines[i].trim().split("\\s+");
            if (parts.length == 3) {
                try {
                    long freeSpace = Long.parseLong(parts[0]);
                    char label = parts[1].charAt(0);
                    long totalSpace = Long.parseLong(parts[2]);

                    Size freeSpaceSize = new Size(freeSpace, freeSpace);
                    Size totalSpaceSize = new Size(totalSpace, totalSpace);

                    diskList.add(new Disk(totalSpaceSize, freeSpaceSize, label));
                } catch (NumberFormatException e) {
                    System.out.println("Unable to parse disk information: " + Arrays.toString(parts));
                }
            }
        }
        Disk[] disks = new Disk[diskList.size()];
        diskList.toArray(disks);
        return disks;
    }

}
