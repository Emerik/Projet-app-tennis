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

    // Equals & HashCode

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Match)) return false;

        Match match = (Match) o;

        if (getResultat() != match.getResultat()) return false;
        if (getJ1() != null ? !getJ1().equals(match.getJ1()) : match.getJ1() != null) return false;
        if (getJ2() != null ? !getJ2().equals(match.getJ2()) : match.getJ2() != null) return false;
        if (getScore() != null ? !getScore().equals(match.getScore()) : match.getScore() != null)
            return false;
        if (getSurface() != null ? !getSurface().equals(match.getSurface()) : match.getSurface() != null)
            return false;
        return !(getEpreuve() != null ? !getEpreuve().equals(match.getEpreuve()) : match.getEpreuve() != null);

    }

    @Override
    public int hashCode() {
        int result = getJ1() != null ? getJ1().hashCode() : 0;
        result = 31 * result + (getJ2() != null ? getJ2().hashCode() : 0);
        result = 31 * result + (getScore() != null ? getScore().hashCode() : 0);
        result = 31 * result + (getSurface() != null ? getSurface().hashCode() : 0);
        result = 31 * result + getResultat();
        result = 31 * result + (getEpreuve() != null ? getEpreuve().hashCode() : 0);
        return result;
    }


    // Parcelable function

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
