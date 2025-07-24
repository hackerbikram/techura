package techura.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;

public class Settings {

    public String softwareVersion = "1.0.0";
    public boolean notificationsEnabled = true;
    public String theme = "light"; // "light" or "dark"
    public boolean autoUpdate = false;
    public String language = "en"; // "en", "ja", "ne"

    // Save settings to file
    public void save(String filename) {
        try (Writer writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load settings from file or return default
    public static Settings load(String filename) {
        File file = new File(filename);
        if (!file.exists()) {
            return new Settings();
        }
        try (Reader reader = new FileReader(file)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Settings.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Settings();
        }
    }
}