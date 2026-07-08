package schach;

public class Schacherkennung {
    public boolean StehtimSchach() {
        FigurenLogik logik = new FigurenLogik();

        if (Schachbrett.weißamZug == true) {

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {

                    String Figur = Schachbrett.brettStatus[r][c];
                    if (Figur != null && Figur.charAt(0) == 'w') {
                        if (logik.ZugErlaubnis(r, c, Schachbrett.BKönigRow, Schachbrett.BKönigCol) == true){
                            return true;

                        }

                    }
                }
            }
        } else if (Schachbrett.weißamZug == false) {

            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    String Figur = Schachbrett.brettStatus[r][c];
                    if (Figur != null && Figur.charAt(0) == 'b') {
                        if (logik.ZugErlaubnis(r, c, Schachbrett.WKönigRow, Schachbrett.WKönigCol) == true){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
        //Funktion zur Berechnung ob ein Spieler überhaupt noch legale Züge hat oder im Schachmatt oder Patt steht.
    public boolean hatlegaleZügen() {
        FigurenLogik logik = new FigurenLogik();
        boolean bool = true;

        if (Schachbrett.weißamZug == true) {
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    String Figur = Schachbrett.brettStatus[r][c];
                    if (Figur != null && Figur.charAt(0) == 'w') {
                        for (int j = 0; j < 8; j++) {
                            for (int h = 0; h < 8; h++) {
                                if (logik.ZugErlaubnis(r, c, j, h) == true) {

                                    String alteZielFigur = Schachbrett.brettStatus[j][h];
                                    Schachbrett.brettStatus[j][h] = Schachbrett.brettStatus[r][c];
                                    Schachbrett.brettStatus[r][c] = null;

                                    int altWKönigRow = Schachbrett.WKönigRow;
                                    int altWKönigCol = Schachbrett.WKönigCol;
                                    int altBKönigRow = Schachbrett.BKönigRow;
                                    int altBKönigCol = Schachbrett.BKönigCol;

                                    if (Schachbrett.brettStatus[j][h].equals("bK")) {
                                        Schachbrett.BKönigRow = j;
                                        Schachbrett.BKönigCol = h;
                                    } else if (Schachbrett.brettStatus[j][h].equals("wK")) {
                                        Schachbrett.WKönigRow = j;
                                        Schachbrett.WKönigCol = h;
                                    }
                                    Schachbrett.weißamZug = false;
                                    boolean immernochSchach = StehtimSchach();
                                    Schachbrett.weißamZug = true;

                                    Schachbrett.brettStatus[r][c] = Schachbrett.brettStatus[j][h];
                                    Schachbrett.brettStatus[j][h] = alteZielFigur;
                                    Schachbrett.WKönigRow = altWKönigRow;
                                    Schachbrett.WKönigCol = altWKönigCol;
                                    Schachbrett.BKönigCol = altBKönigCol;
                                    Schachbrett.BKönigRow = altBKönigRow;

                                    if (immernochSchach == false) {
                                        return bool = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return bool = false;

        } else if (Schachbrett.weißamZug != true){
            for (int r = 0; r < 8; r++) {
                for (int c = 0; c < 8; c++) {
                    String Figur = Schachbrett.brettStatus[r][c];
                    if (Figur != null && Figur.charAt(0) == 'b') {
                        for (int j = 0; j < 8; j++) {
                            for (int h = 0; h < 8; h++) {
                                if (logik.ZugErlaubnis(r, c, j, h) == true) {

                                    String alteZielFigur = Schachbrett.brettStatus[j][h];
                                    Schachbrett.brettStatus[j][h] = Schachbrett.brettStatus[r][c];
                                    Schachbrett.brettStatus[r][c] = null;

                                    int altWKönigRow = Schachbrett.WKönigRow;
                                    int altWKönigCol = Schachbrett.WKönigCol;
                                    int altBKönigRow = Schachbrett.BKönigRow;
                                    int altBKönigCol = Schachbrett.BKönigCol;

                                    if (Schachbrett.brettStatus[j][h].equals("bK")) {
                                        Schachbrett.BKönigRow = j;
                                        Schachbrett.BKönigCol = h;
                                    } else if (Schachbrett.brettStatus[j][h].equals("wK")) {
                                        Schachbrett.WKönigRow = j;
                                        Schachbrett.WKönigCol = h;
                                    }
                                    Schachbrett.weißamZug = true;
                                    boolean immernochSchach = StehtimSchach();
                                    Schachbrett.weißamZug = false;

                                    Schachbrett.brettStatus[r][c] = Schachbrett.brettStatus[j][h];
                                    Schachbrett.brettStatus[j][h] = alteZielFigur;
                                    Schachbrett.WKönigRow = altWKönigRow;
                                    Schachbrett.WKönigCol = altWKönigCol;
                                    Schachbrett.BKönigCol = altBKönigCol;
                                    Schachbrett.BKönigRow = altBKönigRow;

                                    if (immernochSchach == false) {
                                        return bool = true;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            return bool = false;
        }
        return bool;
    }
}
