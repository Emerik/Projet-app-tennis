package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.sax.RootElement;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.zip.Inflater;

import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Epreuve;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private Profil mainProfil;
    private int classementBilan;
    private int modeCalcul = 0;

    //View
    private TextView tvClass, tvVict, tvDef, tvPts, tvClassFinal, tvLevUp, tvHypo, tvHypoResult;
    private ListView listViewMatch;
    private Button btnLeft, btnRight;
    private RelativeLayout layoutHypo,layoutHypoResult;



    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);


        listViewMatch = (ListView) rootView.findViewById(R.id.listViewMatch);

        Button btnM = (Button) rootView.findViewById(R.id.buttonM);
        btnM.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), MatchActivity.class);
                //Passage du profil
                intent.putExtra("profil", mainProfil);
                startActivity(intent);

            }
        });

        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);
        tvPts = (TextView) rootView.findViewById(R.id.textViewPts);
        tvClassFinal = (TextView) rootView.findViewById(R.id.textViewClassF);
        tvLevUp = (TextView) rootView.findViewById(R.id.textViewPtsLevUp);

        tvHypo = (TextView) rootView.findViewById(R.id.textViewClassCur);
        layoutHypo = (RelativeLayout) rootView.findViewById(R.id.layoutHypo);
        tvHypoResult = (TextView) rootView.findViewById(R.id.textViewClassResHypo);
        layoutHypoResult = (RelativeLayout) rootView.findViewById(R.id.layoutResHypo);


        //Recuperation données ----------------------------------
        creationProfilFictif();
        classementBilan = mainProfil.getJoueurProfil().getClassement();
        System.out.println("Classement bilan "+classementBilan);
        //CalculBilan
        calculBilanProfil();
        calculHypot();

        btnLeft = (Button) rootView.findViewById(R.id.btnLeftArrow);
        btnRight = (Button) rootView.findViewById(R.id.btnRightArrow);

        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementBilan > 0) {
                    System.out.println("Hypo class "+classementBilan);
                    classementBilan--;
                    calculHypot();
                }
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(classementBilan < 20) {
                    System.out.println("Hypo class "+classementBilan);
                    classementBilan++;
                    calculHypot();
                }
            }
        });



        return rootView;
    }

    public void calculHypot(){

        int ptsManquant = Classement.calculPoint(classementBilan, mainProfil.getMatchs(), modeCalcul);
        int classementCalcule = Classement.calculClassement(classementBilan, mainProfil.getMatchs());
        tvHypo.setText(Classement.convertirClassementInt(classementBilan));
        tvHypoResult.setText("Points : "+ptsManquant);

        // Coloration du cadre classement hypothetique
        if(classementCalcule > classementBilan){
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorGreen));
        }
        else if(classementCalcule < classementBilan){
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorRed));
        }
        else{
            layoutHypoResult.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorOrange));
        }

    }

    public void calculBilanProfil(){
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

        listViewMatch.setAdapter(adapterM);
    }


    //To delete
    public void creationProfilFictif(){
        try {
            if (getActivity().getIntent().getExtras().getParcelable("profil") != null) {
                mainProfil = getActivity().getIntent().getExtras().getParcelable("profil");
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

        }

        if(mainProfil == null){
            Joueur j1 = new Joueur(1, "John", 9, 10, 0);
            Joueur j2 = new Joueur(1, "Grigor Dimitrov", 9, 10, 0);
            Epreuve e1 = new Epreuve(1, "Championnat", 1);

            mainProfil = new Profil(1, "Emerik", j1);

            Match m1 = new Match(mainProfil.getJoueurProfil(), j2, "7/5-7/5", "GreenSet", 1, e1);
            Match m2 = new Match(mainProfil.getJoueurProfil(), j2, "6/3-6/3", "Terre Battue", 2, e1);
            Match m3 = new Match(mainProfil.getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 1, e1);
            Match m4 = new Match(mainProfil.getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 2, e1);

            mainProfil.addMatch(m1);
            mainProfil.addMatch(m2);
            mainProfil.addMatch(m3);
            mainProfil.addMatch(m4);
        }
    }
}