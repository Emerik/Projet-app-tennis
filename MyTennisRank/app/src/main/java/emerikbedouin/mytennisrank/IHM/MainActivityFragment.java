package emerikbedouin.mytennisrank.ihm;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.lzyzsd.circleprogress.DonutProgress;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Epreuve;
import emerikbedouin.mytennisrank.modele.Joueur;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements UpdateFragment{

    private int classementCalcul;
    private int modeCalcul = 0;

    //View
    private TextView tvBilan, tvClass, tvVict, tvDef, tvSimulation;
    private Button btnLeft, btnRight;
    private RelativeLayout layoutBilan;
    private DonutProgress ptsBarProgress;
    private Button btnCurrent, btnFutur, btnDown, btnUp;


    public MainActivityFragment() {
        // Constructeur MainActivityFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity, container, false);

        // Recuperation des Vue
        initComposant(rootView);


        animateBilan();


        // Traitement
        classementCalcul = 0;
        if(ProfilSingleton.getInstance().getProfil() != null) {
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            upBilanProfil();
            upProgressBar();
        }
        else{
            /*creationProfilComplet();
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            calculBilanProfil();
            upProgressBar();*/
        }

        return rootView;
    }


    /**
     * Initialisation des composant du Fragment
     * @param rootView
     */
    public void initComposant(View rootView){


        // Bilan
        layoutBilan = (RelativeLayout) rootView.findViewById(R.id.relativLayoutBilan);
        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);


        // Hypo - circle
        btnLeft = (Button) rootView.findViewById(R.id.btnLeftArrow);
        btnRight = (Button) rootView.findViewById(R.id.btnRightArrow);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeClassementCalcul(-1);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeClassementCalcul(1);
            }
        });

        ptsBarProgress = (DonutProgress) rootView.findViewById(R.id.donut_progress);

        tvSimulation = (TextView) rootView.findViewById(R.id.simulationTextView);


        // Boutons de modification du mode de calcul
        btnCurrent = (Button) rootView.findViewById(R.id.currentButton);
        btnFutur = (Button) rootView.findViewById(R.id.futurButton);
        btnDown = (Button) rootView.findViewById(R.id.downButton);
        btnUp = (Button) rootView.findViewById(R.id.upButton);

        btnCurrent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCurrent();
            }
        });
        btnFutur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFutur();
            }
        });
        btnDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDown();
            }
        });
        btnUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUp();
            }
        });

    }

    public void animateBilan(){
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
       // layoutBilan.setAlpha(0f);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.


        layoutBilan.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

    }


    /**
     * Cette fonction actualise la bar de progression du joueur
     */
    public void upProgressBar(){

        //Ratio nombre de points actuel / nombre de points requis
        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        if(mainProfil != null) {

            int pts = Classement.calculPointTotal(classementCalcul, mainProfil.getMatchs(), modeCalcul);
            int ptsMaintien = Classement.ptsMaintien(classementCalcul);

            int res = 0;

            if (ptsMaintien != 0) res = pts * 100 / ptsMaintien;

            if (res >= 100) {
                res = 100;
                ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
                ptsBarProgress.setProgress(0);
            } else {
                ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorFail));
            }

            //ptsBarProgress.setProgress(res);


            // Animation de la progress bar
            int time = 0;
            int diffProgress = ptsBarProgress.getProgress() - res;
            if (Math.abs(diffProgress) < 30) time = 250;
            else time = 750;
            ObjectAnimator animation = ObjectAnimator.ofInt(ptsBarProgress, "progress", res);
            animation.setDuration(time);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();

            ptsBarProgress.setInnerBottomText(pts + " points à " + Classement.convertirClassementInt(classementCalcul));
            ptsBarProgress.setInnerBottomTextColor(ContextCompat.getColor(getContext(), R.color.colorPersoDark));


            // Onclick lance le detail du calcul
            ptsBarProgress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Lancement de la fenetre de création d'un nouveau profil
                    Intent intent = new Intent(getActivity(), CalculDetailsActivity.class);
                    intent.putExtra("classement", classementCalcul);
                    intent.putExtra("mode", modeCalcul);
                    startActivity(intent);
                }
            });
        }
    }

    /**
     * Cette fonction actualise la partie Bilan avec les données du profil
     */
    public void upBilanProfil(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        //Classement
        tvClass.setText(Classement.convertirClassementInt(mainProfil.getJoueurProfil().getClassement()));
        //Victoire
        tvVict.setText(mainProfil.getNbreVictoire()+" Victoire");
        //Defaite
        tvDef.setText(mainProfil.getNbreDefaite()+" Défaites");
        // Points
        //tvPts.setText(tvPts.getText()+" "+ Classement.calculPointTotal(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs(), modeCalcul));
        //Classement final
        //tvClassFinal.setText(tvClassFinal.getText()+" "+Classement.convertirClassementInt(Classement.calculClassement(mainProfil, modeCalcul)));
        // Points manquant classement au dessus
        //tvLevUp.setText(tvLevUp.getText()+" "+mainProfil.getJoueurProfil().getClassement());


        //La listview
        //LinkedList<Match> matchsIntoAccount = Classement.getMatchsIntoAccount(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs());
        //MatchAdapter adapterM = new MatchAdapter(this.getActivity(), matchsIntoAccount);

        //listViewMatch.setAdapter(adapterM);
    }


    // Fonctions pour la séléction du mode de calcul

    public void setCurrent(){
        modeCalcul = 0;
        tvSimulation.setText("Simulation classement ACTUEL");
        upProgressBar();
    }

    public void setFutur(){
        modeCalcul = 1;
        tvSimulation.setText("Simulation classement PRÉVU");
        upProgressBar();
    }

    public void setDown(){
        modeCalcul = 2;
        tvSimulation.setText("Simulation classement DESCENTE");
        upProgressBar();
    }

    public void setUp(){
        modeCalcul = 3;
        tvSimulation.setText("Simulation classement MONTER");
        upProgressBar();
    }

    /**
     * Cett fonction vérifie que les composants ont bien été initialisé
     * @return
     */
    public boolean initialized(){
        if (tvSimulation == null || tvClass == null || tvBilan == null || tvDef == null || tvVict == null) return false;
        if(ptsBarProgress == null) return false;
        if(btnCurrent == null || btnFutur == null || btnDown == null || btnUp == null) return false;

        return true;
    }

    /**
     * Cette fonction actualise les données de la Fragement
     */
    @Override
    public void update() {
        if (initialized()) {
            upBilanProfil();
            upProgressBar();
        }
    }

    public void changeClassementCalcul(int nbr){
        if(classementCalcul  > 0 && classementCalcul < 20) {

            classementCalcul += nbr;
            upProgressBar();
        }
    }

    // Pour les tests ------ To delete
    public void creationProfilComplet(){

        Profil p = new Profil();

        Joueur j1 = new Joueur(0, "Roger", 10, 10, 0); // Principal 15/3
        Joueur j2 = new Joueur(0, "David", 10, 9, 0);
        Joueur j3 = new Joueur(0, "Yannick", 11, 10, 0);
        Joueur j4 = new Joueur(0, "Victor", 9, 8, 0);
        Joueur j5 = new Joueur(0, "gwenael", 9, 8, 0);
        Joueur j6 = new Joueur(0, "Jerome", 8, 8, 0);
        Joueur j7 = new Joueur(0, "Gaylor", 8, 7, 0);
        Joueur j8 = new Joueur(0, "Sebastien", 8, 9, 0);
        Joueur j9 = new Joueur(0, "Noam", 11, 11, 0);
        Joueur j10 = new Joueur(0, "Pierre", 10, 11, 0);



        Match m1 = new Match(j1, j2, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m2 = new Match(j1, j3, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m3 = new Match(j1, j4, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m4 = new Match(j1, j5, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m5 = new Match(j1, j6, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m6 = new Match(j1, j7, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m7 = new Match(j1, j8, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m8 = new Match(j1, j6, "6/0,6/0","surface",1, new Epreuve(),0,0);

        Match m9 = new Match(j1, j9, "6/0,6/0","surface",0, new Epreuve(),0,0);
        Match m10 = new Match(j1, j10, "6/0,6/0","surface",0, new Epreuve(),0,0);

        p.setJoueurProfil(j1);

        p.getMatchs().add(m1);
        p.getMatchs().add(m2);
        p.getMatchs().add(m3);
        p.getMatchs().add(m4);
        p.getMatchs().add(m5);
        p.getMatchs().add(m6);
        p.getMatchs().add(m7);
        p.getMatchs().add(m8);
        p.getMatchs().add(m9);
        p.getMatchs().add(m10);

        ProfilSingleton.getInstance().setProfil(p);

        Toast.makeText(getActivity().getApplicationContext(), "Profil temp !", Toast.LENGTH_LONG).show();

        // Actualisation des données du Fragment des Matchs
        ((ViewPagerAdapter) ((MainActivity) getActivity()).getViewPager().getAdapter()).updateItems();
    }

}
