package schach;

import com.sun.javafx.iio.gif.GIFImageLoaderFactory;

import java.util.ArrayList;
import java.util.List;

public class ChessBot {

    private static int getFigurWert(String figurCode){
        if (figurCode == null) {
            return 0;
        }

        switch (figurCode) {

            case "wP": return 100;
            case "wN": return 300;
            case "wB": return 310; // Läufer minimal wertvoller als Springer
            case "wR": return 500;
            case "wQ": return 900;
            case "wK": return 200000; // Sehr hoher Wert damit niemals der König geopfert wird

            case "bP": return -100;
            case "bN": return -300;
            case "bB": return -310;
            case "bR": return -500;
            case "bQ": return -900;
            case "bK": return -200000;

            default: return 0;
        }
    }

    public static int bewerteStellung(String[][] brett){
        int gesamtWert = 0;

        for (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                gesamtWert += getFigurWert(brett[row][col]);
            }
        }
        return gesamtWert;
    }

    //Sucht alle eigenen Figuren und probiert mit diesen jeden möglichen Zug aus
    public static List<Zug> generiereLegaleZüge(boolean Weiß){
        List<Zug> legaleZüge = new ArrayList<>();  //Leere Liste in die alle möglichen legalen Züge dann kommen
        FigurenLogik logik = new FigurenLogik();
        Schacherkennung erkennung =  new Schacherkennung();

        boolean anfangsweiß = Schachbrett.weißamZug;
        Schachbrett.weißamZug = Weiß;
        char gesuchteFarbe = Weiß ? 'w' : 'b';

        for  (int row = 0; row < 8; row++){
            for (int col = 0; col < 8; col++){
                String figur = Schachbrett.brettStatus[row][col];
                if (figur != null && figur.charAt(0) == gesuchteFarbe){
                    for (int k = 0; k < 8; k++){
                        for (int l = 0; l < 8; l++){
                            if (logik.ZugErlaubnis(row, col, k, l)){

                                String alteZielFigur = Schachbrett.brettStatus[k][l];
                                Schachbrett.brettStatus[k][l] = Schachbrett.brettStatus[row][col];
                                Schachbrett.brettStatus[row][col] = null;

                                int altWKönigRow = Schachbrett.WKönigRow;
                                int altWKönigCol = Schachbrett.WKönigCol;
                                int altBKönigRow = Schachbrett.BKönigRow;
                                int altBKönigCol = Schachbrett.BKönigCol;

                                if (Schachbrett.brettStatus[k][l].equals("bK")){
                                    Schachbrett.BKönigRow = k;
                                    Schachbrett.BKönigCol = l;
                                } else if (Schachbrett.brettStatus[k][l].equals("bW")){
                                    Schachbrett.WKönigRow = k;
                                    Schachbrett.WKönigCol = l;
                                }

                                Schachbrett.weißamZug = !Weiß;
                                boolean nochimSchach = erkennung.StehtimSchach();
                                Schachbrett.weißamZug = Weiß;

                                Schachbrett.brettStatus[row][col] = Schachbrett.brettStatus[k][l];
                                Schachbrett.brettStatus[k][l] = alteZielFigur;
                                Schachbrett.WKönigRow = altWKönigRow;
                                Schachbrett.WKönigCol = altWKönigCol;
                                Schachbrett.BKönigCol = altBKönigCol;
                                Schachbrett.BKönigRow = altBKönigRow;

                                if (!nochimSchach){
                                    legaleZüge.add(new Zug(row, col, k, l));
                                }

                            }
                        }
                    }
                }
            }
        }

        Schachbrett.weißamZug = anfangsweiß;
        return legaleZüge;
    }
}
