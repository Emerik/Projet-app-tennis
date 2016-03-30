package emerikbedouin.mytennisrank.Modele;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Match implements Parcelable{

    private Joueur j1;
    private Joueur j2;
    private String score;
    private String surface;
    private int resultat;
    private Epreuve epreuve;

    public Match(Joueur j1, Joueur j2, String score, String surface, int resultat, Epreuve epreuve) {
        this.j1 = j1;
        this.j2 = j2;
        this.score = score;
        this.surface = surface;
        this.resultat = resultat;
        this.epreuve = epreuve;
    }

    public  Match(){
        this.j1 = new Joueur();
        this.j2 = new Joueur();
        this.score = "";
        this.surface = "";
        this.resultat = -1;
        this.epreuve = new Epreuve();
    }

    public Joueur getGagnant(){
        if ( resultat==1 ){
            return j1;
        }
        else{
            return j2;
        }

    }

    public String toString(){
        return j1.getNom()+" vs "+j2.getNom()+" score :"+score;
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

    public Epreuve getEpreuve() {
        return epreuve;
    }

    public void setEpreuve(Epreuve epreuve) {
        this.epreuve = epreuve;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(j1,flags);
        dest.writeParcelable(j2,flags);
        dest.writeString(score);
        dest.writeString(surface);
        dest.writeInt(resultat);
        dest.writeParcelable(epreuve,flags);
    }

    public static final Parcelable.Creator<Match> CREATOR = new Parcelable.Creator<Match>()
    {
        @Override
        public Match createFromParcel(Parcel source)
        {
            return new Match(source);
        }

        @Override
        public Match[] newArray(int size)
        {
            return new Match[size];
        }
    };

    public Match(Parcel in) {
        this.j1 = in.readParcelable(Joueur.class.getClassLoader());
        this.j2 = in.readParcelable(Joueur.class.getClassLoader());
        this.score = in.readString();
        this.surface = in.readString();
        this.resultat = in.readInt();
        this.epreuve = in.readParcelable(Epreuve.class.getClassLoader());
    }

}
