package schach;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessBot {

    public String[][] brett = Schachbrett.brettStatus;
    private static final int Matt_Wert = 1_000_000;

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


    // Speichert alles, was beim Testen eines Zuges verändert wird. So kann der MiniMax-Algorithmus die Stellung danach wieder exakt zurücksetzen.
    private static class ZugZustand {
        String gezogeneFigur;
        String alteZielfigur;
        String enPassantFigur;
        String turmFigur;
        int alteWKRow, alteWKCol, alteBKRow, alteBKCol;
        int alteEnPassantRow, alteEnPassantCol;
        boolean alteWKbewegt, alteBKbewegt, alteWRbewegt, alteBRbewegt;
        boolean enPassant;
        boolean rochade;
    }

    private static ZugZustand fuehreZugAus(Zug zug) {
        ZugZustand zustand = new ZugZustand();
        zustand.gezogeneFigur = Schachbrett.brettStatus[zug.startRow][zug.startCol];
        zustand.alteZielfigur = Schachbrett.brettStatus[zug.endRow][zug.endCol];
        zustand.alteWKRow = Schachbrett.WKönigRow;
        zustand.alteWKCol = Schachbrett.WKönigCol;
        zustand.alteBKRow = Schachbrett.BKönigRow;
        zustand.alteBKCol = Schachbrett.BKönigCol;
        zustand.alteEnPassantRow = Schachbrett.enPassantRow;
        zustand.alteEnPassantCol = Schachbrett.enPassantCol;
        zustand.alteWKbewegt = FigurenLogik.WKbewegt;
        zustand.alteBKbewegt = FigurenLogik.BKbewegt;
        zustand.alteWRbewegt = FigurenLogik.WRbewegt;
        zustand.alteBRbewegt = FigurenLogik.BRbewegt;

        zustand.enPassant = ("wP".equals(zustand.gezogeneFigur) || "bP".equals(zustand.gezogeneFigur))
                && Math.abs(zug.endCol - zug.startCol) == 1
                && zustand.alteZielfigur == null
                && Schachbrett.enPassantRow == zug.startRow
                && Schachbrett.enPassantCol == zug.endCol;
        if (zustand.enPassant) {
            zustand.enPassantFigur = Schachbrett.brettStatus[zug.startRow][zug.endCol];
            Schachbrett.brettStatus[zug.startRow][zug.endCol] = null;
        }

        Schachbrett.brettStatus[zug.endRow][zug.endCol] = zustand.gezogeneFigur;
        Schachbrett.brettStatus[zug.startRow][zug.startCol] = null;

        zustand.rochade = ("wK".equals(zustand.gezogeneFigur) || "bK".equals(zustand.gezogeneFigur))
                && Math.abs(zug.endCol - zug.startCol) == 2;
        if (zustand.rochade) {
            int turmStartCol = zug.endCol > zug.startCol ? 7 : 0;
            int turmZielCol = zug.endCol > zug.startCol ? 5 : 3;
            zustand.turmFigur = Schachbrett.brettStatus[zug.startRow][turmStartCol];
            Schachbrett.brettStatus[zug.startRow][turmZielCol] = zustand.turmFigur;
            Schachbrett.brettStatus[zug.startRow][turmStartCol] = null;
        }

        // Der Bot promotet immer zur Dame. Das ist auch die übliche Wahl.
        if ("wP".equals(zustand.gezogeneFigur) && zug.endRow == 0) {
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = "wQ";
        } else if ("bP".equals(zustand.gezogeneFigur) && zug.endRow == 7) {
            Schachbrett.brettStatus[zug.endRow][zug.endCol] = "bQ";
        }

        if ("wK".equals(zustand.gezogeneFigur)) {
            Schachbrett.WKönigRow = zug.endRow;
            Schachbrett.WKönigCol = zug.endCol;
            FigurenLogik.WKbewegt = true;
        } else if ("bK".equals(zustand.gezogeneFigur)) {
            Schachbrett.BKönigRow = zug.endRow;
            Schachbrett.BKönigCol = zug.endCol;
            FigurenLogik.BKbewegt = true;
        } else if ("wR".equals(zustand.gezogeneFigur)) {
            FigurenLogik.WRbewegt = true;
        } else if ("bR".equals(zustand.gezogeneFigur)) {
            FigurenLogik.BRbewegt = true;
        }

        Schachbrett.enPassantRow = -1;
        Schachbrett.enPassantCol = -1;
        if (("wP".equals(zustand.gezogeneFigur) || "bP".equals(zustand.gezogeneFigur))
                && Math.abs(zug.endRow - zug.startRow) == 2) {
            Schachbrett.enPassantRow = zug.endRow;
            Schachbrett.enPassantCol = zug.endCol;
        }
        return zustand;
    }

    private static void macheZugRueckgaengig(Zug zug, ZugZustand zustand) {
        Schachbrett.brettStatus[zug.startRow][zug.startCol] = zustand.gezogeneFigur;
        Schachbrett.brettStatus[zug.endRow][zug.endCol] = zustand.alteZielfigur;
        if (zustand.enPassant) {
            Schachbrett.brettStatus[zug.startRow][zug.endCol] = zustand.enPassantFigur;
        }
        if (zustand.rochade) {
            int turmStartCol = zug.endCol > zug.startCol ? 7 : 0;
            int turmZielCol = zug.endCol > zug.startCol ? 5 : 3;
            Schachbrett.brettStatus[zug.startRow][turmStartCol] = zustand.turmFigur;
            Schachbrett.brettStatus[zug.startRow][turmZielCol] = null;
        }
        Schachbrett.WKönigRow = zustand.alteWKRow;
        Schachbrett.WKönigCol = zustand.alteWKCol;
        Schachbrett.BKönigRow = zustand.alteBKRow;
        Schachbrett.BKönigCol = zustand.alteBKCol;
        Schachbrett.enPassantRow = zustand.alteEnPassantRow;
        Schachbrett.enPassantCol = zustand.alteEnPassantCol;
        FigurenLogik.WKbewegt = zustand.alteWKbewegt;
        FigurenLogik.BKbewegt = zustand.alteBKbewegt;
        FigurenLogik.WRbewegt = zustand.alteWRbewegt;
        FigurenLogik.BRbewegt = zustand.alteBRbewegt;
    }

    private static int getFigurWert(String figurCode, int row, int col,boolean endspiel) {
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
                material = 40000;
                position = endspiel ? König_Tabelle_Endspiel[TabellenRow][TabellenCol] : König_Tabelle[TabellenRow][TabellenCol];
                break;

            default:
                return 0;
        }

        int gesamt = material + position;
        return istWeiß ? gesamt : -gesamt;
    }

    public static int bewerteStellung(String[][] brett) {
        int gesamtWert = 0;
        boolean endspiel = istEndspiel(brett);

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                gesamtWert += getFigurWert(brett[row][col],  row, col, endspiel);
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
                            String zielfigur = Schachbrett.brettStatus[k][l];
                            // Ein König wird nicht geschlagen. Schachmatt entsteht dadurch,
                            // dass keine legalen Züge mehr übrig sind.
                            if (zielfigur != null && zielfigur.charAt(1) == 'K') {
                                continue;
                            }
                            if (logik.ZugErlaubnis(row, col, k, l)) {

                                Zug zug = new Zug(row, col, k, l);
                                ZugZustand zustand = fuehreZugAus(zug);

                                Schachbrett.weißamZug = !Weiß;
                                boolean nochimSchach = erkennung.StehtimSchach();
                                Schachbrett.weißamZug = Weiß;

                                macheZugRueckgaengig(zug, zustand);

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

    //berechnet den besten Zug für die jeweils angegebene Farbe mit anpassbarer Suchtiefe (also wie viele Züge der Bot in die zukunft schauen können soll)
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

            ZugZustand zustand = fuehreZugAus(zug);

            int wert = minimax(tiefe - 1,alpha, beta, !Weiß);

            macheZugRueckgaengig(zug, zustand);

            if (Weiß) {
                if (wert > besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                } else if (wert == besterWert) {
                    besteZuege.add(zug);
                }
                alpha = Math.max(alpha, besterWert);
            } else {
                if (wert < besterWert) {
                    besterWert = wert;
                    besteZuege.clear();
                    besteZuege.add(zug);
                }  else if (wert == besterWert) {
                    besteZuege.add(zug);
                }
                beta = Math.min(beta, besterWert);
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
            Schacherkennung erkennung = new Schacherkennung();
            boolean anfangsweiß = Schachbrett.weißamZug;
            Schachbrett.weißamZug = !Weiß;
            boolean imSchach = erkennung.StehtimSchach();
            Schachbrett.weißamZug = anfangsweiß;

            if (imSchach) {
                return Weiß ? -900000 - tiefe : 900000 + tiefe;
            }
            return 0;
        }

        if (Weiß) {
            int maxWert = Integer.MIN_VALUE;
            for (Zug zug : legaleZuge) {

                ZugZustand zustand = fuehreZugAus(zug);

                int wert = minimax(tiefe - 1, alpha, beta, false);

                macheZugRueckgaengig(zug, zustand);

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

                ZugZustand zustand = fuehreZugAus(zug);

                int wert = minimax(tiefe - 1, alpha, beta, true);

                macheZugRueckgaengig(zug, zustand);

                minWert = Math.min(minWert, wert);
                beta = Math.min(beta, minWert);
                if (beta <= alpha) {
                    break;
                }
            }
            return minWert;
        }
    }

    // Hilfsmethode damit der König weiß wann Endspiel ist und wann nicht
    private static boolean istEndspiel(String[][] brett) {
        int nichtBauernMaterial = 0;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                String fig = brett[row][col];
                if (fig != null) {
                    char typ = fig.charAt(1);
                    if (typ != 'P' && typ != 'K') {
                        if (typ == 'Q') nichtBauernMaterial += 900;
                        else if (typ == 'R') nichtBauernMaterial += 500;
                        else if (typ == 'B' || typ == 'N') nichtBauernMaterial += 300;
                    }
                }
            }
        }
        return nichtBauernMaterial <= 2400;
    }
}
