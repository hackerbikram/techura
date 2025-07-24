package techura.controlls;

import javafx.scene.Scene;
import techura.Main;

public class ApplyTheme {

    public static void applyTheme(String theme) {
        Scene scene = Main.getPrimaryStage().getScene(); // Get the current scene from Main

        if (scene == null) {
            System.err.println("Scene is null. Theme not applied.");
            return;
        }

        // Clear previous styles
        scene.getStylesheets().clear();

        // Apply the correct theme
        String stylesheetPath = theme.equalsIgnoreCase("dark")
                ? "/css/dark.css"
                : "/css/light.css";

        scene.getStylesheets().add(Main.class.getResource(stylesheetPath).toExternalForm());
    }
}