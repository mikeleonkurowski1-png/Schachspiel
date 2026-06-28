package schach;


public class FIgurenLogik {
    private boolean ZugErlaubnis(int sRow,int sCol, int zRow, int zCol) {
        String Figur = Schachbrett.brettStatus[sRow][sCol];

        if (Figur == null) {
            return false;
        }

        switch (Figur) {
            case "wR":
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
