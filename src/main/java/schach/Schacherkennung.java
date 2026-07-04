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
}
