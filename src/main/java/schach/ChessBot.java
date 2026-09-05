package schach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessBot {

    private static int getFigurWert(String figurCode) {
        if (figurCode == null) {
            return 0;
        }

        switch (figurCode) {

            case "wP":
                return 100;
            case "wN":
                return 300;
            case "wB":
                return 310; // Läufer minimal wertvoller als Springer
            case "wR":
                return 500;
            case "wQ":
                return 900;
            case "wK":
                return 200000; // Sehr hoher Wert damit niemals der König geopfert wird

            case "bP":
                return -100;
            case "bN":
                return -300;
            case "bB":
                return -310;
            case "bR":
                return -500;
            case "bQ":
                return -900;
            case "bK":
                return -200000;

            default:
                return 0;
        }
    }

    public static int bewerteStellung(String[][] brett) {
        int gesamtWert = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gesamtWert += getFigurWert(brett[row][col]);
            }
        }
        return gesamtWert;
    }

    //Sucht alle eigenen Figuren und probiert mit diesen jeden möglichen Zug aus
    public static List<Zug> generierealleLegalenZüge(boolean Weiß) {
        List<Zug> legaleZüge = new ArrayList<>();  //Leere Liste in die alle möglichen legalen Züge dann kommen
        FigurenLogik logik = new FigurenLogik();
        Schacherkennung erkennung = new Schacherkennung();

        boolean anfangsweiß = Schachbrett.weißamZug;
        Schachbrett.weißamZug = Weiß;
        char gesuchteFarbe = Weiß ? 'w' : 'b';

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String figur = Schachbrett.brettStatus[row][col];
                if (figur != null && figur.charAt(0) == gesuchteFarbe) {
                    for (int k = 0; k < 8; k++) {
                        for (int l = 0; l < 8; l++) {
                            if (logik.ZugErlaubnis(row, col, k, l)) {

                                String alteZielFigur = Schachbrett.brettStatus[k][l];
                                Schachbrett.brettStatus[k][l] = Schachbrett.brettStatus[row][col];
                                Schachbrett.brettStatus[row][col] = null;

                                int altWKönigRow = Schachbrett.WKönigRow;
                                int altWKönigCol = Schachbrett.WKönigCol;
                                int altBKönigRow = Schachbrett.BKönigRow;
                                int altBKönigCol = Schachbrett.BKönigCol;

                                if (Schachbrett.brettStatus[k][l].equals("bK")) {
                                    Schachbrett.BKönigRow = k;
                                    Schachbrett.BKönigCol = l;
                                } else if (Schachbrett.brettStatus[k][l].equals("wK")) {
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

                                if (!nochimSchach) {
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

    //berechnet den besten Zug für die jeweils angegebene Farbe mit anpassbarer Suchtiefe ( also wie viele Züge der Bot in die zukunft schauen können soll)
    public static Zug berechnebestenZug(int tiefe, boolean Weiß) {
        List<Zug> legaleZuege = generierealleLegalenZüge(Weiß);
        if (legaleZuege.isEmpty()) {
            return null;
        }

        java.util.Collections.shuffle(legaleZuege);

        List<Zug> besteZuege =  new ArrayList<>();
        int besterWert = Weiß ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        for (Zug zug : legaleZuege) {

            String alteZielFigur = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = Schachbrett.brettStatus[zug.startRow][zug.startCol];
            Schachbrett.brettStatus[zug.startRow][zug.startCol] = null;

            int altWKRow = Schachbrett.WKönigRow, altWKCol = Schachbrett.WKönigCol;
            int altBKRow = Schachbrett.BKönigRow, altBKCol = Schachbrett.BKönigCol;

            if ("wK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                Schachbrett.WKönigRow = zug.endRow;
                Schachbrett.WKönigCol = zug.endCol;
            }
            if ("bK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                Schachbrett.BKönigRow = zug.endRow;
                Schachbrett.BKönigCol = zug.endCol;
            }

            int wert = minimax(tiefe - 1, !Weiß);

            Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
            Schachbrett.WKönigRow = altWKRow;
            Schachbrett.WKönigCol = altWKCol;
            Schachbrett.BKönigRow = altBKRow;
            Schachbrett.BKönigRow = altBKRow;

            if (Weiß) {
                if (wert > besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                }
            } else {
                if (wert < besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                }
            }
        }
        if (besteZuege.isEmpty()) {
            return null;
        }

        int randomIndex = new Random().nextInt(besteZuege.size());
        return besteZuege.get(randomIndex); //Nimmt bei mehreren gleichwertigen Zügen einen zufälligen statt z.b. in der eröffnung immer den selben zug zu spielen
    }

    // MiniMax algorithmus wie aus der Algorithmen Vorlesung der Züge Simuliert
    private static int minimax(int tiefe, boolean Weiß) {
        if (tiefe == 0) {
            return bewerteStellung(Schachbrett.brettStatus);
        }

        List<Zug> legaleZuge = generierealleLegalenZüge(Weiß);

        if (legaleZuge.isEmpty()) {
            return bewerteStellung(Schachbrett.brettStatus);
        }

        if (Weiß) {
            int maxWert = Integer.MIN_VALUE;
            for (Zug zug : legaleZuge) {

                String alteZielFigur = Schachbrett.brettStatus[zug.endRow][zug.endCol];
                Schachbrett.brettStatus[zug.endRow][zug.endCol] = Schachbrett.brettStatus[zug.startRow][zug.startCol];
                Schachbrett.brettStatus[zug.startRow][zug.startCol] = null;

                int altWKRow = Schachbrett.WKönigRow, altWKCol = Schachbrett.WKönigCol;
                int altBKRow = Schachbrett.BKönigRow, altBKCol = Schachbrett.BKönigCol;

                if ("wK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                    Schachbrett.WKönigRow = zug.endRow;
                    Schachbrett.WKönigCol = zug.endCol;
                }
                if ("bK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                    Schachbrett.BKönigRow = zug.endRow;
                    Schachbrett.BKönigCol = zug.endCol;
                }

                int wert = minimax(tiefe - 1, false);

                Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
                Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
                Schachbrett.WKönigRow = altWKRow;
                Schachbrett.WKönigCol = altWKCol;
                Schachbrett.BKönigRow = altBKRow;
                Schachbrett.BKönigCol = altBKCol;

                maxWert = Math.max(maxWert, wert);
            }
            return maxWert;
        } else {
            int minWert = Integer.MAX_VALUE;
            for (Zug zug : legaleZuge) {

            String alteZielFigur = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = Schachbrett.brettStatus[zug.startRow][zug.startCol];
            Schachbrett.brettStatus[zug.startRow][zug.startCol] = null;

            int altWKRow = Schachbrett.WKönigRow, altWKCol = Schachbrett.WKönigCol;
            int altBKRow = Schachbrett.BKönigRow, altBKCol = Schachbrett.BKönigCol;

            if ("wK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                Schachbrett.WKönigRow = zug.endRow;
                Schachbrett.WKönigCol = zug.endCol;
            }
            if ("bK".equals(Schachbrett.brettStatus[zug.endRow][zug.endCol])) {
                Schachbrett.BKönigRow = zug.endRow;
                Schachbrett.BKönigCol = zug.endCol;
            }

            int wert = minimax(tiefe - 1, true);

            Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
            Schachbrett.WKönigRow = altWKRow;
            Schachbrett.WKönigCol = altWKCol;
            Schachbrett.BKönigRow = altBKRow;
            Schachbrett.BKönigCol = altBKCol;

            minWert = Math.min(minWert, wert);
            }
            return minWert;
        }
    }
}
