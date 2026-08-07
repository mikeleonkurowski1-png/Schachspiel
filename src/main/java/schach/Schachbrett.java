package schach;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;


public class Schachbrett extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private GridPane Board;
    private VBox oben;
    private BorderPane unten;
    private Historie historie = new Historie();
    private static final int Tile_size = 80;
    Text Figurenspeicher = null;
    StackPane TileSpeicher = null;
    public static String[][] brettStatus = new String[8][8]; //Dient der Logik im Hintergrund [Speichert Figuren-Position)
    int startRow = 0;
    int startCol = 0;
    int zielRow = 0;
    int zielCol = 0;
    String originalTileFarbe = null;
    public static boolean weißamZug = true;
    public static int WKönigRow = 7;
    public static int WKönigCol = 4;
    public static int BKönigRow = 0;
    public static int BKönigCol = 4;

    public static int enPassantRow = -1;
    public static int enPassantCol = -1;

    //Dropshadow (Underglow fürs Brett) um anzuzeigen, welcher Spieler am Zug ist
        DropShadow dropShadow = new DropShadow();

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Strukturgerüst für das GUI
        GridPane Board = new GridPane();
        Board.setAlignment(Pos.CENTER);

        VBox oben = new VBox();
        oben.setPrefHeight(100);
        oben.setMinHeight(100);
        oben.setAlignment(Pos.CENTER);

        BorderPane unten = new BorderPane();
        unten.setPrefHeight(100);
        unten.setMinHeight(100);
        unten.setMaxWidth(640);
        BorderPane.setAlignment(unten,  Pos.CENTER);

        StackPane mitte = new StackPane(Board);
        mitte.setAlignment(Pos.CENTER);

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: gray;");
        root.setTop(oben);
        root.setCenter(mitte);
        root.setBottom(unten);

        Scene scene = new Scene(root, 800, 920);

        Label willkommen = new Label("Willkommen! Weiß startet das Spiel. \uD83D\uDE01");
        final double Schriftgröße = 30.0;
        willkommen.setFont(new Font(Schriftgröße));
        oben.getChildren().add(willkommen);


        Button vor = new Button();
        vor.setText("->");
        vor.autosize();
        vor.setStyle("-fx-background-color: dark-gray;");
        vor.setStyle("-fx-text-fill: light-gray;");
        vor.setOnMouseClicked(event -> {
            String[][] neuerZustand = historie.redo();
            if (neuerZustand != null) {
                for (int i = 0; i < 8; i++) {
                    System.arraycopy(neuerZustand[i], 0, brettStatus[i], 0, 8);
                }
                brettNeuZeichnen(Board);
                weißamZug = !weißamZug;
                vor.setDisable(true);
            }
        });
        unten.setRight(vor);

        Button zurück = new Button();
        zurück.setText("<-");
        zurück.autosize();
        zurück.setStyle("-fx-background-color: dark-gray;");
        zurück.setStyle("-fx-text-fill: light-gray;");
        zurück.setOnMouseClicked(event -> {
            String[][] alterZustand = historie.undo();
            if (alterZustand != null) {
                for (int i = 0; i < 8; i++) {
                    System.arraycopy(alterZustand[i], 0, brettStatus[i], 0, 8);
                }
                brettNeuZeichnen(Board);
                vor.setDisable(false);
                zurück.setDisable(true);
                weißamZug = !weißamZug;
            }
        });
        unten.setLeft(zurück);

        vor.setDisable(true);
        zurück.setDisable(true);



        // Schleife zum Erstellen des Schachbretts und der Startaufstellung


        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                StackPane tile = new StackPane();
                Text Figur = null;

                if ((row + col) % 2 == 0) {
                    tile.setStyle("-fx-background-color: #F5F5F5");
                } else {
                    tile.setStyle("-fx-background-color: #708090");
                }

                if (row == 0 && col == 0 || row == 0 && col == 7) {
                    Figur = new Text("♜");
                    brettStatus[row][col] = "bR";
                } else if (row == 0 && col == 1 || row == 0 && col == 6) {
                    Figur = new Text("♞");
                    brettStatus[row][col] = "bN";
                } else if (row == 0 && col == 2 || row == 0 && col == 5) {
                    Figur = new Text("♝");
                    brettStatus[row][col] = "bB";
                } else if (row == 0 && col == 3) {
                    Figur = new Text("♛");
                    brettStatus[row][col] = "bQ";
                } else if (row == 0 && col == 4) {
                    Figur = new Text("♚");
                    brettStatus[row][col] = "bK";
                } else if (row == 1) {
                    Figur = new Text("♟");
                    brettStatus[row][col] = "bP";
                } else if (row == 7 && col == 0 || row == 7 && col == 7) {
                    Figur = new Text("♖");
                    brettStatus[row][col] = "wR";
                } else if (row == 7 && col == 1 || row == 7 && col == 6) {
                    Figur = new Text("♘");
                    brettStatus[row][col] = "wN";
                } else if (row == 7 && col == 2 || row == 7 && col == 5) {
                    Figur = new Text("♗");
                    brettStatus[row][col] = "wB";
                } else if (row == 7 && col == 3) {
                    Figur = new Text("♕");
                    brettStatus[row][col] = "wQ";
                } else if (row == 7 && col == 4) {
                    Figur = new Text("♔");
                    brettStatus[row][col] = "wK";
                } else if (row == 6) {
                    Figur = new Text("♙");
                    brettStatus[row][col] = "wP";
                } else {
                    Figur = null;
                }
                if (Figur != null) {
                    Figur.setStyle("-fx-font-size: 50px;");
                    tile.getChildren().add(Figur);
                }


                tile.setPrefSize(Tile_size, Tile_size);

                Board.add(tile, col, row);



                // Maus-Klick Event

                tile.setOnMouseClicked(e -> {


                    StackPane clicked = (StackPane) e.getSource();
                    int aktuelleRow = GridPane.getRowIndex(tile);
                    int aktuelleCol = GridPane.getColumnIndex(tile);

                    //Event zum auswählen der Figur, die bewegt werden soll
                    if (tile.getChildren().isEmpty() == false && Figurenspeicher == null) {

                        String FigurName = brettStatus[aktuelleRow][aktuelleCol];


                        //Dieser Block stellt sicher, dass man nur eine Figur der Farbe auswählen kann, die dran ist
                        if (FigurName != null) {
                            if ((weißamZug == true && FigurName.startsWith("b")) || (weißamZug == false && FigurName.startsWith("w"))) {
                                return;
                            }
                        }
                        Figurenspeicher = (Text) tile.getChildren().get(0);
                        TileSpeicher = tile;
                        originalTileFarbe = tile.getStyle().toString();
                        startCol = GridPane.getColumnIndex((clicked));
                        startRow = GridPane.getRowIndex((clicked));
                        tile.setStyle("-fx-background-color: #add8e6");
                        vor.setDisable(true);



                        //Event zum bewegen einer bereits angeklickten Figur
                    } else if (Figurenspeicher != null) {

                        oben.getChildren().remove(willkommen);

                        FigurenLogik logik = new FigurenLogik();
                        zielRow = GridPane.getRowIndex((clicked));
                        zielCol = GridPane.getColumnIndex((clicked));

                        boolean Schach = false;

                        if (logik.ZugErlaubnis(startRow, startCol, zielRow, zielCol)) { //Überprüft, ob der Zug legal ist

                            historie.getZurückState(brettStatus);

                            String geschlageneEnPassantFigur = null;

                            boolean enPassant = (brettStatus[startRow][startCol].equals("wP") || brettStatus[startRow][startCol].equals("bP")) && Math.abs(zielCol - startCol) == 1 && brettStatus[zielRow][zielCol] == null && enPassantRow == startRow && enPassantCol == zielCol;


                            //Dieser ganze Block überprüft, ob man nach seinem Zug (immer noch) im Schach steht, indem er den Zug ausführt und falls ja wieder zurücksetzt.
                            String alteZielFigur = brettStatus[zielRow][zielCol];
                            brettStatus[zielRow][zielCol] = brettStatus[startRow][startCol];
                            brettStatus[startRow][startCol] = null;

                            if (enPassant) {
                                geschlageneEnPassantFigur = brettStatus[startRow][zielCol];
                                brettStatus[startRow][zielCol] = null;
                            }

                            int altWKönigRow = WKönigRow;
                            int altWKönigCol = WKönigCol;
                            int altBKönigRow = BKönigRow;
                            int altBKönigCol = BKönigCol;

                            if (brettStatus[zielRow][zielCol].equals("bK")) {
                                BKönigRow = zielRow;
                                BKönigCol = zielCol;
                            } else if (brettStatus[zielRow][zielCol].equals("wK")) {
                                WKönigRow = zielRow;
                                WKönigCol = zielCol;
                            }
                            Schacherkennung erkennung = new Schacherkennung();

                            weißamZug = !weißamZug;
                            boolean inSchachZiehen = erkennung.StehtimSchach();
                            weißamZug = !weißamZug;

                            if (inSchachZiehen) {
                                brettStatus[startRow][startCol] = brettStatus[zielRow][zielCol];
                                brettStatus[zielRow][zielCol] = alteZielFigur;
                                WKönigRow = altWKönigRow;
                                WKönigCol = altWKönigCol;
                                BKönigCol = altBKönigCol;
                                BKönigRow = altBKönigRow;

                                if (enPassant) {
                                    brettStatus[startRow][zielCol] = geschlageneEnPassantFigur;
                                }

                                TileSpeicher.setStyle(originalTileFarbe);

                                TileSpeicher = null;
                                Figurenspeicher = null;
                                return;
                            }

                            String gezogeneFigur = brettStatus[zielRow][zielCol];

                            enPassantRow = -1;
                            enPassantCol = -1;

                            if ((gezogeneFigur.equals("wP") || gezogeneFigur.equals("bP")) && Math.abs(zielRow - startRow) == 2) {

                                enPassantRow = zielRow;
                                enPassantCol = zielCol;
                            }

                            if (gezogeneFigur.equals("wK")) {
                                FigurenLogik.WKbewegt = true;
                            }

                            if (gezogeneFigur.equals("bK")) {
                                FigurenLogik.BKbewegt = true;
                            }

                            if (gezogeneFigur.equals("wR")) {
                                FigurenLogik.WRbewegt = true;
                            }

                            if (gezogeneFigur.equals("bR")) {
                                FigurenLogik.BRbewegt = true;
                            }


                            //Ab hier wird der Zug ausgeführt, falls er legal ist
                            TileSpeicher.getChildren().clear();
                            tile.getChildren().clear();
                            tile.getChildren().add(Figurenspeicher);


                            if (enPassant) {
                                for (Node node : Board.getChildren()) {
                                    if (GridPane.getRowIndex(node) == startRow
                                            && GridPane.getColumnIndex(node) == zielCol) {

                                        ((StackPane) node).getChildren().clear();
                                    }
                                }
                            }

                            //Automatisches hinterherziehen des Turms beim kurzen rochieren
                            if ((gezogeneFigur.equals("wK") || gezogeneFigur.equals("bK")) && zielCol == startCol + 2) {

                                StackPane TurmStart = null;
                                StackPane TurmZiel = null;

                                for (javafx.scene.Node node : Board.getChildren()) {
                                    int row2 = GridPane.getRowIndex(node);
                                    int col2 = GridPane.getColumnIndex(node);

                                    if (row2 == startRow && col2 == 7) {
                                        TurmStart = (StackPane) node;
                                    }

                                    if (row2 == startRow && col2 == 5) {
                                        TurmZiel = (StackPane) node;
                                    }
                                }

                                Text Turm = (Text) TurmStart.getChildren().get(0);
                                TurmStart.getChildren().clear();
                                TurmZiel.getChildren().add(Turm);

                                brettStatus[startRow][5] = brettStatus[startRow][7];
                                brettStatus[startRow][7] = null;
                            }

                            //Automatisches hinterherziehen des Turms beim langen rochieren
                            if ((gezogeneFigur.equals("wK") || gezogeneFigur.equals("bK")) && zielCol == startCol - 2) {

                                StackPane TurmStart = null;
                                StackPane TurmZiel = null;

                                for (javafx.scene.Node node : Board.getChildren()) {
                                    int row2 = GridPane.getRowIndex(node);
                                    int col2 = GridPane.getColumnIndex(node);

                                    if (row2 == startRow && col2 == 0) {
                                        TurmStart = (StackPane) node;
                                    }

                                    if (row2 == startRow && col2 == 3) {
                                        TurmZiel = (StackPane) node;
                                    }
                                }

                                Text Turm = (Text) TurmStart.getChildren().get(0);
                                TurmStart.getChildren().clear();
                                TurmZiel.getChildren().add(Turm);

                                brettStatus[startRow][3] = brettStatus[startRow][0];
                                brettStatus[startRow][0] = null;
                            }

                            //Bauern-Promotion der weißen Bauern
                            if (gezogeneFigur.equals("wP") && zielRow == 0) {

                                Board.setDisable(true);

                                Label promotion = new Label("Zu welcher Figur soll dein Bauer promoten?");
                                promotion.setFont(new Font(Schriftgröße));
                                oben.getChildren().add(promotion);

                                HBox promotionsButtons = new HBox(
                                        erstellePromotionsButton("wQ", "♕", zielRow, zielCol, Board, oben, unten),
                                        erstellePromotionsButton("wR", "♖", zielRow, zielCol, Board, oben, unten),
                                        erstellePromotionsButton("wB", "♗", zielRow, zielCol, Board, oben, unten),
                                        erstellePromotionsButton("wN", "♘", zielRow, zielCol, Board, oben, unten)
                                );
                                promotionsButtons.setAlignment(Pos.CENTER);

                                unten.setCenter(promotionsButtons);

                            }
                            //Promotion für schwarze Bauern
                            if (gezogeneFigur.equals("bP") && zielRow == 7) {

                                Board.setDisable(true);
                                Label promotion = new  Label("Zu welcher Figur soll dein Bauer promoten?");
                                promotion.setFont(new Font(Schriftgröße));
                                oben.getChildren().add(promotion);

                                HBox promotionsbuttons = new HBox(erstellePromotionsButton("bQ", "♛", zielRow, zielCol, Board, oben, unten),
                                                                    erstellePromotionsButton("bR", "♜", zielRow, zielCol, Board, oben, unten),
                                                                    erstellePromotionsButton("bB", "♝", zielRow, zielCol, Board, oben, unten),
                                                                    erstellePromotionsButton("bN", "♞", zielRow, zielCol, Board, oben, unten));
                                promotionsbuttons.setAlignment(Pos.CENTER);
                                unten.setCenter(promotionsbuttons);

                            }
                            historie.getVorState(brettStatus);
                            zurück.setDisable(false);
                            vor.setDisable(true);

                            if ((startRow + startCol) % 2 == 0) { //Zurückfärben des angeklickten Felds
                                TileSpeicher.setStyle("-fx-background-color: #F5F5F5");
                            } else {
                                TileSpeicher.setStyle("-fx-background-color: #708090");
                            }
                            if (brettStatus[zielRow][zielCol].equals("wK")) { //Tracked die Königsposition
                                WKönigCol = zielCol;
                                WKönigRow = zielRow;
                            }
                            if (brettStatus[zielRow][zielCol].equals("bK")) { //Tracked die Königsposition
                                BKönigCol = zielCol;
                                BKönigRow = zielRow;
                            }

                            Schacherkennung erkennung2 = new Schacherkennung();
                            Schach = erkennung2.StehtimSchach();

                            if (Schach) { //Falls einer der Könige im Schach steht überprüft das Programm hier welcher und färbt dessen Tile rot
                                if (weißamZug == true) {
                                    for (javafx.scene.Node node : Board.getChildren()) {

                                        int rowcheck = GridPane.getRowIndex(node);
                                        int colcheck = GridPane.getColumnIndex(node);

                                        if (rowcheck == BKönigRow && colcheck == BKönigCol) {

                                            StackPane BKönigsTile = (StackPane) node;

                                            BKönigsTile.setStyle("-fx-background-color: #FF0000;");
                                            dropShadow.setColor(Color.RED);


                                            break;
                                        }
                                    }
                                } else if (weißamZug != true) {
                                    for (javafx.scene.Node node : Board.getChildren()) {

                                        int rowcheck = GridPane.getRowIndex(node);
                                        int colcheck = GridPane.getColumnIndex(node);

                                        if (rowcheck == WKönigRow && colcheck == WKönigCol) {

                                            StackPane WKönigsTile = (StackPane) node;

                                            WKönigsTile.setStyle("-fx-background-color: #FF0000;");
                                            dropShadow.setColor(Color.RED);

                                            break;
                                        }
                                    }
                                }
                            } else { //Steht kein König nicht (mehr) im Schach werden die Felder wieder zurückgefärbt
                                for (javafx.scene.Node node : Board.getChildren()) {
                                    int rowcheck = GridPane.getRowIndex(node);
                                    int colcheck = GridPane.getColumnIndex(node);

                                    if ((rowcheck == WKönigRow && colcheck == WKönigCol) || (rowcheck == BKönigRow && colcheck == BKönigCol)) {
                                        StackPane königsTile = (StackPane) node;
                                        if ((rowcheck + colcheck) % 2 == 0 && !Schach) {
                                            königsTile.setStyle("-fx-background-color: #F5F5F5");
                                        } else if ((rowcheck + colcheck) % 2 != 0 && !Schach) {
                                            königsTile.setStyle("-fx-background-color: #708090");
                                        }
                                    }
                                }
                            }
                            weißamZug = !weißamZug; //Spielerwechsel

                            Schacherkennung erkennung3 = new Schacherkennung();
                            if (erkennung3.hatlegaleZügen() == false) {

                                if (Schach) {

                                    //Erkennung von Schachmatt, so wie Siegeranzeige im GUI und reset button
                                    boolean schwarzHatGewonnen = weißamZug;
                                    String Sieger = schwarzHatGewonnen ? "Schwarz" : "Weiß";

                                    Label Siegerlabel = new Label("Schachmatt! Der Sieger ist: " + Sieger);
                                    Siegerlabel.setFont(new Font(Schriftgröße));

                                    if (schwarzHatGewonnen) {
                                        root.setStyle("-fx-background-color: black;");
                                        Siegerlabel.setStyle("-fx-text-fill: white;");
                                    } else {
                                        root.setStyle("-fx-background-color: white;");
                                        Siegerlabel.setStyle("-fx-text-fill: black;");
                                    }
                                    oben.getChildren().add(Siegerlabel);

                                    Button MattresetButton = new Button("Reset");
                                    MattresetButton.setStyle("-fx-background-color: dark-gray;");
                                    MattresetButton.setText("Reset");
                                    MattresetButton.setStyle("-fx-text-fill: light-gray;");
                                    MattresetButton.setPrefSize(200, 35);
                                    MattresetButton.setFont(new Font(30));
                                    unten.setCenter(MattresetButton);
                                    MattresetButton.setOnMouseClicked(event -> {
                                        appNeustart(primaryStage);
                                    });

                                } else {

                                    //Patt anzeige und reset button
                                    System.out.println("Spiel ist vorbei...PATT!!");
                                    Label PattLabel = new Label("Patt! (Unentschieden)");
                                    PattLabel.setFont(new Font(30));
                                    oben.getChildren().add(PattLabel);

                                    Button PattresetButton = new Button("Reset");
                                    PattresetButton.setStyle("-fx-background-color: dark-gray;");
                                    PattresetButton.setText("Reset");
                                    PattresetButton.setStyle("-fx-text-fill: light-gray;");
                                    PattresetButton.setPrefSize(200, 35);
                                    PattresetButton.setFont(new Font(30));
                                    unten.setCenter(PattresetButton);
                                    PattresetButton.setOnMouseClicked(event -> {
                                        appNeustart(primaryStage);
                                    });

                                }
                                Board.setDisable(true);
                                return;
                            }


                            if (!Schach) {
                              DropShadow Anzeige = (DropShadow) Board.getEffect(); //Dropshadow ändern, je nachdem wer am Zug ist
                                if (Anzeige != null) {
                                    if (weißamZug == true) {
                                        Anzeige.setColor(Color.WHITE);
                                    } else {
                                        Anzeige.setColor(Color.BLACK);
                                    }
                                }
                            }

                        } else {
                            TileSpeicher.setStyle(originalTileFarbe);
                        }
                        Figurenspeicher = null;
                        TileSpeicher = null;
                    }
                });
            }
        }

        //Erstellen der Szene (Brett)
        scene.setFill(Color.GRAY);
        dropShadow.setRadius(30);
        dropShadow.setSpread(0.6);
        dropShadow.setColor(Color.WHITE);


        Board.setEffect(dropShadow);
        Board.setPadding(new Insets(40));
        primaryStage.setTitle("Schachspiel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(920);
        primaryStage.show();
    }
    //Kleine Hilfsmethode zum Neustarten des Spiels
    public void appNeustart(Stage primaryStage) {
        brettStatus = new String[8][8];
        weißamZug = true;

        FigurenLogik.WKbewegt = false;
        FigurenLogik.BKbewegt = false;
        FigurenLogik.WRbewegt = false;
        FigurenLogik.BRbewegt = false;

        WKönigCol = 4;
        WKönigRow = 7;
        BKönigRow = 0;
        BKönigCol = 4;

        enPassantRow = -1;
        enPassantCol = -1;

        Figurenspeicher = null;
        TileSpeicher = null;
        originalTileFarbe = null;

        try{
           start(primaryStage);

        } catch(Exception e){
            e.printStackTrace();
        }
    }

    //Hilfsmethode zum aktualisieren des Bretts anhand einer Kopie in Form eines 2d-Arrays
    public void brettNeuZeichnen(GridPane board){
        for (Node node :  board.getChildren()) {
            if (node instanceof StackPane) {
                StackPane tile =  (StackPane) node;
                Integer row = GridPane.getRowIndex(node);
                Integer col = GridPane.getColumnIndex(node);

                if (row != null && col != null) {
                    tile.getChildren().clear();

                    String figurCode = brettStatus[row][col];
                    if ("wK".equals(figurCode)) { WKönigRow = row; WKönigCol = col; }
                    if ("bK".equals(figurCode)) { BKönigRow = row; BKönigCol = col; }
                    if (figurCode != null) {
                        String unicodeZeichen = getUnicodeZeichen(figurCode);
                        if (unicodeZeichen != null) {
                            Text figurtext = new  Text(unicodeZeichen);
                            figurtext.setStyle("-fx-font-size: 50px;");
                            tile.getChildren().add(figurtext);
                        }
                    }
                }

            }
        }
    }
    //Hilfsmethode zum Konvertieren der Texte
    public String getUnicodeZeichen(String figurCode) {
        switch (figurCode) {
            case "wK": return "♔";
            case "wQ": return "♕";
            case "wR": return "♖";
            case "wB": return "♗";
            case "wN": return "♘";
            case "wP": return "♙";
            case "bK": return "♚";
            case "bQ": return "♛";
            case "bR": return "♜";
            case "bB": return "♝";
            case "bN": return "♞";
            case "bP": return "♟";
            default: return null;
        }
    }

    //Hilfsmethode zur erstellung der Promotionsbuttons
    private Button erstellePromotionsButton(String figurCode, String symbol, int zielRow, int zielCol, GridPane board, Pane oben, BorderPane unten) {
        Button btn = new Button(symbol);

        // Dein exaktes Styling:
        btn.setStyle("-fx-background-color: dark-gray; -fx-text-fill: light-gray;");
        btn.setPrefSize(75, 75);
        btn.setFont(new Font(30));

        btn.setOnAction(e -> {
            // 1. Logik-Array aktualisieren
            brettStatus[zielRow][zielCol] = figurCode;

            // 2. UI zurücksetzen & Brett neu zeichnen (ersetzt das fehleranfällige StackPane-Casting)
            brettNeuZeichnen(board);
            oben.getChildren().clear();
            unten.setCenter(null);
            board.setDisable(false);

            // 3. Historie für Redo sichern
            historie.getVorState(brettStatus);
        });

        return btn;
    }
}
