package techura;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;
import techura.utils.LoggedInEmployee;
import techura.utils.Navigator;
import techura.views.DashboardView;
import techura.views.HomeView;
import techura.views.LoginView;

import java.util.Stack;

public class Main extends Application {
   public static BorderPane rootPane;
   private static Stack<Pane> viewHistory = new Stack<>();

   public static Node getPrimaryStage() {
      return rootPane;
   }

   @Override
   public void start(Stage primaryStage) {
      // 🌟 Splash screen label
      Label animatedLabel = new Label("Techura by Bikram");
      animatedLabel.setFont(Font.font("Verdana", FontWeight.EXTRA_BOLD, 40));
      animatedLabel.setTextFill(Color.CYAN);
      animatedLabel.setEffect(new Glow(1));
      animatedLabel.setOpacity(0);

      StackPane splash = new StackPane(animatedLabel);
      splash.setStyle("-fx-background-color: #000000;");
      splash.setAlignment(Pos.CENTER);

      Scene scene = new Scene(splash, 1200, 800);
      primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/app_icon.png")));
      primaryStage.setTitle("Techura");
      primaryStage.setScene(scene);
      primaryStage.show();

      // Fade & slide animation
      FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), animatedLabel);
      fadeIn.setFromValue(0);
      fadeIn.setToValue(1);

      TranslateTransition slideUp = new TranslateTransition(Duration.seconds(2), animatedLabel);
      slideUp.setFromY(50);
      slideUp.setToY(0);

      ParallelTransition showSplash = new ParallelTransition(fadeIn, slideUp);

      showSplash.setOnFinished(event -> {
         loadMainUI(primaryStage);
      });

      showSplash.play();
   }

   private void loadMainUI(Stage stage) {
      rootPane = new BorderPane();

      // 🔙 Back Button
      Button backButton = new Button("← Back");
      backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
      backButton.setStyle("-fx-background-color: #444; -fx-text-fill: white;");
      backButton.setOnAction(e -> {
         if (!viewHistory.isEmpty()) {
            Pane previousView = viewHistory.pop();
            rootPane.setCenter(previousView);
         } else {
            // If no history, go to Home or Dashboard based on login
            if (LoggedInEmployee.isIslogin()) {
               Navigator.navigateTo(DashboardView.getView());
            } else {
               Navigator.navigateTo(HomeView.getView());
            }
         }
      });

      HBox topBar = new HBox(backButton);
      topBar.setAlignment(Pos.CENTER_LEFT);
      topBar.setPadding(new Insets(10));
      topBar.setStyle("-fx-background-color: #222222;");

      // Default view
      Pane homeView = HomeView.getView();
      viewHistory.push(homeView);
      rootPane.setCenter(homeView);
      rootPane.setTop(topBar);

      // 📜 Footer
      Label footer = new Label("© Techura by Bikram - All Rights Reserved");
      footer.setTextFill(Color.GRAY);
      footer.setFont(Font.font("Arial", 13));
      HBox footerBox = new HBox(footer);
      footerBox.setAlignment(Pos.CENTER);
      footerBox.setPadding(new Insets(10));
      footerBox.setStyle("-fx-background-color: #111111;");
      rootPane.setBottom(footerBox);

      // Animate main UI fade-in
      FadeTransition fade = new FadeTransition(Duration.seconds(1.2), rootPane);
      fade.setFromValue(0);
      fade.setToValue(1);
      fade.play();

      stage.getScene().setRoot(rootPane);
   }

   public static void main(String[] args) {
      launch(args);
   }
   public Scene getsence(){
      return rootPane.getScene();
   }
}