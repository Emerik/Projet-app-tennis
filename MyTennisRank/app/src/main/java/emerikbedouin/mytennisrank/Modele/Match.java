package emerikbedouin.mytennisrank.Modele;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Match {

    private Joueur j1;
    private Joueur j2;
    private String score;
    private String surface;
    private int resultat;

    public Match(Joueur j1, Joueur j2, String score, String surface, int resultat) {
        this.j1 = j1;
        this.j2 = j2;
        this.score = score;
        this.surface = surface;
        this.resultat = resultat;
    }

    public Joueur getGagnant(){
        if ( resultat==1 ){
            return j1;
        }
        else{
            return j2;
        }

    }

    // Getters & Setters

    public Joueur getJ1() {
        return j1;
    }

    public void setJ1(Joueur j1) {
        this.j1 = j1;
    }

    public Joueur getJ2() {
        return j2;
    }

    public void setJ2(Joueur j2) {
        this.j2 = j2;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public int getResultat() {
        return resultat;
    }

    public void setResultat(int resultat) {
        this.resultat = resultat;
    }
}
