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
    Text Figurenpeicher = null;
    StackPane TileSpeicher = null;
    public static String[][] brettStatus = new String[8][8]; //Dient der Logik im Hintergrund [Speichert Figuren-Position)
    int startRow = 0;
    int startCol = 0;
    int zielRow = 0;
    int zielCol = 0;
    String originalTileFarbe = null;

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane Board = new GridPane();


        // Schleife zum Erstellen des Schachbretts und der Startaufstellung

        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                StackPane tile = new StackPane();
                Text Figur = null;

                if ((row + col) % 2 == 0 ){
                    tile.setStyle("-fx-background-color: #F5F5F5");
                } else {
                    tile.setStyle("-fx-background-color: #708090");
                }

                if (row == 0 && col == 0 || row == 0 && col == 7){
                    Figur = new Text("♜");
                    brettStatus[row][col] = "bR";
                }else if (row == 0 && col == 1 || row == 0 && col == 6){
                    Figur = new Text("♞");
                    brettStatus[row][col] = "bN";
                }else if (row == 0 && col == 2 || row == 0 && col == 5){
                    Figur = new Text("♝");
                    brettStatus[row][col] = "bB";
                }else if (row == 0 && col == 3){
                    Figur = new Text("♛");
                    brettStatus[row][col] = "bQ";
                }else if (row == 0 && col == 4){
                    Figur = new Text("♚");
                    brettStatus[row][col] = "bK";
                }else if (row == 1) {
                    Figur = new Text("♟");
                    brettStatus[row][col] = "bP";
                }

                else if (row == 7 && col == 0 || row == 7 && col == 7){
                    Figur = new Text("♖");
                    brettStatus[row][col] = "wR";
                } else if (row == 7 && col == 1 || row == 7 && col == 6){
                    Figur = new Text("♘");
                    brettStatus[row][col] = "wN";
                }else if (row == 7 && col == 2 || row == 7 && col == 5){
                    Figur = new Text("♗");
                    brettStatus[row][col] = "wB";
                } else if (row == 7 && col == 3){
                    Figur = new Text("♕");
                    brettStatus[row][col] = "wQ";
                } else if (row == 7 && col == 4){
                    Figur = new Text("♔");
                    brettStatus[row][col] = "wK";
                }else if (row == 6){
                    Figur = new Text("♙");
                    brettStatus[row][col] = "wP";
                } else {
                    Figur = null;
                }
                if (Figur != null){
                    Figur.setStyle("-fx-font-size: 50px;");
                    tile.getChildren().add(Figur);
                }


                tile.setPrefSize(Tile_size, Tile_size);

                Board.add(tile, col, row);


                // Maus-Klick Event

                tile.setOnMouseClicked(e -> {
                    StackPane clicked =  (StackPane) e.getSource();

                    //Event zum auswählen der Figur, die bewegt werden soll
                    if (tile.getChildren().isEmpty() == false && Figurenpeicher == null){
                        Figurenpeicher = (Text) tile.getChildren().get(0);
                        TileSpeicher = tile;
                        originalTileFarbe = tile.getStyle().toString();
                        startCol = GridPane.getColumnIndex((clicked));
                        startRow = GridPane.getRowIndex((clicked));
                        tile.setStyle("-fx-background-color: #add8e6");

                    //Event zum bewegen einer bereits angeklickten Figur
                    } else if (Figurenpeicher != null) {

                        FigurenLogik logik = new FigurenLogik();
                        zielRow = GridPane.getRowIndex((clicked));
                        zielCol = GridPane.getColumnIndex((clicked));

                        if (logik.ZugErlaubnis(startRow, startCol, zielRow, zielCol)) {
                            TileSpeicher.getChildren().clear();

                            tile.getChildren().add(Figurenpeicher);

                            brettStatus[zielRow][zielCol] = brettStatus[startRow][startCol];
                            brettStatus[startRow][startCol] = null;

                            if (originalTileFarbe.equals("-fx-background-color: #F5F5F5")) {
                                TileSpeicher.setStyle("-fx-background-color: #F5F5F5");
                            } else {
                                TileSpeicher.setStyle("-fx-background-color: #708090");

                            }

                        } else {
                            if (originalTileFarbe.equals("-fx-background-color: #F5F5F5")) {
                                TileSpeicher.setStyle("-fx-background-color: #F5F5F5");
                            } else {
                                TileSpeicher.setStyle("-fx-background-color: #708090");

                            }
                        }
                        Figurenpeicher = null;
                        TileSpeicher = null;
                    }
                });
            }
        }


        //Erstellen der Szene (Brett)
        Scene scene = new Scene(Board, 8 * Tile_size, 8 * Tile_size);

        primaryStage.setTitle("Schachspiel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

}
