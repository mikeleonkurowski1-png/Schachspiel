package schach;

public class Historie {

    private String[][] zurückState = new String[8][8];
    private String[][] vorState = new String[8][8];

    public String[][] getZurückState(String[][] brettStatus){

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                zurückState[i][j] = Schachbrett.brettStatus[i][j];
            }
        }
        return zurückState;
    }

    public String[][] getVorState(String[][] brettStatus){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                vorState[i][j] = Schachbrett.brettStatus[i][j];
            }
        }
        return vorState;
    }

    public String[][] undo() {
        return zurückState;
    }

    public String[][] redo() {
        return vorState;
    }


}
