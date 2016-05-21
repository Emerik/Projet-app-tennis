package emerikbedouin.mytennisrank.IHM;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Parcelable;
import android.os.SystemClock;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.lzyzsd.circleprogress.DonutProgress;

import java.util.LinkedList;
import java.util.zip.Inflater;

import emerikbedouin.mytennisrank.Controler.ProfilSingleton;
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
    //private ListView listViewMatch;
    private Button btnLeft, btnRight;
    private RelativeLayout layoutHypo,layoutHypoResult;
    private DonutProgress ptsBarProgress;


    public MainActivityV2Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main_activity_v2, container, false);

        classementCalcul = 0;

        //listViewMatch = (ListView) rootView.findViewById(R.id.listViewMatch);

        Button btnM = (Button) rootView.findViewById(R.id.buttonM);
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MatchActivity.class);
                //Passage du profil
                //intent.putExtra("profil", (Parcelable) ((MainActivity) getActivity()).getMainProfil());
                startActivity(intent);

            }
        });


        // Bilan
        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);
        tvPts = (TextView) rootView.findViewById(R.id.textViewPts);
        tvClassFinal = (TextView) rootView.findViewById(R.id.textViewClassF);
        tvLevUp = (TextView) rootView.findViewById(R.id.textViewPtsLevUp);


        // Hypo - circle
        btnLeft = (Button) rootView.findViewById(R.id.btnLeftArrow);
        btnRight = (Button) rootView.findViewById(R.id.btnRightArrow);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementCalcul > 0) {

                    classementCalcul--;
                    progressBar();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementCalcul < 20) {

                    classementCalcul++;
                    progressBar();
                }
            }
        });

        ptsBarProgress = (DonutProgress) rootView.findViewById(R.id.donut_progress);

        //Recuperation données ---------------------------------------------------------------------

        //getProfil();

        if(ProfilSingleton.getInstance().getProfil() != null) {
            classementCalcul = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            //CalculBilan
            calculBilanProfil();
            //calculHypot();
            progressBar();
        }




        return rootView;
    }

    public void progressBar(){
        //Ratio nombre de points actuel / nombre de points requis
        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        int pts = Classement.calculPoint(classementCalcul, mainProfil.getMatchs(), modeCalcul);
        int ptsMaintien = Classement.ptsMaintien(classementCalcul);

        int res = pts*100/ptsMaintien;

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

    }

    public void calculHypot(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        int ptsManquant = Classement.calculPoint(classementCalcul, mainProfil.getMatchs(), modeCalcul);
        int classementCalcule = Classement.calculClassement(classementCalcul, mainProfil.getMatchs());
        tvHypo.setText(Classement.convertirClassementInt(classementCalcul));
        tvHypoResult.setText("Points : "+ptsManquant);

        // Coloration du cadre classement hypothetique
        if(classementCalcule > classementCalcul){
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        }
        else if(classementCalcule < classementCalcul){
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
        }
        else{
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
        }

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
        tvPts.setText(tvPts.getText()+" "+ Classement.calculPoint(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs(), 0));
        //Classement final
        tvClassFinal.setText(tvClassFinal.getText()+" "+Classement.convertirClassementInt(Classement.calculClassement(mainProfil)));
        // Points manquant classement au dessus
        //tvLevUp.setText(tvLevUp.getText()+" "+mainProfil.getJoueurProfil().getClassement());


        //La listview
        LinkedList<Match> matchsIntoAccount = Classement.getMatchsIntoAccount(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs());
        MatchAdapter adapterM = new MatchAdapter(this.getActivity(), matchsIntoAccount);

        //listViewMatch.setAdapter(adapterM);
    }


}
