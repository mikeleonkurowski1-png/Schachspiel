package schach;


public class FigurenLogik {
    public boolean ZugErlaubnis(int sRow,int sCol, int zRow, int zCol) {
        String Figur = Schachbrett.brettStatus[sRow][sCol];

        if (Figur == null) {
            return false;
        }

        switch (Figur) {
            case "wR":
                if (sRow == zRow && sCol != zCol || sRow != zRow && sCol == zCol) {
                    return true;
                } else {
                    return false;
                }
            case "wN":
            case "wB":
            case "wQ":
            case "wK":
            case "wP":
            case "bR":
            case "bN":
            case "bQ":
            case "bK":
            case "bP":
            case "bB":
        }
        return false;
    }
}
