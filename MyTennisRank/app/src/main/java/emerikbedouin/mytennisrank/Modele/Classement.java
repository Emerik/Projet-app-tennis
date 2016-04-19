package emerikbedouin.mytennisrank.Modele;

import java.util.LinkedList;

/**
 * Created by emerikbedouin on 10/03/16.
 */
public class Classement {




    public static int calculClassement(Profil p){
        Joueur j = p.getJoueurProfil();
        LinkedList<Match> m = p.getMatchs();
        int classement = 0;
        if(m.size() > 0) {
            if (maintien(j.getClassement(), calculPoint(j.getClassement(), m, 0))) {
                classement = j.getClassement();
                classement++;
                while (maintien(classement, calculPoint(classement, m, 0))) {
                    classement++;
                }

                return classement - 1;
            } else {
                return j.getClassement() - 1;
            }
        }

        return classement-1;
    }

    public static int calculClassement(int classement, LinkedList<Match> matchs){

        if(matchs.size() > 0) {
            if (maintien(classement, calculPoint(classement, matchs, 0))) {
                classement++;
                while (maintien(classement, calculPoint(classement, matchs, 0))) {
                    classement++;
                }

                return classement - 1;
            } else {
                return classement - 1;
            }
        }

        return classement-1;
    }


    public static LinkedList<Match> getMatchsIntoAccount(int classement, LinkedList<Match> matchs){
        LinkedList<Match> matchIntoAccount = new LinkedList<>();

        for (int i=0; i < matchs.size() ; i++){
            if( (matchs.get(i).getJ2().getClassement() > classement ||  matchs.get(i).getJ2().getClassement() >= classement-3)
                    && matchs.get(i).getGagnant().equals(matchs.get(i).getJ1())){
                matchIntoAccount.add(matchs.get(i));
            }
        }

        return matchIntoAccount;
    }

    /**
     * Détermine si le nombre de points passé en paramètre est suffisant pour le classement donné
     * @param classement
     * @param pts
     * @return
     */
    public static boolean maintien(int classement, int pts) {
        if (ptsMaintien(classement) <= pts){
            return true;
        }
        else{
            return false;
        }
    }

    /** Fonction qui calcul la somme des points pour une liste de matchs pour un classement donné
     * @param classement Le classement pour le calcul
     * @param match La liste des matchs
     * @param mode Le mode calcul pour le classement adverse (0 : eq , 1 : sup , 2 : inf)
     * @return Le nombre de points
     */
    public static int calculPoint(int classement, LinkedList<Match> match, int mode){
        int points=0;
        int adverClassement=0;

        for(int i=0;i<match.size();i++){
            if( match.get(i).getGagnant().equals(match.get(i).getJ1()) ){
                if(mode == 0){
                    adverClassement = match.get(i).getJ2().getClassement();
                }
                else if(mode == 1){
                    adverClassement = match.get(i).getJ2().getFuturClassement();
                }
                else if(mode == 2){
                    adverClassement = match.get(i).getJ2().getClassement() - 1;
                }

                //System.out.println("Mode"+mode+"Classement "+match.get(i).getJ2().getNom()+" futur:"+adverClassement);

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
            }
        }
        //points=(getPts()+points);
        System.out.println("Nombre de points à "+convertirClassementInt(classement)+" = "+points);
        return points;
    }

    /** Retourne le nombre de points requis pour le maintien à un classement donné
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
        if(classementInt==1)
            classement="40";
        if(classementInt==2)
            classement="30/5";
        if(classementInt==3)
            classement="30/4";
        if(classementInt==4)
            classement="30/3";
        if(classementInt==5)
            classement="30/2";
        if(classementInt==6)
            classement="30/1";
        if(classementInt==7)
            classement="30";
        if(classementInt==8)
            classement="15/5";
        if(classementInt==9)
            classement="15/4";
        if(classementInt==10)
            classement="15/3";
        if(classementInt==11)
            classement="15/2";
        if(classementInt==12)
            classement="15/1";
        if(classementInt==13)
            classement="15";
        if(classementInt==14)
            classement="5/6";
        if(classementInt==15)
            classement="4/6";
        if(classementInt==16)
            classement="3/6";

        return classement;
    }
    /**Convertit en int un classement en String
     * @param c
     * @return
     */
    public static int convertirClassementString(String c){
        int classement = 0;
        if(c.equals("NC"))
            classement=0;
        if(c.equals("40"))
            classement=1;
        if(c.equals("30/5"))
            classement=2;
        if(c.equals("30/4"))
            classement=3;
        if(c.equals("30/3"))
            classement=4;
        if(c.equals("30/2"))
            classement=5;
        if(c.equals("30/1"))
            classement=6;
        if(c.equals("30"))
            classement=7;
        if(c.equals("15/5"))
            classement=8;
        if(c.equals("15/4"))
            classement=9;
        if(c.equals("15/3"))
            classement=10;
        if(c.equals("15/2"))
            classement=11;
        if(c.equals("15/1"))
            classement=12;
        if(c.equals("15"))
            classement=13;
        if(c.equals("5/6"))
            classement=14;
        if(c.equals("4/6"))
            classement=15;
        if(c.equals("3/6"))
            classement=16;

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


        return listClassements;

    }


    @Deprecated
    /** Détermine si le joueur se maintien pour un classement et un nombre de pts donné
     * @param classement
     * @param pts
     * @return
     */
    public static boolean maintienOld(int classement, int pts){
        int m=0;
        //Pour chaque classement
        if(classement==0){
            return true;}
        else if(classement==1){
            if(pts>0)
                return true;}
        else if(classement==2){
            if(pts>6)
                return true;}
        else if(classement==3){
            if(pts>70)
                return true;}
        else if(classement==4){
            if(pts>120)
                return true;}
        else if(classement==5){
            if(pts>170)
                return true;}
        else if(classement==6){
            // 30/1
            if(pts>210)
                return true;}
        else if(classement==7){
            if(pts>280)
                return true;}
        else if(classement==8){
            if(pts>300)
                return true;}
        else if(classement==9){
            if(pts>310)
                return true;}
        else if(classement==10){
            if(pts>320)
                return true;}
        else if(classement==11){
            if(pts>340)
                return true;}
        else if(classement==12){
            // 15/1
            if(pts>370)
                return true;}
        else if(classement==13){
            if(pts>430)
                return true;}
        else if(classement==14){
            if(pts>430)
                return true;}

        return false;
    }

}
