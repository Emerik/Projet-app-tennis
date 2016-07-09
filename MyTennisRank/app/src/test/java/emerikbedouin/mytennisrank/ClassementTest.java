package emerikbedouin.mytennisrank;

import org.junit.Test;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Epreuve;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;

import static org.junit.Assert.assertEquals;

/**
 * Created by emerikbedouin on 17/04/16.
 */
public class ClassementTest {

    Joueur j1 = new Joueur(0, "Roger", 10, 10, 0); // Principal 15/3
    Joueur j2 = new Joueur(0, "Rafa", 11, 10, 0);
    Joueur j3 = new Joueur(0, "Novak", 9, 10, 0);
    Joueur j4 = new Joueur(0, "Andy", 8, 10, 0);
    Joueur j5 = new Joueur(0, "Grigor", 10, 10, 0);
    Match m1 = new Match(j1, j2, "6/0,6/0","surface",1, new Epreuve(), 0,0);
    Match m2 = new Match(j1, j3, "6/0,6/0","surface",1, new Epreuve(), 0,0);
    Match m3 = new Match(j1, j4, "6/0,6/0","surface",1, new Epreuve(), 0,0);
    Match m4 = new Match(j1, j2, "6/0,6/0","surface",0, new Epreuve(), 0,0);
    Match m5 = new Match(j1, j5, "6/0,6/0","surface",1, new Epreuve(), 0,0);
    LinkedList<Match> list1 = new LinkedList<>();
    Profil p1 = new Profil(0, "Federer", j1);


    @Test
    public void getMatchsIntoAccount_isCorrect() throws Exception {

    }

    /**
     * Test si le calcul des points est correct
     * Cas testé :
     * @throws Exception
     */
    @Test
    public void calculPointsTotal_isCorrect() throws Exception {

        // Match de base contre un 15/3
        Match m6Temp = new Match(j1, j5, "6/0,6/0","surface",1, new Epreuve(), 0,0);
        // Match de championnant bonus 15 pts
        Match m7Temp = new Match(j1, j5, "6/0,6/0","surface",1, new Epreuve(), 1,0);
        // Match WO à 15/2
        Match m8Temp = new Match(j1, j2, "6/0,6/0","surface",1, new Epreuve(), 0,1);
        // Match défaite à 15/4
        Match m9Temp = new Match(j1, j3, "6/0,6/0","surface",0, new Epreuve(), 0,0);


        list1 = new LinkedList<>();
        list1.add(m1);
        list1.add(m2);
        list1.add(m3);
        list1.add(m4);
        list1.add(m5);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);
        list1.add(m6Temp);

        assertEquals(Classement.calculPointTotal(10, list1, 0), 730);

        list1.add(m7Temp);
        list1.add(m8Temp);

        assertEquals(Classement.calculPointTotal(10, list1, 0), 745);

        list1.add(m9Temp);

        assertEquals(Classement.calculPointTotal(10, list1, 0), 645);

        // Improve
    }

    @Test
    public void calculClassement_isCorrect() throws Exception {
        list1 = new LinkedList<>();
        list1.add(m1);
        list1.add(m2);
        list1.add(m3);
        list1.add(m4);
        list1.add(m5);
        p1.setMatchs(list1);

        // Methode Classement / Liste Match
        assertEquals(Classement.calculClassement(10, list1, 0), 9);


        // Methode Profil
        assertEquals(Classement.calculClassement(p1, 0), 9);
    }


    @Test
    public void ptsMaintien_isCorrect() throws Exception {
        assertEquals(Classement.ptsMaintien(3), 70);
        assertEquals(Classement.ptsMaintien(9), 310);
        assertEquals(Classement.ptsMaintien(10), 320);
        assertEquals(Classement.ptsMaintien(11), 340);
        assertEquals(Classement.ptsMaintien(12), 370);
        assertEquals(Classement.ptsMaintien(13), 430);
        assertEquals(Classement.ptsMaintien(14), 430);
        //assertEquals(Classement.ptsMaintien(15), 430);
    }

    @Test
    public void maintien_isCorrect() throws Exception {
        assertEquals(Classement.maintien(3,70), true);
        assertEquals(Classement.maintien(3,65), false);

        assertEquals(Classement.maintien(6,210), true);
        assertEquals(Classement.maintien(6,200), false);

        assertEquals(Classement.maintien(9,310), true);
        assertEquals(Classement.maintien(9,300), false);

        assertEquals(Classement.maintien(12,370), true);
        assertEquals(Classement.maintien(12,360), false);
    }

    @Test
    public void convertClassement_isCorrect() throws Exception {
        assertEquals(Classement.convertirClassementInt(3), "30/4");
        assertEquals(Classement.convertirClassementInt(6), "30/1");
        assertEquals(Classement.convertirClassementInt(9), "15/4");
        assertEquals(Classement.convertirClassementInt(10), "15/3");
        assertEquals(Classement.convertirClassementInt(12), "15/1");
        assertEquals(Classement.convertirClassementInt(14), "5/6");

        assertEquals(Classement.convertirClassementString("30/3"), 4);
        assertEquals(Classement.convertirClassementString("30/1"), 6);
        assertEquals(Classement.convertirClassementString("15/4"), 9);
        assertEquals(Classement.convertirClassementString("15/3"), 10);
        assertEquals(Classement.convertirClassementString("15/1"), 12);
        assertEquals(Classement.convertirClassementString("5/6"), 14);
    }

}
