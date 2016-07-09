package emerikbedouin.mytennisrank.IHM;

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

import emerikbedouin.mytennisrank.DAO.ProfilSingleton;
import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Epreuve;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityV2Fragment extends Fragment {

    private int classementCalcul;
    private int modeCalcul = 0;

    //View
    private TextView tvClass, tvVict, tvDef, tvPts, tvClassFinal, tvLevUp, tvHypo, tvHypoResult;
    private Button btnLeft, btnRight, btnAllDrop, btnFutur, btnAllUp, btnNormal;
    private RelativeLayout layoutBilan,layoutHypoResult;
    private DonutProgress ptsBarProgress;


    public MainActivityV2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity_v2, container, false);

        // Recuperation des Vue
        initComp(rootView);


        animateBilan();

        // Traitement
        classementCalcul = 0;
        if(ProfilSingleton.getInstance().getProfil() != null) {
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            calculBilanProfil();
            upProgressBar();
        }
        else{
            creationProfilComplet();
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            calculBilanProfil();
            upProgressBar();
        }

        return rootView;
    }


    public void initComp(View rootView){

        Button btnM = (Button) rootView.findViewById(R.id.buttonM);
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MatchActivity.class);
                startActivity(intent);
            }
        });


        // Bilan
        layoutBilan = (RelativeLayout) rootView.findViewById(R.id.relativLayoutBilan);
        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);
        tvPts = (TextView) rootView.findViewById(R.id.textViewPts);
        tvClassFinal = (TextView) rootView.findViewById(R.id.textViewBonus);
        tvLevUp = (TextView) rootView.findViewById(R.id.textViewPtsLevUp);


        // Hypo - circle
        btnLeft = (Button) rootView.findViewById(R.id.btnLeftArrow);
        btnRight = (Button) rootView.findViewById(R.id.btnRightArrow);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementCalcul > 0) {

                    classementCalcul--;
                    upProgressBar();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementCalcul < 20) {

                    classementCalcul++;
                    upProgressBar();
                }
            }
        });

        ptsBarProgress = (DonutProgress) rootView.findViewById(R.id.donut_progress);

        // Bouton all monter,descente
        btnAllDrop = (Button) rootView.findViewById(R.id.buttonAllDrop);
        btnFutur = (Button) rootView.findViewById(R.id.buttonFutur);
        btnNormal = (Button) rootView.findViewById(R.id.buttonNormal);
        btnAllUp = (Button) rootView.findViewById(R.id.buttonAllUp);

        btnAllDrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeCalcul = 2;
                upProgressBar();
            }
        });

        btnFutur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeCalcul = 1;
                upProgressBar();
            }
        });

        btnAllUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeCalcul = 3;
                upProgressBar();
            }
        });

        btnNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modeCalcul = 0;
                upProgressBar();
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

    public void upProgressBar(){
        //Ratio nombre de points actuel / nombre de points requis
        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        int pts = Classement.calculPointTotal(classementCalcul, mainProfil.getMatchs(), modeCalcul);
        int ptsMaintien = Classement.ptsMaintien(classementCalcul);
        System.out.println(classementCalcul);

        int res = 0;

        if(ptsMaintien != 0) res = pts*100/ptsMaintien;

        System.out.println("Res circle "+res+" p "+pts+" "+ptsMaintien);

        if(res >= 100) {
            res = 100;
            ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        }
        else{
            ptsBarProgress.setFinishedStrokeColor(ContextCompat.getColor(getContext(), R.color.colorPerso));
        }

        //ptsBarProgress.setProgress(res);

        // Animation de la progress bar
        int time = 0;
        int diffProgress = ptsBarProgress.getProgress() - res ;
        if(Math.abs(diffProgress) < 30) time = 250; else time = 750;
        ObjectAnimator animation = ObjectAnimator.ofInt(ptsBarProgress, "progress", res);
        animation.setDuration(time);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();

        ptsBarProgress.setInnerBottomText(pts+" points à "+Classement.convertirClassementInt(classementCalcul));
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

    public void calculBilanProfil(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        //Classement
        tvClass.setText(tvClass.getText()+" "+Classement.convertirClassementInt(mainProfil.getJoueurProfil().getClassement()));
        //Victoire
        tvVict.setText(tvVict.getText()+" "+mainProfil.getNbreVictoire());
        //Defaite
        tvDef.setText(tvDef.getText()+" "+mainProfil.getNbreDefaite());
        // Points
        tvPts.setText(tvPts.getText()+" "+ Classement.calculPointTotal(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs(), modeCalcul));
        //Classement final
        tvClassFinal.setText(tvClassFinal.getText()+" "+Classement.convertirClassementInt(Classement.calculClassement(mainProfil, modeCalcul)));
        // Points manquant classement au dessus
        //tvLevUp.setText(tvLevUp.getText()+" "+mainProfil.getJoueurProfil().getClassement());


        //La listview
        //LinkedList<Match> matchsIntoAccount = Classement.getMatchsIntoAccount(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs());
        //MatchAdapter adapterM = new MatchAdapter(this.getActivity(), matchsIntoAccount);

        //listViewMatch.setAdapter(adapterM);
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


    }

}