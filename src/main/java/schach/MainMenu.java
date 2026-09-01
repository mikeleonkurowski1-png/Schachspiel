package schach;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class MainMenu {
    private final Scene scene;

    public MainMenu(Schachbrett schachbrett) {

        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #2b2b2b;");

        Label placeholder = new Label("Hauptmenü");
        placeholder.setStyle("-fx-text-fill: white; -fx-font-size: 24px;");
        root.getChildren().add(placeholder);

        this.scene = new Scene(root, 800, 920);
    }

    public Scene getScene() {
        return scene;
    }
}
