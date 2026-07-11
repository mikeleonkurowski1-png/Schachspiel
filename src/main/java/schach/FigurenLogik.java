package schach;


public class FigurenLogik {
    public boolean ZugErlaubnis(int sRow,int sCol, int zRow, int zCol) {
        String Figur = Schachbrett.brettStatus[sRow][sCol];
        String ZielFigur = Schachbrett.brettStatus[zRow][zCol];
        Schacherkennung erkennung = new Schacherkennung();

        if (Figur == null) {
            return false;
        }
        //Dieser if-Block überprüft, ob man versucht eine eigene Figur zu schlagen.
        if (ZielFigur != null) {
            char zielFarbe = ZielFigur.charAt(0);
            char startFarbe = Figur.charAt(0);
            if (zielFarbe == startFarbe) {
                return false;
            }
        }

            switch (Figur) {

                case "wR", "bR":

                    if (TurmKollision(sRow, sCol, zRow, zCol)) {
                        return true;
                    } else {
                        return false;
                    }

                case "wN", "bN":

                    if (zRow == sRow + 2 && zCol == sCol + 1 || zRow == sRow + 2 && zCol == sCol - 1 || zRow == sRow - 2 && zCol == sCol + 1 || zRow == sRow - 2 && zCol == sCol - 1 || zRow == sRow + 1 && zCol == sCol + 2 || zRow == sRow - 1 && zCol == sCol + 2 || zRow == sRow + 1 && zCol == sCol - 2 || zRow == sRow - 1 && zCol == sCol - 2) {
                        return true;
                    } else {
                        return false;
                    }

                case "wB", "bB":

                    if (LäuferKollision(sRow, sCol, zRow, zCol)) {
                        return true;
                    } else {
                        return false;
                    }

                case "wQ", "bQ":

                    if (TurmKollision(sRow, sCol, zRow, zCol) || LäuferKollision(sRow, sCol, zRow, zCol)) {
                        return true;
                    } else {
                        return false;
                    }

                case "wK", "bK":

                    if (zRow == sRow + 1 && zCol == sCol || zRow == sRow - 1 && zCol == sCol || zCol == sCol + 1 && zRow == sRow || zCol == sCol - 1 && zRow == sRow || zRow == sRow + 1 && zCol == sCol - 1 || zRow == sRow + 1 && zCol == sCol + 1 || zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 1 && zCol == sCol + 1) {
                            return true;
                    } else {
                        return false;
                    }

                case "bP":

                    if ((zRow == sRow + 1 && (zCol == sCol - 1 || zCol == sCol + 1)) && Schachbrett.brettStatus[zRow][zCol] != null) {
                        return true;
                    }

                    if (sRow == 1) { //Checken ob der Bauer noch nicht gezogen ist, falls nicht 2 Felder laufen ermöglichen

                        if (zRow == sRow + 1 && sCol == zCol && Schachbrett.brettStatus[zRow][zCol] == null || zRow == sRow + 2 && zCol == sCol && (Schachbrett.brettStatus[zRow][zCol] == null && Schachbrett.brettStatus[zRow - 1][zCol] == null)) {
                            return true;
                        } else {
                            return false;
                        }

                    } else {

                        if (zRow == sRow + 1 && sCol == zCol && Schachbrett.brettStatus[zRow][zCol] == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                case "wP":

                    if ((zRow == sRow - 1 && (zCol == sCol - 1 || zCol == sCol + 1)) && Schachbrett.brettStatus[zRow][zCol] != null) {
                        return true;
                    }
                    if (sRow == 6) { //Checken ob der Bauer noch nicht gezogen ist, falls nicht 2 Felder laufen ermöglichen
                        if (zRow == sRow - 1 && sCol == zCol && Schachbrett.brettStatus[zRow][zCol] == null || zRow == sRow - 2 && sCol == zCol && (Schachbrett.brettStatus[zRow][zCol] == null && Schachbrett.brettStatus[zRow + 1][zCol] == null)) {
                            return true;
                        } else {
                            return false;
                        }
                    } else {
                        if (zRow == sRow - 1 && sCol == zCol && Schachbrett.brettStatus[zRow][zCol] == null) {
                            return true;
                        } else {
                            return false;
                        }
                    }

            }

        return false;
    }


    public boolean TurmKollision (int sRow, int sCol, int zRow, int zCol) {

        if (sRow == zRow && sCol != zCol || sRow != zRow && sCol == zCol) {

            //Prüft ob etwas bei einer Bewegung nach rechts im Weg ist
            if (sRow == zRow && zCol > sCol){
                for (int i = sCol + 1; i < zCol; i++) {
                    if (Schachbrett.brettStatus[sRow][i] != null) {
                        return false;
                    }
                } return true;
            }
            //Prüft ob etwas bei einer Bewegung nach links im Weg ist
            else if (sRow == zRow && zCol < sCol) {
                for (int i = sCol - 1; i > zCol; i--) {
                    if (Schachbrett.brettStatus[sRow][i] != null) {
                        return false;
                    }
                } return true;
            }
            // Prüft ob etwas bei einer Bewegung nach oben im Weg ist
            else if (sCol == zCol && sRow > zRow) {
                for (int i = sRow - 1; i > zRow; i--) {
                    if (Schachbrett.brettStatus[i][sCol] != null) {
                        return false;
                    }

                } return true;
            }
            //Prüft ob etwas bei einer Bewegung nach unten im Weg ist
            else if (sCol == zCol && sRow < zRow) {
                for (int i = sRow + 1; i < zRow; i++) {
                    if (Schachbrett.brettStatus[i][sCol] != null) {
                        return false;
                    }
                } return true;
            }
        } else {
            return false;
        }
        return false;
    }

    public boolean LäuferKollision (int sRow, int sCol, int zRow, int zCol) {

        if (zRow == sRow + 1 &&  zCol == sCol + 1 || zRow == sRow + 2 && zCol == sCol + 2 || zRow == sRow + 3 && zCol == sCol + 3 || zRow == sRow + 4 && zCol == sCol + 4 || zRow == sRow + 5 && zCol == sCol + 5 || zRow == sRow + 6 && zCol == sCol + 6 || zRow == sRow + 7 && zCol == sCol + 7 || zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 2 && zCol == sCol - 2 || zRow == sRow - 3 && zCol == sCol - 3 || zRow == sRow - 4 && zCol == sCol - 4 || zRow == sRow - 5 && zCol == sCol - 5 || zRow == sRow - 6 && zCol == sCol - 6 || zRow == sRow - 7 && zCol == sCol - 7 || zRow == sRow + 1 &&  zCol == sCol - 1 || zRow == sRow + 2 && zCol == sCol - 2 || zRow == sRow + 3 && zCol == sCol - 3 || zRow == sRow + 4 && zCol == sCol - 4 || zRow == sRow + 5 && zCol == sCol - 5 || zRow == sRow + 6 && zCol == sCol - 6 || zRow == sRow + 7 && zCol == sCol - 7 || zRow == sRow - 1 && zCol == sCol + 1 || zRow == sRow - 2 && zCol == sCol + 2 || zRow == sRow - 3 && zCol == sCol + 3 || zRow == sRow - 4 && zCol == sCol + 4 || zRow == sRow - 5 && zCol == sCol + 5 || zRow == sRow - 6 && zCol == sCol + 6 || zRow == sRow - 7 && zCol == sCol + 7) {
            //Überprüft KOllision bei rechts unten Bewegung
            if (zRow > sRow && zCol > sCol) {
                int r = sRow + 1;
                int c = sCol + 1;

                while (c < zCol && r < zRow){
                    if (Schachbrett.brettStatus[r][c] != null) {
                        return false;
                    }
                    r++;
                    c++;
                }
                return true;
            }

            //Überprüft KOllision bei rechts oben Bewegung
            if (zRow < sRow && zCol > sCol) {
                int r = sRow - 1;
                int c = sCol + 1;

                while (c < zCol && r > zRow){
                    if (Schachbrett.brettStatus[r][c] != null) {
                        return false;
                    }
                    r--;
                    c++;
                }
                return true;
            }
            //Überprüft KOllision bei links unten Bewegung
            if (zRow > sRow && zCol < sCol) {
                int r = sRow + 1;
                int c = sCol - 1;

                while (c > zCol && r < zRow){
                    if (Schachbrett.brettStatus[r][c] != null) {
                        return false;
                    }
                    r++;
                    c--;
                }
                return true;
            }
            //Überprüft KOllision bei links oben Bewegung
            if (zRow < sRow && zCol < sCol) {
                int r = sRow - 1;
                int c = sCol - 1;

                while (c > zCol && r > zRow){
                    if (Schachbrett.brettStatus[r][c] != null) {
                        return false;
                    }
                    r--;
                    c--;
                }
                return true;
            }

        } else {
            return false;
        }
        return false;
    }
}

