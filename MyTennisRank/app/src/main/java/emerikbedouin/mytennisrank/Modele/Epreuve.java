package emerikbedouin.mytennisrank.Modele;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by emerikbedouin on 10/03/16.
 */

public class Epreuve implements Parcelable, Serializable{

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Epreuve)) return false;

        Epreuve epreuve = (Epreuve) o;

        if (getNumEpreuve() != epreuve.getNumEpreuve()) return false;
        if (getType() != epreuve.getType()) return false;
        return !(getNomEpreuve() != null ? !getNomEpreuve().equals(epreuve.getNomEpreuve()) : epreuve.getNomEpreuve() != null);

    }

    @Override
    public int hashCode() {
        int result = getNumEpreuve();
        result = 31 * result + (getNomEpreuve() != null ? getNomEpreuve().hashCode() : 0);
        result = 31 * result + getType();
        return result;
    }

    // Fonction Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(numEpreuve);
        dest.writeString(nomEpreuve);
        dest.writeInt(type);
    }

    public static final Parcelable.Creator<Epreuve> CREATOR = new Parcelable.Creator<Epreuve>()
    {
        @Override
        public Epreuve createFromParcel(Parcel source)
        {
            return new Epreuve(source);
        }

        @Override
        public Epreuve[] newArray(int size)
        {
            return new Epreuve[size];
        }
    };

    public Epreuve(Parcel in) {
        this.numEpreuve= in.readInt();
        this.nomEpreuve = in.readString();
        this.type = in.readInt();

    }
}
