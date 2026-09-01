package schach;

import javafx.animation.KeyFrame;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.animation.Timeline;
import javafx.util.Duration;
import javafx.geometry.Orientation;
import javafx.scene.layout.FlowPane;

import java.util.ArrayList;
import java.util.List;


public class Schachbrett extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    private Stage primaryStage;

    int WeißZeit = 600;
    int SchwarzZeit = 600;
    private final List<String> redoCache = new ArrayList<>();
    private VBox oben;
    private BorderPane unten;
    private Historie historie = new Historie();
    private static final int Tile_size = 80;
    Text Figurenspeicher = null;
    StackPane TileSpeicher = null;
    public static String[][] brettStatus = new String[8][8]; //Dient der Logik im Hintergrund [Speichert Figuren-Position)
    int startRow = 0, startCol = 0, zielRow = 0, zielCol = 0;
    String originalTileFarbe = null;
    public static boolean weißamZug = true;
    public static int WKönigRow = 7, WKönigCol = 4, BKönigRow = 0,  BKönigCol = 4;
    public static int enPassantRow = -1,  enPassantCol = -1;
    private Timeline timeline;
    private final List<String> geschlagenUndoCache = new ArrayList<>();
    private final List<String> geschlagenRedoCache = new ArrayList<>();
    FlowPane weißgeschlagenListe = new FlowPane();
    FlowPane schwarzgeschlagenListe = new FlowPane();

    //Dropshadow (Underglow fürs Brett) um anzuzeigen, welcher Spieler am Zug ist
    DropShadow dropShadow = new DropShadow();

    @Override
    public void start(Stage primaryStage) throws Exception {

        this.primaryStage = primaryStage;
        primaryStage.setTitle("Schachbrett");

        zeigeHauptmenue();
        primaryStage.show();

    }

    public void zeigeHauptmenue() {
        MainMenu menu = new MainMenu(this);
        primaryStage.setScene(menu.getScene());
    }

    public void starteSchachbrett(){


        weißgeschlagenListe = getWeißgeschlagenListe();
        schwarzgeschlagenListe = getSchwarzgeschlagenListe();

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

        VBox linksmitte = new VBox();
        linksmitte.setAlignment(Pos.CENTER);
        linksmitte.setMinWidth(200);
        linksmitte.setPrefWidth(200);
        linksmitte.setMaxWidth(200);


        VBox rechtsmitte = new VBox();
        rechtsmitte.setAlignment(Pos.CENTER);
        rechtsmitte.setPrefWidth(200);
        rechtsmitte.setMinWidth(200);
        rechtsmitte.setMaxWidth(200);

        Label Historie = new Label("Zug-Historie");
        Historie.setStyle("-fx-text-fill: black;");
        Historie.setFont(new Font( 20));

        ListView<String> historieliste = new ListView<>();
        historieliste.setPrefHeight(615);
        historieliste.setMaxHeight(615);
        historieliste.setMaxWidth(180);
        historieliste.setMaxWidth(180);
        historieliste.setEditable(false);
        historieliste.setFocusTraversable(false);
        historieliste.setStyle("-fx-background-color: #787878; " + "-fx-control-inner-background: #505050; " + "-fx-background-radius: 8px; " + "-fx-padding: 5px;");

        rechtsmitte.getChildren().addAll(Historie, historieliste);

        StackPane BoardContainer = new StackPane(Board);
        BoardContainer.setAlignment(Pos.CENTER);

        StackPane mitte = new StackPane();
        mitte.getChildren().addAll(BoardContainer, rechtsmitte, linksmitte);
        StackPane.setAlignment(BoardContainer, Pos.CENTER);
        StackPane.setAlignment(rechtsmitte, Pos.CENTER_RIGHT);
        StackPane.setAlignment(linksmitte, Pos.CENTER_LEFT);
        StackPane.setMargin(rechtsmitte, new Insets(0, 10,0,0));
        StackPane.setMargin(linksmitte, new Insets(0, 10,0,0));

        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: gray;");
        root.setTop(oben);
        root.setCenter(mitte);
        root.setBottom(unten);

        Scene scene = new Scene(root, 1100, 920);

        Label willkommen = new Label("Willkommen! Weiß startet das Spiel. \uD83D\uDE01");
        final double Schriftgröße = 30.0;
        willkommen.setFont(new Font(Schriftgröße));
        oben.getChildren().add(willkommen);


        Button vor = new Button();
        vor.setText("⟹");
        vor.setFont(new Font(30));
        vor.setStyle("-fx-background-color: dark-gray; -fx-text-fill: light-gray;");
        vor.setOnMouseClicked(event -> {
            String[][] neuerZustand = historie.redo();
            if (neuerZustand != null) {
                for (int i = 0; i < 8; i++) {
                    System.arraycopy(neuerZustand[i], 0, brettStatus[i], 0, 8);
                }

                weißamZug = !weißamZug;
                brettNeuZeichnen(Board);
                weißamZug = !weißamZug;

                if (!redoCache.isEmpty()) {
                    String wiederhergestellterZug = redoCache.remove(redoCache.size() - 1);
                    historieliste.getItems().add(wiederhergestellterZug);
                    historieliste.scrollTo(historieliste.getItems().size() - 1);
                }
                weißamZug = !weißamZug;
                if (redoCache.isEmpty()) {
                    vor.setDisable(true);
                }
            }

            if (!geschlagenRedoCache.isEmpty()) {
                String wiederhergestellterSchlag = geschlagenRedoCache.remove(geschlagenRedoCache.size() - 1);
                geschlagenUndoCache.add(wiederhergestellterSchlag);

                if (wiederhergestellterSchlag != null) {
                    String symbol = getUnicodeZeichen(wiederhergestellterSchlag);
                    if (wiederhergestellterSchlag.startsWith("w")) {
                        Label figur = new Label(symbol);
                        figur.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");
                        weißgeschlagenListe.getChildren().add(figur);
                    } else if (wiederhergestellterSchlag.startsWith("b")) {
                        Label figur = new Label(symbol);
                        figur.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
                        schwarzgeschlagenListe.getChildren().add(figur);
                    }
                }
            }

        });

        unten.setRight(vor);

        Button zurück = new Button();
        zurück.setText("⟸");
        zurück.setFont(new Font(30));
        zurück.setStyle("-fx-background-color: dark-gray; -fx-text-fill: light-gray;");
        zurück.setOnMouseClicked(event -> {
            String[][] alterZustand = historie.undo();
            if (alterZustand != null) {
                for (int i = 0; i < 8; i++) {
                    System.arraycopy(alterZustand[i], 0, brettStatus[i], 0, 8);
                }

                weißamZug = !weißamZug;
                brettNeuZeichnen(Board);
                weißamZug = !weißamZug;

                int letzterIndex = historieliste.getItems().size() - 1;
                if (letzterIndex >= 0) {
                    String entfernterZug = historieliste.getItems().remove(letzterIndex);
                    redoCache.add(entfernterZug);
                }
                vor.setDisable(false);
                zurück.setDisable(true);
                weißamZug = !weißamZug;
            }

            if (!geschlagenUndoCache.isEmpty()) {
                String entfernterSchlag = geschlagenUndoCache.remove(geschlagenUndoCache.size() - 1);
                geschlagenRedoCache.add(entfernterSchlag);

                if (entfernterSchlag != null) {
                    if (entfernterSchlag.startsWith("w")) {
                        if (!weißgeschlagenListe.getChildren().isEmpty()) {
                            weißgeschlagenListe.getChildren().remove(weißgeschlagenListe.getChildren().size() - 1);
                        }
                    } else if (entfernterSchlag.startsWith("b")) {
                        if (!schwarzgeschlagenListe.getChildren().isEmpty()) {
                            schwarzgeschlagenListe.getChildren().remove(schwarzgeschlagenListe.getChildren().size() - 1);
                        }
                    }
                }
            }

        });
        unten.setLeft(zurück);

        vor.setDisable(true);
        zurück.setDisable(true);


        Label schwarzzeit = new Label();
        schwarzzeit.setText("10:00");
        schwarzzeit.setFont(new Font(30));
        linksmitte.getChildren().add(schwarzzeit);


        linksmitte.getChildren().add(weißgeschlagenListe);

        Label temp1 = new Label();
        temp1.setText(" ");
        temp1.setFont(new Font(60));
        linksmitte.getChildren().add(temp1);

        Button reset = new Button();
        reset.setText("↺");
        reset.setFont(new Font(45));
        reset.setStyle("-fx-background-color: dark-gray; -fx-text-fill: light-gray;");
        reset.setOnMouseClicked(event -> {
            appNeustart(primaryStage);
        });
        linksmitte.getChildren().add(reset);

        Button close = new Button();
        close.setText("✕");
        close.setFont(new Font(45));
        close.setStyle("-fx-background-color: dark-gray; -fx-text-fill: light-gray;");
        close.setOnMouseClicked(event -> {
            System.exit(0);
        });
        linksmitte.getChildren().add(close);

        Label temp2 = new Label();
        temp2.setText(" ");
        temp2.setFont(new Font(60));
        linksmitte.getChildren().add(temp2);

        linksmitte.getChildren().add(schwarzgeschlagenListe);

        Label weißzeit = new Label();
        weißzeit.setText("10:00");
        weißzeit.setFont(new Font(30));
        linksmitte.getChildren().add(weißzeit);


        //Block für die Schachuhr und deren Funktionalität
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            if (weißamZug) {
                WeißZeit = WeißZeit - 1;
                String aktwzeit = zeitformatieren(WeißZeit);
                weißzeit.setText(aktwzeit);

                if (weißamZug && WeißZeit <= 0) {
                    timeline.stop();
                    Board.setDisable(true);
                    weißzeit.setText("00:00");
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

                    oben.getChildren().remove(willkommen);
                    Label Siegerlabel = new Label();
                    Siegerlabel.setFont(new Font(Schriftgröße));

                        Siegerlabel.setText("Zeit abgelaufen! Der Sieger ist: Schwarz");
                        root.setStyle("-fx-background-color: black;");
                        Siegerlabel.setStyle("-fx-text-fill: white;");

                    oben.getChildren().add(Siegerlabel);
                }
            } else if (weißamZug == false){
                SchwarzZeit = SchwarzZeit - 1;
                String aktszeit = zeitformatieren(SchwarzZeit);
                schwarzzeit.setText(aktszeit);

                if (!weißamZug && SchwarzZeit <= 0) {
                    timeline.stop();
                    Board.setDisable(true);
                    schwarzzeit.setText("00:00");
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

                    oben.getChildren().remove(willkommen);
                    Label Siegerlabel = new Label();
                    Siegerlabel.setFont(new Font(Schriftgröße));

                    Siegerlabel.setText("Zeit abgelaufen! Der Sieger ist: Weiß");
                    root.setStyle("-fx-background-color: white;");
                    Siegerlabel.setStyle("-fx-text-fill: black;");

                    oben.getChildren().add(Siegerlabel);
                }
            }
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();



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

                Figur = erzeugeStartFigur(row, col);


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
                        zeigelegaleZüge(Board, startRow, startCol);



                        //Event zum bewegen einer bereits angeklickten Figur
                    } else if (Figurenspeicher != null) {

                        highlightsentfernen(Board);

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

                            // Geschlagene Figur ermitteln um sie in die geschlagenenListe einzutragen
                            String geschlagen = enPassant ? geschlageneEnPassantFigur : alteZielFigur;

                            //Falls eine Figur geschlagen wurde, zur passenden Liste hinzufügen
                            if (geschlagen != null) {
                                String symbol = getUnicodeZeichen(geschlagen);

                                Label figurLabel = new Label(symbol);
                                figurLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

                                if (geschlagen.startsWith("w")) {
                                    figurLabel.setStyle("-fx-font-size: 18px; ");
                                    weißgeschlagenListe.getChildren().add(figurLabel);
                                } else if (geschlagen.startsWith("b")) {
                                    figurLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
                                    schwarzgeschlagenListe.getChildren().add(figurLabel);
                                }
                            }

                            geschlagenUndoCache.add(geschlagen);
                            geschlagenRedoCache.clear();

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


                            //Block zum hinzufügen gemachter Züge zur Zug Historie
                            char vonSpalte = (char) ('a' + startCol);
                            int vonZeile = 8-startRow;
                            char nachSpalte =  (char) ('a' + zielCol);
                            int nachZeile = 8-zielRow;

                            String figur = brettStatus[zielRow][zielCol];
                            String geschlagenText = "";
                            if (geschlagen != null) {
                                String geschlagenSymbol = getUnicodeZeichen(geschlagen);
                                geschlagenText = "  (x " + geschlagenSymbol + ")";
                            }

                            String figurSymbol = getUnicodeZeichen(figur);
                            String zugText = String.format("%s %c%d ➔ %c%d%s", //KI - gernerierte Formatierung (funktioniert)
                                    figurSymbol, vonSpalte, vonZeile, nachSpalte, nachZeile, geschlagenText);

                            historieliste.getItems().add(zugText);
                            historieliste.scrollTo(historieliste.getItems().size() - 1);

                            redoCache.clear();



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
                                        timeline.stop();
                                        root.setStyle("-fx-background-color: #1D1D1D;");
                                        Siegerlabel.setStyle("-fx-text-fill: white;");
                                        schwarzzeit.setStyle("-fx-text-fill: black;");
                                        weißzeit.setStyle("-fx-text-fill: black;");
                                    } else {
                                        timeline.stop();
                                        root.setStyle("-fx-background-color: white;");
                                        Siegerlabel.setStyle("-fx-text-fill: black;");
                                        reset.setStyle("-fx-background-color: white;");
                                    }
                                    oben.getChildren().add(Siegerlabel);

                                    Button MattresetButton = new Button("Reset");
                                    MattresetButton.setStyle("-fx-background-color: transparent;");
                                    MattresetButton.setText("Reset");
                                    MattresetButton.setStyle("-fx-text-fill: black;");
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
                                    PattresetButton.setStyle("-fx-background-color: transparent;");
                                    PattresetButton.setText("Reset");
                                    PattresetButton.setStyle("-fx-text-fill: black;");
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

        scene.getStylesheets().add("data:text/css," + ".list-cell { -fx-background-color: transparent; -fx-text-fill: dark-gray; -fx-min-width: 0px; -fx-padding: 0 1px; -fx-alignment: center; }" + ".list-cell:selected { -fx-background-color: transparent; -fx-text-fill: white; }");

        Board.setEffect(dropShadow);
        Board.setPadding(new Insets(40));
        primaryStage.setTitle("Schachspiel");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(920);
        primaryStage.show();

        // Am Ende deiner starteSpielfeld()-Methode:
        Scene gameScene = new Scene(root, 1000, 800); // (dein Root-Pane)
        primaryStage.setScene(gameScene);
    }
    // Erzeugt die Liste aller durch schwarz geschlagenen Figuren. also aller weißen geschlagenen Figuren
    private static FlowPane getSchwarzgeschlagenListe() {
        FlowPane liste = new FlowPane();
        liste.setHgap(1);
        liste.setVgap(2);
        liste.setPrefWrapLength(170);
        liste.setMaxWidth(180);
        liste.setMinHeight(70);
        liste.setStyle("-fx-background-color: transparent; -fx-background-radius: 8px; -fx-padding: 5px;");
        return liste;
    }

    //Erzeugt die Liste aller schwarzen geschlagenen Figuren
    private static FlowPane getWeißgeschlagenListe() {
        FlowPane liste = new FlowPane();
        liste.setHgap(1);
        liste.setVgap(2);
        liste.setPrefWrapLength(170);
        liste.setMaxWidth(180);
        liste.setMinHeight(70);
        liste.setStyle("-fx-background-color: transparent; -fx-background-radius: 8px; -fx-padding: 5px;");
        return liste;
    }

    //Kleine Hilfsmethode zum Neustarten des Spiels
    public void appNeustart(Stage primaryStage) {
        brettStatus = new String[8][8];
        weißamZug = true;

        WeißZeit = 600;
        SchwarzZeit = 600;

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

                    boolean HellerFleck = (row + col) % 2 == 0;
                    tile.setStyle("-fx-background-color: " + (HellerFleck ? "#F5F5F5" : "#708090") + ";");
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

        weißamZug = !weißamZug;
        Schacherkennung erkennung = new  Schacherkennung();
        boolean stehtimSchach = erkennung.StehtimSchach();
        weißamZug = !weißamZug;

        if (stehtimSchach) {
            int koenigRow = weißamZug ? WKönigRow : BKönigRow;
            int koenigCol = weißamZug ? WKönigCol : BKönigCol;

            // Kachel des betroffenen Königs rot einfärben
            for (Node node : board.getChildren()) {
                if (node instanceof StackPane) {
                    Integer r = GridPane.getRowIndex(node);
                    Integer c = GridPane.getColumnIndex(node);
                    if (r != null && c != null && r == koenigRow && c == koenigCol) {
                        node.setStyle("-fx-background-color: #FF0000;");
                        break;
                    }
                }
            }
            dropShadow.setColor(Color.RED);
        } else {
            dropShadow.setColor(weißamZug ? Color.WHITE : Color.BLACK);
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

    //Hilfsmethode zum erstellen der Startaufstellung der Fiiguren
    private Text erzeugeStartFigur(int row, int col) {
        if (row == 0 && (col == 0 || col == 7)) {
            brettStatus[row][col] = "bR";
            return new Text("♜");
        } else if (row == 0 && (col == 1 || col == 6)) {
            brettStatus[row][col] = "bN";
            return new Text("♞");
        } else if (row == 0 && (col == 2 || col == 5)) {
            brettStatus[row][col] = "bB";
            return new Text("♝");
        } else if (row == 0 && col == 3) {
            brettStatus[row][col] = "bQ";
            return new Text("♛");
        } else if (row == 0 && col == 4) {
            brettStatus[row][col] = "bK";
            return new Text("♚");
        } else if (row == 1) {
            brettStatus[row][col] = "bP";
            return new Text("♟");
        } else if (row == 7 && (col == 0 || col == 7)) {
            brettStatus[row][col] = "wR";
            return new Text("♖");
        } else if (row == 7 && (col == 1 || col == 6)) {
            brettStatus[row][col] = "wN";
            return new Text("♘");
        } else if (row == 7 && (col == 2 || col == 5)) {
            brettStatus[row][col] = "wB";
            return new Text("♗");
        } else if (row == 7 && col == 3) {
            brettStatus[row][col] = "wQ";
            return new Text("♕");
        } else if (row == 7 && col == 4) {
            brettStatus[row][col] = "wK";
            return new Text("♔");
        } else if (row == 6) {
            brettStatus[row][col] = "wP";
            return new Text("♙");
        } else {
            brettStatus[row][col] = null;
            return null;
        }
    }

    //Hilfsmethode zum entfernen aller Punkte der möglichen Züge
    private void highlightsentfernen(GridPane board) {
        for (Node node : board.getChildren()){
            if (node instanceof StackPane){
                StackPane tile =  (StackPane) node;
                tile.getChildren().removeIf(child -> "highlight".equals(child.getUserData()));
            }
        }
    }

    //Hilfsmethode zum Anzeigen der legalen Züge
    private void zeigelegaleZüge(GridPane board, int sRow, int sCol){
        highlightsentfernen(board);
        FigurenLogik logik =  new FigurenLogik();
        Schacherkennung erkennung = new Schacherkennung();

        for (Node node : board.getChildren()){
            if (node instanceof StackPane){
                StackPane tile =  (StackPane) node;
                Integer zRow = GridPane.getRowIndex(tile);
                Integer zCol = GridPane.getColumnIndex(tile);

                if (zRow == null || zCol == null) continue;

                if (logik.ZugErlaubnis(sRow, sCol, zRow, zCol)){

                    String alteZielfigur = brettStatus[zRow][zCol];
                    brettStatus[zRow][zCol] = brettStatus[sRow][sCol];
                    brettStatus[sRow][sCol] = null;

                    int altWKRow = WKönigRow, altWKCol = WKönigCol;
                    int altBKRow = BKönigRow, altBKCol = BKönigCol;

                    if ("wK".equals(brettStatus[zRow][zCol])){WKönigRow = zRow; WKönigCol = zCol;}
                    if ("bK".equals(brettStatus[zRow][zCol])) { BKönigRow = zRow; BKönigCol = zCol; }

                    weißamZug = !weißamZug;
                    boolean inSchach = erkennung.StehtimSchach();
                    weißamZug = !weißamZug;

                    brettStatus[sRow][sCol] = brettStatus[zRow][zCol];
                    brettStatus[zRow][zCol] = alteZielfigur;
                    WKönigRow = altWKRow; WKönigCol = altWKCol;
                    BKönigRow = altBKRow; BKönigCol = altBKCol;

                    if (!inSchach) {
                        if (weißamZug){
                            javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(12, Color.rgb(225, 225, 225, 0.75));
                            dot.setUserData("highlight");
                            dot.setMouseTransparent(true);
                            tile.getChildren().add(dot);
                        } else if (!weißamZug){
                            javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(12, Color.rgb(0, 0, 0, 0.75));
                            dot.setUserData("highlight");
                            dot.setMouseTransparent(true);
                            tile.getChildren().add(dot);
                        }
                    }
                }
            }
        }
    }

    private String zeitformatieren(int StartZeit){
        int RestMin = StartZeit / 60;
        int RestSek = StartZeit % 60;

        String tempMin = "" +  RestMin;
        if (RestMin < 10){
            tempMin =  "0" + RestMin;
        }
        String tempSek = "" +  RestSek;
        if (RestSek < 10){
            tempSek =  "0" + RestSek;
        }

        return tempMin + ":" + tempSek;
    }
}
