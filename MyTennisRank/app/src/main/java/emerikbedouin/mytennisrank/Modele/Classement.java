package emerikbedouin.mytennisrank.modele;

import java.util.LinkedList;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Classement {


    /**
     * Calcul pour un profil le classement résultat
     * @param p
     * @param mode
     * @return
     */
    public static int calculClassement(Profil p, int mode){
        Joueur j = p.getJoueurProfil();
        LinkedList<Match> m = p.getMatchs();
        int classement = 0;
        if(m.size() > 0) {
            if (maintien(j.getClassement(), calculPointTotal(j.getClassement(), m, mode))) {
                classement = j.getClassement();
                classement++;
                while (maintien(classement, calculPointTotal(classement, m, mode))) {
                    classement++;
                }

                return classement - 1;
            } else {
                return j.getClassement() - 1;
            }
        }

        System.out.println("Classement"+classement);
        return classement-1;
    }

    /**
     * Calcul pour un classement et une liste de matchs le classement résultat
     * @param inputClassement le classement du joueur
     * @param matchs la liste de matchs
     * @param mode
     * @return
     */
    public static int calculClassement(int inputClassement, LinkedList<Match> matchs, int mode){

        int classement = inputClassement;

        if(matchs.size() > 0) {
            if (maintien(classement, calculPointTotal(classement, matchs, mode))) {
                classement++;
                while (maintien(classement, calculPointTotal(classement, matchs, mode))) {
                    classement++;
                }

                return classement - 1;
            } else {
                return classement - 1;
            }
        }

        return classement-1;
    }

    /**
     * Retourne le classement adverse pris en compte en fonction du mode
     * @param mode
     * @param adversaire
     * @return
     */
    public static int modeClassementAdversaire(int mode, Joueur adversaire){
        if(mode == 0){
            return adversaire.getClassement();
        }
        else if(mode == 1){
            return adversaire.getFuturClassement();
        }
        else if(mode == 2){
           return adversaire.getClassement() - 1;
        }
        else if(mode == 3){
            return adversaire.getClassement() + 1;
        }

        return 0;
    }

    /**
     * Récupère la liste des matchs à prendre en compte
     * @param classement
     * @param allMatchs
     * @param mode
     * @return
     */
    public static LinkedList<Match> getMatchsIntoAccount(int classement, LinkedList<Match> allMatchs, int mode){

        int nbrMatchPEC = calculNbrVictoirePEC(allMatchs, classement, mode);
        int adverClassement = 0;

        LinkedList<Match> matchIntoAccount = new LinkedList<>();

        LinkedList<Match> matchs = Match.sortDesc(allMatchs);

        for (int i=0; i < matchs.size() ; i++){

            adverClassement = modeClassementAdversaire(mode, matchs.get(i).getJ2());

            if( (adverClassement > classement ||  adverClassement >= classement-3)
                    && matchs.get(i).getGagnant().equals(matchs.get(i).getJ1()) && nbrMatchPEC > 0 && matchs.get(i).getWo() != 1){
                matchIntoAccount.add(matchs.get(i));
                nbrMatchPEC --;
            }
        }

        return matchIntoAccount;
    }

    /**
     * Calcul les points gagnés par bonus championnat
     * @param matchs
     * @return
     */
    public static int bonusChampionnat(LinkedList<Match> matchs){
        int nbrBonusChpt = 3;
        int pts = 0;
        for(int i=0;i<matchs.size();i++) {
            if (matchs.get(i).getBonusChpt() == 1 && nbrBonusChpt > 0 && matchs.get(i).getGagnant().equals(matchs.get(i).getJ1())) {
                pts += 15;
                nbrBonusChpt--;
            }
        }

        return pts;
    }

    /**
     * Calcul les points gagnés poru absence de défaite significative
     * @param matchs
     * @param classement
     * @param mode
     * @return
     */
    public static int bonusAbsenceDefaiteSignificative(LinkedList<Match> matchs, int classement, int mode){
        int pts = 0;
        int nbMatchs = 0;
        int adverClassement = 0;


        if(classement < 9 && classement > 4) pts=50;
        else if(classement < 13) pts=100;
        else pts = 150;

        for (int i=0; i < matchs.size() ; i++){

            adverClassement = modeClassementAdversaire(mode, matchs.get(i).getJ2());

            if (matchs.get(i).getWo() == 0) {
                if(matchs.get(i).getGagnant().equals(matchs.get(i).getJ2()) && adverClassement <= classement){
                    return 0;
                }

                nbMatchs++;
            }

        }
        if(nbMatchs > 4) return pts;
        else return 0;
    }


    /**
     * D�termine le delta VE2I5G qui entre en compte dans la determination du nombre de victoire � prendre en compte
     * @param matchs La liste des matchs du joueur
     * @param classement classement du joueur
     * @return
     */
    public static int calculVE2I5G(LinkedList<Match> matchs, int classement, int mode){
        int ptsTotal = 0;
        int adverClassement = 0;

        for (int i=0; i < matchs.size() ; i++){

            adverClassement = modeClassementAdversaire(mode, matchs.get(i).getJ2());

            if( matchs.get(i).getGagnant().equals(matchs.get(i).getJ1()) ){
                //Victoire
                ptsTotal += 1;
            }
            else{
                //Defaite
                if( adverClassement == classement ){
                    ptsTotal -= 1;
                }
                else if( adverClassement == classement-1 ){
                    ptsTotal -= 2;
                }
                else if( adverClassement < classement ){
                    ptsTotal -= 5;
                }
            }
        }

        return ptsTotal;
    }

    /**
     * D�termine le nombre de victoire � prendre en compte
     * @param match La liste des matchs du joueur
     * @param classement classement du joueur
     * @return
     */
    public static int calculNbrVictoireDelta(LinkedList<Match> match, int classement, int mode){

        // Calcul du delta
        int ptsDelta = calculVE2I5G(match, classement, mode);
        int nbrVictoire = 0;

        if(classement <= 6){
            // 4eme serie

            if(ptsDelta >= 0 && ptsDelta <= 4) nbrVictoire += 1;
            else if(ptsDelta >= 5 && ptsDelta <= 9) nbrVictoire += 2;
            else if(ptsDelta >= 10 && ptsDelta <= 14) nbrVictoire += 3;
            else if(ptsDelta >= 15 && ptsDelta <= 19) nbrVictoire += 4;
            else if(ptsDelta >= 20 && ptsDelta <= 24) nbrVictoire += 5;
            else if(ptsDelta > 24) nbrVictoire += 6;
        }
        else if(classement >= 7 && classement < 13){
            // 3eme serie

            if(ptsDelta >= 0 && ptsDelta <= 7) nbrVictoire += 1;
            else if(ptsDelta >= 8 && ptsDelta <= 14) nbrVictoire += 2;
            else if(ptsDelta >= 15 && ptsDelta <= 22) nbrVictoire += 3;
            else if(ptsDelta >= 23 && ptsDelta <= 29) nbrVictoire += 4;
            else if(ptsDelta >= 30 && ptsDelta <= 39) nbrVictoire += 5;
            else if(ptsDelta > 39) nbrVictoire += 6;
        }
        else if(classement >= 13 && classement < 20){
            // 2eme serie positive
            if(ptsDelta < -40) nbrVictoire -= 3;
            else if(ptsDelta <= -31 && ptsDelta >= -40) nbrVictoire -= 2;
            else if(ptsDelta <= -21 && ptsDelta >= -30) nbrVictoire -= 1;
            else if(ptsDelta <= -1 && ptsDelta >= -20) nbrVictoire -= 0;
            else if(ptsDelta >= 0 && ptsDelta <= 7) nbrVictoire += 1;
            else if(ptsDelta >= 8 && ptsDelta <= 14) nbrVictoire += 2;
            else if(ptsDelta >= 15 && ptsDelta <= 22) nbrVictoire += 3;
            else if(ptsDelta >= 23 && ptsDelta <= 29) nbrVictoire += 4;
            else if(ptsDelta >= 30 && ptsDelta <= 39) nbrVictoire += 5;
            else if(ptsDelta > 39) nbrVictoire += 6;
        }
        else if(classement >= 20){
            // 2eme serie négative
            if(ptsDelta < -80) nbrVictoire -= 5;
            else if(ptsDelta <= -61 && ptsDelta >= -80) nbrVictoire -= 4;
            else if(ptsDelta <= -41 && ptsDelta >= -60) nbrVictoire -= 3;
            else if(ptsDelta <= -31 && ptsDelta >= -40) nbrVictoire -= 2;
            else if(ptsDelta <= -21 && ptsDelta >= -30) nbrVictoire -= 1;
            else if(ptsDelta <= -1 && ptsDelta >= -20) nbrVictoire -= 0;
            else if(ptsDelta >= 0 && ptsDelta <= 9) nbrVictoire += 1;
            else if(ptsDelta >= 10 && ptsDelta <= 19) nbrVictoire += 2;
            else if(ptsDelta >= 20 && ptsDelta <= 24) nbrVictoire += 3;
            else if(ptsDelta >= 25 && ptsDelta <= 29) nbrVictoire += 4;
            else if(ptsDelta >= 30 && ptsDelta <= 34) nbrVictoire += 5;
            else if(ptsDelta >= 35 && ptsDelta <= 44) nbrVictoire += 6;
            else if(ptsDelta > 44) nbrVictoire += 7;
        }

        return nbrVictoire;
    }

    /**
     * D�termine le nombre de victoire � prendre en compte
     * @param match La liste des matchs du joueur
     * @param classement classement du joueur
     * @return
     */
    public static int calculNbrVictoirePEC(LinkedList<Match> match, int classement, int mode){

        int nbrVictoirePEC = 0;
        if(classement <= 6){
            // 4eme serie
            nbrVictoirePEC = 6;
        }
        else if(classement >= 7 && classement < 13){
            // 3eme serie
            nbrVictoirePEC = 8;
        }
        else if(classement >= 13 && classement < 20){
            // 2eme serie positive
            if(classement < 16) nbrVictoirePEC = 9;
            else if(classement == 16 || classement == 17) nbrVictoirePEC = 10;
            else if(classement == 18) nbrVictoirePEC = 11;
            else if(classement == 19) nbrVictoirePEC = 12;
        }
        else if(classement >= 20){
            // 2eme serie négative
            if(classement < 20) nbrVictoirePEC = 15;
            else if(classement == 21) nbrVictoirePEC = 17;
            else if(classement == 22) nbrVictoirePEC = 19;
            else if(classement == 23) nbrVictoirePEC = 20; // top 100-60
            else if(classement == 24) nbrVictoirePEC = 22; // top 60-40
        }



        return nbrVictoirePEC += calculNbrVictoireDelta(match, classement, mode);
    }

    /**
     * Recupere les bonus pour absence de défaite significative et de championnat
     * @param allMatchs
     * @param classement
     * @param mode
     * @return
     */
    public static int calculBonus(LinkedList allMatchs, int classement, int mode){
        int points = 0;
        // Bonus championnat et pour absence de défaite significative
        points += bonusChampionnat(allMatchs);
        points += bonusAbsenceDefaiteSignificative(allMatchs, classement, mode);

        return points;
    }


    /**
     * Determine si le nombre de points passe en parametre est suffisant pour le classement donne
     * @param classement
     * @param pts
     * @return
     */
    public static boolean maintien(int classement, int pts) {

        if (ptsMaintien(classement) <= pts) return true ;

        return false;

    }


    /**
     * Calcul des points total, des matchs et des bonus
     * @param classement
     * @param allMatchs
     * @param mode
     * @return
     */
    public static int calculPointTotal(int classement, LinkedList<Match> allMatchs, int mode){
        int points = 0;

        // Bonus championnat et pour absence de défaite significative
        points += calculBonus(allMatchs, classement, mode);

        points += calculPointMatchs(classement, allMatchs, mode);

        return points;
    }

    /** Fonction qui calcul la somme des points pour une liste de matchs pour un classement donn�
     * @param classement Le classement pour le calcul
     * @param allMatchs La liste des matchs du joueur
     * @param mode Le mode calcul pour le classement adverse (0 : eq , 1 : sup , 2 : inf)
     * @return Le nombre de points
     */
    public static int calculPointMatchs(int classement, LinkedList<Match> allMatchs, int mode){
        int points=0;
        int adverClassement=0;

        LinkedList<Match> matchs = getMatchsIntoAccount(classement, allMatchs, mode);

        for(int i=0;i<matchs.size();i++){
            if( matchs.get(i).getGagnant().equals(matchs.get(i).getJ1()) ){

                adverClassement = modeClassementAdversaire(mode, matchs.get(i).getJ2());

                points += ptsMatch(classement, adverClassement);

            }


        }
        //points=(getPts()+points);
        System.out.println("Nombre de points à "+convertirClassementInt(classement)+" = "+points);
        return points;
    }


    /**
     * Retourne le nombre de points gagnés en fonction du classement du joueur et de son adversaire
     * @param classement
     * @param adverClassement
     * @return
     */
    public static int ptsMatch( int classement, int adverClassement){

        int points = 0;

        if(adverClassement > classement+1){
            points=(points+120);
        }
        else if((adverClassement>classement) && (adverClassement<=classement+1)){
            points=(points+90);
        }
        else if(adverClassement==classement){
            points=(points+60);
        }
        else if(adverClassement==classement-1){
            points=(points+30);
        }
        else if(adverClassement==classement-2){
            points=(points+20);
        }
        else if(adverClassement==classement-3){
            points=(points+15);
        }

        return points;
    }

    /** Retourne le nombre de points requis pour le maintien � un classement donn�
     * @param classement
     * @return
     */
    public static int ptsMaintien(int classement){
        int pts = 0;

        if(classement==0){
            pts = 0;}
        else if(classement==1){
            pts = 0;}
        else if(classement==2){
            pts = 6;}
        else if(classement==3){
            pts = 70;}
        else if(classement==4){
            pts = 120;}
        else if(classement==5){
            pts = 170;}
        else if(classement==6){
            // 30/1
            pts = 210;}
        else if(classement==7){
            pts = 280;}
        else if(classement==8){
            pts = 300;}
        else if(classement==9){
            pts = 310;}
        else if(classement==10){
            pts = 320;}
        else if(classement==11){
            pts = 340;}
        else if(classement==12){
            // 15/1
            pts = 370;}
        else if(classement==13){
            pts = 430;}
        else if(classement==14){
            pts = 430;}

        return pts;
    }

    /** Convertit en chaine de caractere un classement en integer
     * @param classementInt
     * @return
     */
    public static String convertirClassementInt(int classementInt){
        String classement="";
        if(classementInt==0)
            classement="NC";
        else if(classementInt==1)
            classement="40";
        else if(classementInt==2)
            classement="30/5";
        else if(classementInt==3)
            classement="30/4";
        else if(classementInt==4)
            classement="30/3";
        else if(classementInt==5)
            classement="30/2";
        else if(classementInt==6)
            classement="30/1";
        else if(classementInt==7)
            classement="30";
        else if(classementInt==8)
            classement="15/5";
        else if(classementInt==9)
            classement="15/4";
        else if(classementInt==10)
            classement="15/3";
        else if(classementInt==11)
            classement="15/2";
        else if(classementInt==12)
            classement="15/1";
        else if(classementInt==13)
            classement="15";
        else if(classementInt==14)
            classement="5/6";
        else if(classementInt==15)
            classement="4/6";
        else if(classementInt==16)
            classement="3/6";
        else if(classementInt==17)
            classement="2/6";
        else if(classementInt==18)
            classement="1/6";
        else if(classementInt==19)
            classement="0";

        return classement;
    }
    /**Convertit en int un classement en String
     * @param c
     * @return
     */
    public static int convertirClassementString(String c){
        int classement = 0;

        if("NC".equals(c))
            classement=0;
       else if("40".equals(c))
            classement=1;
       else if("30/5".equals(c))
            classement=2;
       else if("30/4".equals(c))
            classement=3;
       else if("30/3".equals(c))
            classement=4;
        else if("30/2".equals(c))
            classement=5;
        else if("30/1".equals(c))
            classement=6;
        else if("30".equals(c))
            classement=7;
        else if("15/5".equals(c))
            classement=8;
        else if("15/4".equals(c))
            classement=9;
        else if("15/3".equals(c))
            classement=10;
        else if("15/2".equals(c))
            classement=11;
        else if("15/1".equals(c))
            classement=12;
        else if("15".equals(c))
            classement=13;
        else if("5/6".equals(c))
            classement=14;
        else if("4/6".equals(c))
            classement=15;
        else if("3/6".equals(c))
            classement=16;
        else if("2/6".equals(c))
            classement=17;
        else if("1/6".equals(c))
            classement=18;
        else if("0".equals(c))
            classement=19;

        return classement;
    }

    /**
     * Retourne la liste des classements sous forme de String
     * @return liste des classements
     */
    public static LinkedList<String> getClassements(){
        LinkedList<String> listClassements = new LinkedList<>();

        listClassements.add("NC");
        listClassements.add("40");
        listClassements.add("30/5");
        listClassements.add("30/4");
        listClassements.add("30/3");
        listClassements.add("30/2");
        listClassements.add("30/1");
        listClassements.add("30");
        listClassements.add("15/5");
        listClassements.add("15/4");
        listClassements.add("15/3");
        listClassements.add("15/2");
        listClassements.add("15/1");
        listClassements.add("15");
        listClassements.add("5/6");
        listClassements.add("4/6");
        listClassements.add("3/6");
        listClassements.add("2/6");
        listClassements.add("1/6");
        listClassements.add("0");


        return listClassements;

    }


}
