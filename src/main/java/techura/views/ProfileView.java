package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import techura.utils.LoggedInEmployee;

import java.io.File;

public class ProfileView {

    private static final Image defaultProfile = new Image(ProfileView.class.getResourceAsStream("/images/default_profile.png"));

    public static VBox getView() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(30));
        root.setAlignment(Pos.TOP_CENTER);

        // Load profile image
        ImageView profileImage = new ImageView();
        profileImage.setFitHeight(120);
        profileImage.setFitWidth(120);
        profileImage.setPreserveRatio(true);
        profileImage.setClip(new javafx.scene.shape.Circle(60, 60, 60));

        File savedFile = new File("user_profile.png");
        if (savedFile.exists()) {
            profileImage.setImage(new Image(savedFile.toURI().toString()));
        } else {
            profileImage.setImage(defaultProfile);
        }

        // Change photo button
        Button uploadBtn = new Button("📷 Change Photo");
        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Profile Photo");
            File file = chooser.showOpenDialog(null);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                profileImage.setImage(image);
                try {
                    java.nio.file.Files.copy(file.toPath(), new File("user_profile.png").toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        Label name = new Label("👤 " + LoggedInEmployee.getCurrent().getName());
        name.setStyle("-fx-font-size: 18px;");

        Label role = new Label("🧑‍💼 " + LoggedInEmployee.getCurrent().getRole());

        // Password fields (hidden by default)
        PasswordField newPass = new PasswordField();
        newPass.setPromptText("New Password");
        PasswordField confirmPass = new PasswordField();
        confirmPass.setPromptText("Confirm Password");

        VBox passwordBox = new VBox(10, newPass, confirmPass);
        passwordBox.setVisible(false);

        Button changePassBtn = new Button("🔒 Change Password");
        changePassBtn.setOnAction(e -> passwordBox.setVisible(!passwordBox.isVisible()));

        // Save password logic (for demonstration only)
        Button saveBtn = new Button("✅ Save Changes");
        saveBtn.setOnAction(e -> {
            if (newPass.getText().equals(confirmPass.getText()) && !newPass.getText().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Password changed successfully!");
                alert.show();
                newPass.clear();
                confirmPass.clear();
                passwordBox.setVisible(false);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Passwords do not match!");
                alert.show();
            }
        });

        root.getChildren().addAll(profileImage, uploadBtn, name, role, changePassBtn, passwordBox, saveBtn);
        return root;
    }
}