package schach;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class MainMenu {
    private final Scene scene;

    public MainMenu(Schachbrett schachbrett) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: gray;");

        Label placeholder = new Label("Hauptmenü");
        placeholder.setStyle("-fx-text-fill: white; -fx-font-size: 35px;");
        placeholder.setPadding(new Insets(40));
        BorderPane.setAlignment(placeholder, Pos.CENTER);
        root.setTop(placeholder);

        Button Spielen = new Button();
        Spielen.setText("Spiel starten!");
        Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        Spielen.setFont(new Font(30));
        Spielen.setPadding(new Insets(40));
        Spielen.setOnMouseEntered(event -> {
            Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Spielen.setOnMouseExited(event -> {
            Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Spielen.setOnAction(e -> {
            schachbrett.starteSchachbrett();
        });
        BorderPane.setAlignment(Spielen, Pos.CENTER);
        root.setBottom(Spielen);

        this.scene = new Scene(root, 1000, 800);
    }

    public Scene getScene() {
        return scene;
    }
}
