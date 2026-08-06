package schach;

public class Historie {

    private String[][] zurückState = new String[8][8];
    private String[][] vorState = new String[8][8];

    private boolean zWK, zBK, zWR, zBR;

    public void getZurückState(String[][] brettStatus){

        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                zurückState[i][j] = Schachbrett.brettStatus[i][j];
            }
            zWK = FigurenLogik.WKbewegt;
            zBK = FigurenLogik.BKbewegt;
            zWR = FigurenLogik.WRbewegt;
            zBR = FigurenLogik.BRbewegt;
        }
    }

    public void getVorState(String[][] brettStatus){
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++){
                vorState[i][j] = Schachbrett.brettStatus[i][j];
            }
        }
    }

    public String[][] undo() {
        FigurenLogik.WKbewegt = zWK;
        FigurenLogik.BKbewegt = zBK;
        FigurenLogik.WRbewegt = zWR;
        FigurenLogik.BRbewegt = zBR;

        String[][] kopie = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                kopie[i][j] = zurückState[i][j];
            }
        }
        return kopie;
    }

    public String[][] redo() {
        String[][] kopie = new String[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                kopie[i][j] = vorState[i][j];
            }
        }
        return kopie;
    }
    }

