package schach;


public class FigurenLogik {
    public boolean ZugErlaubnis(int sRow,int sCol, int zRow, int zCol) {
        String Figur = Schachbrett.brettStatus[sRow][sCol];
        String ZielFigur = Schachbrett.brettStatus[zRow][zCol];

        if (Figur == null) {
            return false;
        }

        switch (Figur) {

            case "wR", "bR":
                //Dieser if-Block überprüft, ob man versucht eine eigene FIgur zu schlagen. (Temporäre, unelegante Lösung. Eigentlich in eigene FUnktion schreiben statt für jede Figur zu wiederholen)
                if (ZielFigur != null) {
                char zielFarbe = ZielFigur.charAt(0);
                char startFarbe = Figur.charAt(0);
                if (zielFarbe == startFarbe) {
                return false;
                    }
                }

                if (sRow == zRow && sCol != zCol || sRow != zRow && sCol == zCol) {
                    return true;
                } else {
                    return false;
                }

            case "wN", "bN":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                if (zRow == sRow + 2 && zCol == sCol + 1 || zRow == sRow + 2 && zCol == sCol - 1 || zRow == sRow - 2 && zCol == sCol + 1 || zRow == sRow - 2 && zCol == sCol - 1 || zRow == sRow + 1 &&  zCol == sCol + 2 || zRow == sRow - 1 && zCol == sCol + 2 || zRow == sRow + 1 && zCol == sCol - 2 || zRow == sRow - 1 && zCol == sCol - 2) {
                    return true;
                } else {
                    return false;
                }

            case "wB", "bB":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                if (zRow == sRow + 1 &&  zCol == sCol + 1 || zRow == sRow + 2 && zCol == sCol + 2 || zRow == sRow + 3 && zCol == sCol + 3 || zRow == sRow + 4 && zCol == sCol + 4 || zRow == sRow + 5 && zCol == sCol + 5 || zRow == sRow + 6 && zCol == sCol + 6 || zRow == sRow + 7 && zCol == sCol + 7 || zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 2 && zCol == sCol - 2 || zRow == sRow - 3 && zCol == sCol - 3 || zRow == sRow - 4 && zCol == sCol - 4 || zRow == sRow - 5 && zCol == sCol - 5 || zRow == sRow - 6 && zCol == sCol - 6 || zRow == sRow - 7 && zCol == sCol - 7 || zRow == sRow + 1 &&  zCol == sCol - 1 || zRow == sRow + 2 && zCol == sCol - 2 || zRow == sRow + 3 && zCol == sCol - 3 || zRow == sRow + 4 && zCol == sCol - 4 || zRow == sRow + 5 && zCol == sCol - 5 || zRow == sRow + 6 && zCol == sCol - 6 || zRow == sRow + 7 && zCol == sCol - 7 || zRow == sRow - 1 && zCol == sCol + 1 || zRow == sRow - 2 && zCol == sCol + 2 || zRow == sRow - 3 && zCol == sCol + 3 || zRow == sRow - 4 && zCol == sCol + 4 || zRow == sRow - 5 && zCol == sCol + 5 || zRow == sRow - 6 && zCol == sCol + 6 || zRow == sRow - 7 && zCol == sCol + 7) {
                    return true;
                } else {
                    return false;
                }

            case "wQ", "bQ":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                if (sRow == zRow && sCol != zCol || sRow != zRow && sCol == zCol || zRow == sRow + 1 &&  zCol == sCol + 1 || zRow == sRow + 2 && zCol == sCol + 2 || zRow == sRow + 3 && zCol == sCol + 3 || zRow == sRow + 4 && zCol == sCol + 4 || zRow == sRow + 5 && zCol == sCol + 5 || zRow == sRow + 6 && zCol == sCol + 6 || zRow == sRow + 7 && zCol == sCol + 7 || zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 2 && zCol == sCol - 2 || zRow == sRow - 3 && zCol == sCol - 3 || zRow == sRow - 4 && zCol == sCol - 4 || zRow == sRow - 5 && zCol == sCol - 5 || zRow == sRow - 6 && zCol == sCol - 6 || zRow == sRow - 7 && zCol == sCol - 7 || zRow == sRow + 1 &&  zCol == sCol - 1 || zRow == sRow + 2 && zCol == sCol - 2 || zRow == sRow + 3 && zCol == sCol - 3 || zRow == sRow + 4 && zCol == sCol - 4 || zRow == sRow + 5 && zCol == sCol - 5 || zRow == sRow + 6 && zCol == sCol - 6 || zRow == sRow + 7 && zCol == sCol - 7 || zRow == sRow - 1 && zCol == sCol + 1 || zRow == sRow - 2 && zCol == sCol + 2 || zRow == sRow - 3 && zCol == sCol + 3 || zRow == sRow - 4 && zCol == sCol + 4 || zRow == sRow - 5 && zCol == sCol + 5 || zRow == sRow - 6 && zCol == sCol + 6 || zRow == sRow - 7 && zCol == sCol + 7){
                    return true;
                } else {
                    return false;
                }

            case "wK", "bK":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                    if (zRow == sRow + 1 && zCol == sCol || zRow == sRow - 1 && zCol == sCol || zCol == sCol + 1 && zRow == sRow || zCol == sCol - 1 && zRow == sRow || zRow == sRow + 1 && zCol == sCol - 1 || zRow == sRow + 1 && zCol == sCol + 1 ||  zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 1 && zCol == sCol + 1) {
                    return true;
                } else {
                    return false;
                }

            case "bP":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                    if (sRow == 1){ //Checken ob der Bauer noch nicht gezogen ist, falls nicht 2 Felder laufen ermöglichen

                        if (zRow == sRow + 1 && sCol == zCol || zRow == sRow + 2 && sCol == zCol) {
                            return true;
                        } else {
                            return false;
                        }

                    } else {

                        if (zRow == sRow + 1 && sCol == zCol) {
                            return true;
                        } else {
                            return false;
                        }
                    }

            case "wP":

                if (ZielFigur != null) {
                    char zielFarbe = ZielFigur.charAt(0);
                    char startFarbe = Figur.charAt(0);
                    if (zielFarbe == startFarbe) {
                        return false;
                    }
                }

                if (sRow == 6){ //Checken ob der Bauer noch nicht gezogen ist, falls nicht 2 Felder laufen ermöglichen
                    if (zRow == sRow - 1 && sCol == zCol || zRow == sRow - 2 && sCol == zCol) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (zRow == sRow - 1 && sCol == zCol) {
                        return true;
                    } else {
                        return false;
                    }
                }

        }
        return false;
    }
}

