package emerikbedouin.mytennisrank.ihm;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements UpdateFragment{

    private int mClassementCalcul;
    private int mModeCalcul = 0;

    //View
    private TextView tvClass, tvVict, tvDef, tvNom, tvSimulation;
    private LinearLayout layoutBilan;
    private DonutProgress ptsBarProgress;
    private TabLayout tabLayout;
    private ImageView mDetailImageView;
    private View classBottomLine,vicdefBottomLine;


    public MainActivityFragment() {
        // Constructeur MainActivityFragment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_v2, container, false);

        // Recuperation des Vue
        initComposant(rootView);

        animateBilan();
        animateLine();

        // Traitement
        mClassementCalcul = 1;

        if(ProfilSingleton.getInstance().getProfil() != null) {
            mClassementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
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
        layoutBilan = (LinearLayout) rootView.findViewById(R.id.layout_bilan);
        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);
        //tvNom = (TextView) rootView.findViewById(R.id.textViewNom);


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

        //tvSimulation = (TextView) rootView.findViewById(R.id.simulationTextView);


        mDetailImageView = (ImageView) rootView.findViewById(R.id.detail_imageView);

        // Onclick lance le detail du calcul
        mDetailImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de la fenetre de création d'un nouveau profil
                Intent intent = new Intent(getActivity(), CalculDetailsActivity.class);
                intent.putExtra("classement", mClassementCalcul);
                intent.putExtra("mode", mModeCalcul);
                startActivity(intent);
            }
        });

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


        // Les bottom line
        classBottomLine = rootView.findViewById(R.id.classement_bottomline);
        vicdefBottomLine = rootView.findViewById(R.id.vicdef_bottomline);

    }

    public void animateBilan(){
        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
       // layoutBilan.setAlpha(0f);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.


        tvClass.animate()
                .alpha(1f)
                .setDuration(500)
                .setListener(null);

    }


    public void animateLine(){


        DisplayMetrics metrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth = metrics.widthPixels;

        Log.v("MAIN FRGAMENT","screen w : "+screenWidth);

        ViewWidthAnimation anim2 = new ViewWidthAnimation(vicdefBottomLine, screenWidth*18/20);
        anim2.setDuration(500);

        ViewWidthAnimation anim = new ViewWidthAnimation(classBottomLine, screenWidth*3/6);
        anim.setDuration(750);

        classBottomLine.startAnimation(anim);
        vicdefBottomLine.startAnimation(anim2);

    }

    /**
     * Cette fonction actualise la bar de progression du joueur
     */
    public void upProgressBar(){

        //Ratio nombre de points actuel / nombre de points requis
        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        if(mainProfil != null) {


            int pts = Classement.calculPointTotal(mClassementCalcul, mainProfil.getMatchs(), mModeCalcul);
            int ptsMaintien = Classement.ptsMaintien(mClassementCalcul);

            int res = 0;

            if (ptsMaintien != 0) res = pts * 100 / ptsMaintien;

            if (res >= 100) {
                res = 100;
                ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
                ptsBarProgress.setProgress(0);
            } else {
                ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorSuccess));
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

            ptsBarProgress.setInnerBottomText(pts + " points à " + Classement.convertirClassementInt(mClassementCalcul));
            ptsBarProgress.setInnerBottomTextColor(ContextCompat.getColor(getContext(), R.color.colorUda));



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
            tvVict.setText(mainProfil.getNbreVictoire() + "");
            //Defaite
            tvDef.setText(mainProfil.getNbreDefaite() + "");
            //Nom du profil
            //tvNom.setText(mainProfil.getNom());

        }
        else{
            tvClass.setText("Nom profil");
            tvVict.setText("0");
            tvDef.setText("0");
        }
    }


    // Fonctions pour la séléction du mode de calcul

    public void setCurrent(){

        mModeCalcul = 0;
        //tvSimulation.setText("Simulation classement ACTUEL");
        upProgressBar();
    }

    public void setFutur(){
        mModeCalcul = 1;
        //tvSimulation.setText("Simulation classement PRÉVU");
        upProgressBar();
    }

    public void setDown(){
        mModeCalcul = 2;
        //tvSimulation.setText("Simulation classement DESCENTE");
        upProgressBar();
    }

    public void setUp(){
        mModeCalcul = 3;
        //tvSimulation.setText("Simulation classement MONTER");
        upProgressBar();
    }

    /**
     * Cett fonction vérifie que les composants ont bien été initialisé
     * @return
     */
    public boolean initialized(){

        if ( tvClass == null || tvDef == null || tvVict == null ) return false;
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

            if(ProfilSingleton.getInstance().getProfil()!= null ) mClassementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();

            upBilanProfil();
            upProgressBar();
            animateLine();
        }
    }

    /**
     * Cette fonction modifie le classement servant de base a la simulation
     * @param nbr Le nombre à ajouter au classement actuel
     */
    public void changeClassementCalcul(int nbr){
        if (mClassementCalcul > 0 && mClassementCalcul < 20) {

            mClassementCalcul += nbr;
            upProgressBar();
        }
    }


}
