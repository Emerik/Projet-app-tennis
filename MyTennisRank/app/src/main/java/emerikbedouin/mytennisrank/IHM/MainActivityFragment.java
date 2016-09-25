package emerikbedouin.mytennisrank.ihm;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.TabLayout;
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

import com.github.lzyzsd.circleprogress.DonutProgress;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements UpdateFragment{

    private int classementCalcul;
    private int modeCalcul = 0;

    //View
    private TextView tvClass, tvVict, tvDef, tvNom, tvSimulation;
    private RelativeLayout layoutBilan;
    private DonutProgress ptsBarProgress;
    private TabLayout tabLayout;


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
        classementCalcul = 1;

        if(ProfilSingleton.getInstance().getProfil() != null) {
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            upBilanProfil();
            upProgressBar();
        }
        else{
            //Aucun profil chargé
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
        tvNom = (TextView) rootView.findViewById(R.id.textViewNom);


        // Hypo - circle
        Button btnLeft = (Button) rootView.findViewById(R.id.btnLeftArrow);
        Button btnRight = (Button) rootView.findViewById(R.id.btnRightArrow);

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



        tabLayout = (TabLayout) rootView.findViewById(R.id.tabMode);

        tabLayout.addTab(tabLayout.newTab().setText("Actuel"));
        tabLayout.addTab(tabLayout.newTab().setText("Prévu"));
        tabLayout.addTab(tabLayout.newTab().setText("Descente"));
        tabLayout.addTab(tabLayout.newTab().setText("Monter"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0: setCurrent();
                            break;
                    case 1: setFutur();
                        break;
                    case 2: setDown();
                        break;
                    case 3: setUp();
                            break;
                    default: break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //Unused
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //Unused
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
            ptsBarProgress.setInnerBottomTextColor(ContextCompat.getColor(getContext(), R.color.colorUdaGreen));


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
        else{
            ptsBarProgress.setProgress(0);
        }
    }

    /**
     * Cette fonction actualise la partie Bilan avec les données du profil
     */
    public void upBilanProfil(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();


        if(mainProfil != null) {
            //Classement
            tvClass.setText(Classement.convertirClassementInt(mainProfil.getJoueurProfil().getClassement()));
            //Victoire
            tvVict.setText(mainProfil.getNbreVictoire() + " Victoire");
            //Defaite
            tvDef.setText(mainProfil.getNbreDefaite() + " Défaites");
            //Nom du profil
            tvNom.setText(mainProfil.getNom());

        }
        else{
            tvClass.setText("Nom profil");
            tvVict.setText("0");
            tvDef.setText("0");
        }
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
        if (tvSimulation == null || tvClass == null || tvDef == null || tvVict == null) return false;
        if(ptsBarProgress == null) return false;
        if(tabLayout == null) return false;

        return true;
    }

    /**
     * Cette fonction actualise les données de la Fragement
     */
    @Override
    public void update() {

        if (initialized()) {
            if(ProfilSingleton.getInstance().getProfil()!= null ) classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();

            upBilanProfil();
            upProgressBar();
        }
    }

    /**
     * Cette fonction modifie le classement servant de base a la simulation
     * @param nbr Le nombre à ajouter au classement actuel
     */
    public void changeClassementCalcul(int nbr){
        if (classementCalcul > 0 && classementCalcul < 20) {

            classementCalcul += nbr;
            upProgressBar();
        }
    }


}
