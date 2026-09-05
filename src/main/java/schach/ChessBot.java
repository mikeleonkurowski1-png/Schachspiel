package schach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessBot {

    // Tabellen für jede Figur die angibt wo diese am besten stehen sollte. Also Positioning (Source: https://adamberent.com/piece-square-table/)

    private static final int[][] Bauern_Tabelle = {
            { 0,  0,  0,  0,  0,  0,  0,  0},
            {50, 50, 50, 50, 50, 50, 50, 50},
            {10, 10, 20, 30, 30, 20, 10, 10},
            { 5,  5, 10, 25, 25, 10,  5,  5},
            { 0,  0,  0, 20, 20,  0,  0,  0},
            { 5, -5,-10,  0,  0,-10, -5,  5},
            { 5, 10, 10,-20,-20, 10, 10,  5},
            { 0,  0,  0,  0,  0,  0,  0,  0}
    };

    private static final int[][] Springer_Tabelle = {
            {-50,-40,-30,-30,-30,-30,-40,-50},
            {-40,-20,  0,  0,  0,  0,-20,-40},
            {-30,  0, 10, 15, 15, 10,  0,-30},
            {-30,  5, 15, 20, 20, 15,  5,-30},
            {-30,  0, 15, 20, 20, 15,  0,-30},
            {-30,  5, 10, 15, 15, 10,  5,-30},
            {-40,-20,  0,  5,  5,  0,-20,-40},
            {-50,-40,-30,-30,-30,-30,-40,-50}
    };

    private static final int[][] Läufer_Tabelle = {
            {-20,-10,-10,-10,-10,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5, 10, 10,  5,  0,-10},
            {-10,  5,  5, 10, 10,  5,  5,-10},
            {-10,  0, 10, 10, 10, 10,  0,-10},
            {-10, 10, 10, 10, 10, 10, 10,-10},
            {-10,  5,  0,  0,  0,  0,  5,-10},
            {-20,-10,-40,-10,-10,-40,-10,-20},
    };

    private static final int[][] König_Tabelle = {
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-30, -40, -40, -50, -50, -40, -40, -30},
            {-20, -30, -30, -40, -40, -30, -30, -20},
            {-10, -20, -20, -20, -20, -20, -20, -10},
            {20,  20,   0,   0,   0,   0,  20,  20},
            {20,  30,  10,   0,   0,  10,  30,  20}
    };

    private static final int[][] König_Tabelle_Endspiel = {
            {-50,-40,-30,-20,-20,-30,-40,-50},
            {-30,-20,-10,  0,  0,-10,-20,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 30, 40, 40, 30,-10,-30},
            {-30,-10, 20, 30, 30, 20,-10,-30},
            {-30,-30,  0,  0,  0,  0,-30,-30},
            {-50,-30,-30,-30,-30,-30,-30,-50}
    };

    private static final int[][] Turm_Tabelle = {
            {  0,  0,  0,  0,  0,  0,  0,  0},
            {  5, 10, 10, 10, 10, 10, 10,  5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            { -5,  0,  0,  0,  0,  0,  0, -5},
            {  0,  0,  0,  5,  5,  0,  0,  0}
    };

    private static final int[][] Dame_Tabelle = {
            {-20,-10,-10, -5, -5,-10,-10,-20},
            {-10,  0,  0,  0,  0,  0,  0,-10},
            {-10,  0,  5,  5,  5,  5,  0,-10},
            { -5,  0,  5,  5,  5,  5,  0, -5},
            {  0,  0,  5,  5,  5,  5,  0, -5},
            {-10,  5,  5,  5,  5,  5,  0,-10},
            {-10,  0,  5,  0,  0,  0,  0,-10},
            {-20,-10,-10, -5, -5,-10,-10,-20}
    };

    private static int getFigurWert(String figurCode, int row, int col) {
        if (figurCode == null) {
            return 0;
        }

        boolean istWeiß = figurCode.charAt(0) == 'w';
        char typ = figurCode.charAt(1);

        int material = 0;
        int position = 0;

        int TabellenRow = istWeiß ? row : (7-row);
        int TabellenCol = col;

        switch (typ) {

            case 'P':
                material = 100;
                position = Bauern_Tabelle[TabellenRow][TabellenCol];
                break;
            case 'N':
                material = 300;
                position = Springer_Tabelle[TabellenRow][TabellenCol];
                break;
            case 'B':
                material = 310;
                position = Läufer_Tabelle[TabellenRow][TabellenCol];
                break;
            case 'R':
                material = 500;
                position = Turm_Tabelle[TabellenRow][TabellenCol];
                break;
            case 'Q':
                material = 900;
                position = Dame_Tabelle[TabellenRow][TabellenCol];
                break;
            case 'K':
                material = 200000;
                position = König_Tabelle[TabellenRow][TabellenCol];
                break;

            default:
                return 0;
        }

        int gesamt = material + position;
        return istWeiß ? gesamt : -gesamt;
    }

    public static int bewerteStellung(String[][] brett) {
        int gesamtWert = 0;

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gesamtWert += getFigurWert(brett[row][col],  row, col);
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
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

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

            int wert = minimax(tiefe - 1,alpha, beta, !Weiß);

            Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
            Schachbrett.WKönigRow = altWKRow;
            Schachbrett.WKönigCol = altWKCol;
            Schachbrett.BKönigRow = altBKRow;
            Schachbrett.BKönigCol = altBKCol;

            if (Weiß) {
                if (wert > besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                } else if (wert == besterWert) {
                    besteZuege.add(zug);
                }
            } else {
                if (wert < besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                }  else if (wert == besterWert) {
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
    private static int minimax(int tiefe,int alpha, int beta, boolean Weiß) {
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

                int wert = minimax(tiefe - 1, alpha, beta, false);

                Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
                Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
                Schachbrett.WKönigRow = altWKRow;
                Schachbrett.WKönigCol = altWKCol;
                Schachbrett.BKönigRow = altBKRow;
                Schachbrett.BKönigCol = altBKCol;

                maxWert = Math.max(maxWert, wert);
                alpha = Math.max(alpha, maxWert);
                if (beta <= alpha) {
                    break;
                }
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

            int wert = minimax(tiefe - 1, alpha, beta, true);

            Schachbrett.brettStatus[zug.startRow][zug.startCol] = Schachbrett.brettStatus[zug.endRow][zug.endCol];
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = alteZielFigur;
            Schachbrett.WKönigRow = altWKRow;
            Schachbrett.WKönigCol = altWKCol;
            Schachbrett.BKönigRow = altBKRow;
            Schachbrett.BKönigCol = altBKCol;

            minWert = Math.min(minWert, wert);
            beta = Math.min(beta, minWert);
            if (beta <= alpha) {
                break;
            }
            }
            return minWert;
        }
    }
}
