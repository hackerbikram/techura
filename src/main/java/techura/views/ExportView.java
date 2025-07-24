package techura.views;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import techura.utils.Navigator;

public class ExportView {
    public static VBox getView() {
        Button back = new Button("🔙back");
        back.setOnAction(e-> Navigator.navigateTo(DashboardView.getView()));

        Label title = new Label("Export / Backup");

        Button exportJSON = new Button("Export as JSON");
        Button exportCSV = new Button("Export as CSV");

        VBox layout = new VBox(10,back, title, exportJSON, exportCSV);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.CENTER);
        return layout;
    }
}