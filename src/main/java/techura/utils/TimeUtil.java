package techura.utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TimeUtil {

    // Folder where CSV files are stored
    private static final String CSV_FOLDER = "data/salary/";

    public static double getTotalMinutesWorked(String id) {
        double totalMinutes = 0;

        try {
            // List of CSV files - assume you already have filenames (or only today's file)
            List<String> fileNames = List.of(
                    "salary_2025-07-23.csv", // You can also make this dynamic
                    "salary_2025-07-24.csv"
            );

            for (String fileName : fileNames) {
                BufferedReader reader = new BufferedReader(new FileReader(CSV_FOLDER + fileName));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split(",");

                    if (data.length >= 5 && data[0].trim().equalsIgnoreCase(id.trim())) {
                        String inTimeStr = data[3].trim();
                        String outTimeStr = data[4].trim();

                        try {
                            LocalTime inTime = LocalTime.parse(inTimeStr);
                            LocalTime outTime = LocalTime.parse(outTimeStr);
                            Duration duration = Duration.between(inTime, outTime);
                            totalMinutes += duration.toMinutes();
                        } catch (Exception e) {
                            System.err.println("Time parse error in file: " + fileName + " -> " + e.getMessage());
                        }
                    }
                }
                reader.close();
            }

        } catch (IOException e) {
            System.err.println("File read error: " + e.getMessage());
        }

        return totalMinutes;
    }
}