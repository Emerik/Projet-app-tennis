package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.Controler.ProfilSingleton;
import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

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

        classementBilan = 0;

        listViewMatch = (ListView) rootView.findViewById(R.id.listViewMatch);

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

        tvClass = (TextView) rootView.findViewById(R.id.textViewClassement);
        tvVict = (TextView) rootView.findViewById(R.id.textViewVictoire);
        tvDef = (TextView) rootView.findViewById(R.id.textViewDefaite);
        tvPts = (TextView) rootView.findViewById(R.id.textViewPts);
        tvClassFinal = (TextView) rootView.findViewById(R.id.textViewBonus);
        tvLevUp = (TextView) rootView.findViewById(R.id.textViewPtsLevUp);

        tvHypo = (TextView) rootView.findViewById(R.id.textViewClassCur);
        layoutHypo = (RelativeLayout) rootView.findViewById(R.id.layoutHypo);
        tvHypoResult = (TextView) rootView.findViewById(R.id.textViewClassResHypo);
        layoutHypoResult = (RelativeLayout) rootView.findViewById(R.id.layoutResHypo);


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

        //Recuperation données ---------------------------------------------------------------------

        //getProfil();

        if(ProfilSingleton.getInstance().getProfil() != null) {
            classementBilan = ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement();
            System.out.println("Classement bilan " + classementBilan);
            //CalculBilan
            calculBilanProfil();
            calculHypot();
        }




        return rootView;
    }

    public void calculHypot(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

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
        LinkedList<Match> matchsIntoAccount = Classement.getMatchsIntoAccount(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs(), 0);
        MatchAdapter adapterM = new MatchAdapterPoints(this.getActivity(), matchsIntoAccount, 1, classementBilan);

        listViewMatch.setAdapter(adapterM);
    }


    //To delete
    public void getProfil(){
        try {
            if (getActivity().getIntent().getExtras().getParcelable("profil") != null) {
               // ((MainActivity) getActivity()).setMainProfil( (Profil) getActivity().getIntent().getExtras().getParcelable("profil") );

            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());

        }

        if(ProfilSingleton.getInstance().getProfil() == null){
            /*Joueur j1 = new Joueur(1, "John", 9, 10, 0);
            Joueur j2 = new Joueur(1, "Grigor Dimitrov", 9, 10, 0);
            Epreuve e1 = new Epreuve(1, "Championnat", 1);

            ((MainActivity) getActivity()).setMainProfil( new Profil(1, "Emerik", j1) );

            Match m1 = new Match(((MainActivity) getActivity()).getMainProfil().getJoueurProfil(), j2, "7/5-7/5", "GreenSet", 1, e1);
            Match m2 = new Match(((MainActivity) getActivity()).getMainProfil().getJoueurProfil(), j2, "6/3-6/3", "Terre Battue", 2, e1);
            Match m3 = new Match(((MainActivity) getActivity()).getMainProfil().getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 1, e1);
            Match m4 = new Match(((MainActivity) getActivity()).getMainProfil().getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 2, e1);

            ((MainActivity) getActivity()).getMainProfil().addMatch(m1);
            ((MainActivity) getActivity()).getMainProfil().addMatch(m2);
            ((MainActivity) getActivity()).getMainProfil().addMatch(m3);
            ((MainActivity) getActivity()).getMainProfil().addMatch(m4);*/
        }
    }
}
