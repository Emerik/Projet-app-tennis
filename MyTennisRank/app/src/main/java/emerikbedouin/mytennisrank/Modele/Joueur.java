package emerikbedouin.mytennisrank.Modele;

import java.util.LinkedList;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Joueur {

    protected int numJoueur;
    protected String nom;
    protected int classement;
    private int futurClassement;
    protected int victoire;
    protected int pts;
    protected LinkedList<Match> match;
    private String materiel;

    public Joueur(int numJ, String n, int classActuel, int classFutur, int pts){
        this.numJoueur = numJ;
        this.nom = n;
        this.classement = classActuel;
        this.futurClassement = classFutur;
        this.pts = pts;
        this.victoire = 0;
        this.match = new LinkedList();
    }

    public Joueur(){
        nom="ND";
        classement=0;
        victoire=0;
        pts=0;
        this.match = new LinkedList();
        materiel = "";
        futurClassement = 0;
    }

    public void addMatch(Match m){
        match.add(m);
    }

    public String toString() {
			/*return "Joueur [nom=" + nom + ", classement=" + Classement.convertirClassementInt(classement)
					+ ", victoire=" + victoire + ", pts=" + pts + "\n"
					+ match + "]";*/
        return nom;
    }

    // Getters & Setters ------------------------------------------------
    public int getNumJoueur(){
        return this.numJoueur;
    }
    public void setNumJoueur(int numj){
        this.numJoueur = numj;
    }
    public LinkedList<Match> getMatch() {
        return match;
    }
    public void setMatch(LinkedList<Match> match) {
        this.match = match;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }
    public int getClassement() {
        return classement;
    }
    public void setClassement(int classement) {
        this.classement = classement;
    }
    public int getVictoire(){
        return victoire;
    }
    public int getPts() {
        return pts;
    }
    public void setPts(int pts) {
        this.pts = pts;
    }
    public void setVictoire(int v){
        victoire = v;
    }
    public String getMateriel() {
        return materiel;
    }
    public void setMateriel(String materiel) {
        this.materiel = materiel;
    }
    public int getFuturClassement() {
        return futurClassement;
    }
    public void setFuturClassement(int objectifClassement) {
        this.futurClassement = objectifClassement;
    }
}
