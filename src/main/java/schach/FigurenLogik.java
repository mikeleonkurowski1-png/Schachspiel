package schach;


public class FigurenLogik {
    public boolean ZugErlaubnis(int sRow,int sCol, int zRow, int zCol) {
        String Figur = Schachbrett.brettStatus[sRow][sCol];

        if (Figur == null) {
            return false;
        }

        switch (Figur) {
            case "wR", "bR":
                if (sRow == zRow && sCol != zCol || sRow != zRow && sCol == zCol) {
                    return true;
                } else {
                    return false;
                }
            case "wN", "bN":
            case "wB", "bB":
            case "wQ", "bQ":


            case "wK", "bK":

                    if (zRow == sRow + 1 && zCol == sCol || zRow == sRow - 1 && zCol == sCol || zCol == sCol + 1 && zRow == sRow || zCol == sCol - 1 && zRow == sRow || zRow == sRow + 1 && zCol == sCol - 1 || zRow == sRow + 1 && zCol == sCol + 1 ||  zRow == sRow - 1 && zCol == sCol - 1 || zRow == sRow - 1 && zCol == sCol + 1) {
                    return true;
                } else {
                    return false;
                }

            case "bP":

                    if (zRow == sRow + 1 && sCol == zCol){
                    return true;
                } else  {
                    return false;
                }

            case "wP":

                        if (zRow == sRow - 1 && sCol == zCol){
                    return true;
                } else  {
                    return false;
                }

        }
        return false;
    }
}

