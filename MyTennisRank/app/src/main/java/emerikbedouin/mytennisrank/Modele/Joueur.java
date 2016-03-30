package emerikbedouin.mytennisrank.Modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Joueur implements Parcelable {

    protected int numJoueur;
    protected String nom;
    protected int classement;
    private int futurClassement;
    protected int pts;
    private String materiel;

    public Joueur(int numJ, String n, int classActuel, int classFutur, int pts){
        this.numJoueur = numJ;
        this.nom = n;
        this.classement = classActuel;
        this.futurClassement = classFutur;
        this.pts = pts;

    }

    public Joueur(){
        nom="ND";
        classement=0;
        pts=0;
        materiel = "";
        futurClassement = 0;
    }


    public String toString() {
			/*return "Joueur [nom=" + nom + ", classement=" + Classement.convertirClassementInt(classement)
					+ ", victoire=" + victoire + ", pts=" + pts + "\n"
					+ match + "]";*/
        return nom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Joueur joueur = (Joueur) o;

        if (numJoueur != joueur.numJoueur) return false;
        if (classement != joueur.classement) return false;
        if (futurClassement != joueur.futurClassement) return false;
        if (pts != joueur.pts) return false;
        if (nom != null ? !nom.equals(joueur.nom) : joueur.nom != null) return false;
        return !(materiel != null ? !materiel.equals(joueur.materiel) : joueur.materiel != null);

    }

    @Override
    public int hashCode() {
        int result = numJoueur;
        result = 31 * result + (nom != null ? nom.hashCode() : 0);
        result = 31 * result + classement;
        result = 31 * result + futurClassement;
        result = 31 * result + pts;
        result = 31 * result + (materiel != null ? materiel.hashCode() : 0);
        return result;
    }

    // Getters & Setters ------------------------------------------------
    public int getNumJoueur(){
        return this.numJoueur;
    }
    public void setNumJoueur(int numj){
        this.numJoueur = numj;
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
    public int getPts() {
        return pts;
    }
    public void setPts(int pts) {
        this.pts = pts;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numJoueur);
        dest.writeString(nom);
        dest.writeInt(classement);
        dest.writeInt(futurClassement);
        dest.writeInt(pts);
        dest.writeString(materiel);
    }

    public static final Parcelable.Creator<Joueur> CREATOR = new Parcelable.Creator<Joueur>()
    {
        @Override
        public Joueur createFromParcel(Parcel source)
        {
            return new Joueur(source);
        }

        @Override
        public Joueur[] newArray(int size)
        {
            return new Joueur[size];
        }
    };

    public Joueur(Parcel in) {
        this.numJoueur = in.readInt();
        this.nom = in.readString();
        this.classement = in.readInt();
        this.futurClassement = in.readInt();
        this.pts = in.readInt();
        this.materiel = in.readString();
    }
}
