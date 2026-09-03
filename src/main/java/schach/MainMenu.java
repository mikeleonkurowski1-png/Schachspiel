package schach;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;

public class MainMenu {
    private final Scene scene;
    private boolean UhrAn = true;
    private boolean undoan = true;
    private boolean Botaus = true;
    private boolean flipan = true;

    public MainMenu(Schachbrett schachbrett) {

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: gray;");

        Label Hauptmenue = new Label("Hauptmenü");
        Hauptmenue.setStyle("-fx-text-fill: white; -fx-font-size: 35px;");
        Hauptmenue.setUnderline(true);
        Hauptmenue.setPadding(new Insets(40));
        BorderPane.setAlignment(Hauptmenue, Pos.CENTER);
        root.setTop(Hauptmenue);

        Button Spielen = new Button();
        Spielen.setText("Spiel starten!");
        Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        Spielen.setFont(new Font(30));
        Spielen.setPadding(new Insets(10, 25, 10, 25));
        Spielen.setOnMouseEntered(event -> {
            Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Spielen.setOnMouseExited(event -> {
            Spielen.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Spielen.setOnAction(e -> {
            schachbrett.starteSchachbrett(UhrAn, undoan, Botaus, flipan);
        });
        BorderPane.setMargin(Spielen, new Insets(0, 0, 40, 0));
        BorderPane.setAlignment(Spielen, Pos.CENTER);
        root.setBottom(Spielen);

        HBox mitte = new HBox();
        mitte.setSpacing(10);
        mitte.setPadding(new Insets(30));
        mitte.setAlignment(Pos.CENTER);
        root.setCenter(mitte);

        Button Uhr = new Button();
        Uhr.setText("Chess clock on! (10 min)");
        Uhr.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        Uhr.setOnMouseEntered(event -> {
            Uhr.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Uhr.setOnMouseExited(event -> {
            Uhr.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Uhr.setOnMouseClicked(event -> {
            if (Uhr.getText().equals("Chess clock on! (10 min)")) {
                Uhr.setText("Chess clock off!");
                UhrAn = false;
            }
            else  {
                Uhr.setText("Chess clock on! (10 min)");
                UhrAn = true;
            }
        });
        mitte.getChildren().add(Uhr);

        Button undoredo = new Button();
        undoredo.setText("Undo/Redo active!");
        undoredo.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        undoredo.setOnMouseEntered(event -> {
            undoredo.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        undoredo.setOnMouseExited(event -> {
            undoredo.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        undoredo.setOnMouseClicked(event -> {
            if (undoan) {
                undoan = false;
                undoredo.setText("Undo/Redo deactivated!");
            }
            else  {
                undoan = true;
                undoredo.setText("Undo/Redo active!");
            }
        });
        mitte.getChildren().add(undoredo);

        Button Bot = new Button();
        Bot.setText("ChessBot deactivated!");
        Bot.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        Bot.setOnMouseEntered(event -> {
            Bot.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Bot.setOnMouseExited(event -> {
            Bot.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        Bot.setOnMouseClicked(event -> {
            if (Botaus) {
                Botaus = false;
                Bot.setText("Chessbot activated!");
            }
            else   {
                Botaus = true;
                Bot.setText("Chessbot deactivated!");
            }
        });
        mitte.getChildren().add(Bot);

        Button flip = new Button();
        flip.setText("Flip activated!");
        flip.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        flip.setOnMouseEntered(event -> {
            flip.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        flip.setOnMouseExited(event -> {
            flip.setStyle("-fx-background-color: gray; -fx-text-fill: white; -fx-border-color: white; -fx-border-width: 1.5px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-focus-color: transparent; -fx-faint-focus-color: transparent;");
        });
        flip.setOnMouseClicked(event -> {
            if (flipan) {
                flipan = false;
                flip.setText("Flip deactivated!");
            }
            else   {
                flipan = true;
                flip.setText("Flip activated!");
            }
        });
        mitte.getChildren().add(flip);



        this.scene = new Scene(root, 1000, 800);
    }

    public Scene getScene() {
        return scene;
    }
}
