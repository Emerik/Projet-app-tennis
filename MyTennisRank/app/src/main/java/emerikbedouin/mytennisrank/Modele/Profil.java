package emerikbedouin.mytennisrank.Modele;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Profil {

    private int id_profil;
    private String nom;
    private Joueur joueurProfil;

    public Profil() {

    }

    public Profil(int idp, String nom, Joueur j) {
        this.id_profil = idp;
        this.nom = nom;
        this.joueurProfil = j;
    }

    // Getters & setters
    public int getId_profil() {
        return id_profil;
    }
    public void setId_profil(int id_profil) {
        this.id_profil = id_profil;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public Joueur getJoueurProfil() {
        return joueurProfil;
    }

    public void setJoueurProfil(Joueur joueurProfil) {
        this.joueurProfil = joueurProfil;
    }

    @Override
    public String toString() {
        return nom;
    }

}
