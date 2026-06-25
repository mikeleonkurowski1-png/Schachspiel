package schach;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.text.Text;
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

                Text Figur = null;

                if (row == 0 && col == 0 || row == 0 && col == 7){
                    Figur = new Text("♜");
                }else if (row == 0 && col == 1 || row == 0 && col == 6){
                    Figur = new Text("♞");
                }else if (row == 0 && col == 2 || row == 0 && col == 5){
                    Figur = new Text("♝");
                }else if (row == 0 && col == 3){
                    Figur = new Text("♛");
                }else if (row == 0 && col == 4){
                    Figur = new Text("♚");
                }else if (row == 1) {
                    Figur = new Text("♟");
                }

                else if (row == 7 && col == 0 || row == 7 && col == 7){
                    Figur = new Text("♖");
                } else if (row == 7 && col == 1 || row == 7 && col == 6){
                    Figur = new Text("♘");
                }else if (row == 7 && col == 2 || row == 7 && col == 5){
                    Figur = new Text("♗");
                } else if (row == 7 && col == 3){
                    Figur = new Text("♕");
                } else if (row == 7 && col == 4){
                    Figur = new Text("♔");
                }else if (row == 6){
                    Figur = new Text("♙");
                }
                if (Figur != null){
                    Figur.setStyle("-fx-font-size: 50px;");
                    tile.getChildren().add(Figur);
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
