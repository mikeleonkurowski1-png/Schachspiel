package schach;

public class Zug {
    public int startRow;
    public int startCol;
    public int endRow;
    public int endCol;

    public Zug(int startRow, int startCol, int endRow, int endCol) {
        this.startRow = startRow;
        this.startCol = startCol;
        this.endRow = endRow;
        this.endCol = endCol;
    }
}
