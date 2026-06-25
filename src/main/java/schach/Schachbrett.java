package schach;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public class Schachbrett extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private static final int Tile_size = 80;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane Board = new GridPane();

        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                StackPane tile = new StackPane();

                if ((row + col) % 2 == 0 ){
                    tile.setStyle("-fx-background-color: #F5F5F5");
                } else {
                    tile.setStyle("-fx-background-color: #708090");
                }
                tile.setPrefSize(Tile_size, Tile_size);

                Board.add(tile, col, row);
            }
        }

        Scene scene = new Scene(Board, 8 * Tile_size, 8 * Tile_size);

        primaryStage.setTitle("Schachspiel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
