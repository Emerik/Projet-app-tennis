package emerikbedouin.mytennisrank.modele;

import android.os.Parcel;
import android.os.Parcelable;
import java.io.Serializable;
import java.util.LinkedList;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 10/03/16.
 */
public class Profil implements Parcelable, Serializable{

    private int id_profil;
    private String nom;
    private Joueur joueurProfil;
    private LinkedList<Match> matchs;

    public Profil() {
        id_profil = 0;
        nom = "";
        joueurProfil = new Joueur();
        matchs = new LinkedList<>();
    }

    public Profil(int idp, String nom, Joueur j) {
        this.id_profil = idp;
        this.nom = nom;
        this.joueurProfil = j;
        matchs = new LinkedList<Match>();
    }

    public void addMatch(Match m){
        matchs.add(m);
    }

    public int getNbreVictoire(){
        int nbr = 0;
        for (int i=0 ; i < matchs.size() ; i++){
            if (matchs.get(i).getGagnant().equals(joueurProfil) ){
                nbr++;
            }
        }

        return nbr;
    }

    public int getNbreDefaite(){
        int nbr = 0;
        for (int i=0 ; i < matchs.size() ; i++){
            if ( !matchs.get(i).getGagnant().equals(joueurProfil) ){
                nbr++;
            }
        }

        return nbr;
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
    public LinkedList<Match> getMatchs() {
        return matchs;
    }
    public void setMatchs(LinkedList<Match> matchs) {
        this.matchs = matchs;
    }

    @Override
    public String toString() {
        return nom;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_profil);
        dest.writeString(nom);
        dest.writeParcelable(joueurProfil, flags);
        dest.writeTypedList(matchs);
    }

    public static final Parcelable.Creator<Profil> CREATOR = new Parcelable.Creator<Profil>()
    {
        @Override
        public Profil createFromParcel(Parcel source)
        {
            return new Profil(source);
        }

        @Override
        public Profil[] newArray(int size)
        {
            return new Profil[size];
        }
    };

    public Profil(Parcel in) {
        this.id_profil = in.readInt();
        this.nom = in.readString();
        this.joueurProfil = in.readParcelable(Joueur.class.getClassLoader());
        this.matchs = new LinkedList<>();
        in.readTypedList(matchs, Match.CREATOR);

    }
}
