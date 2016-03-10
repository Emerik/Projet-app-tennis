package emerikbedouin.mytennisrank.Modele;

/**
 * Created by emerikbedouin on 10/03/16.
 */

public class Epreuve {

    private int numEpreuve;
    private String nomEpreuve;
    private int type;

    public Epreuve(){

    }

    public Epreuve(int numEpreuve, String nomEpreuve, int type) {
        this.numEpreuve = numEpreuve;
        this.nomEpreuve = nomEpreuve;
        this.type = type;
    }

    public int getNumEpreuve() {
        return numEpreuve;
    }

    public void setNumEpreuve(int numEpreuve) {
        this.numEpreuve = numEpreuve;
    }

    public String getNomEpreuve() {
        return nomEpreuve;
    }

    public void setNomEpreuve(String nomEpreuve) {
        this.nomEpreuve = nomEpreuve;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
