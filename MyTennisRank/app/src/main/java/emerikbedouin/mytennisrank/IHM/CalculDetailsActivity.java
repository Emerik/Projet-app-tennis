package emerikbedouin.mytennisrank.ihm;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * Activity affichant le résultat détaillé d'une simulation
 */
public class CalculDetailsActivity extends AppCompatActivity {


    private int modeCalcul = 0;
    private int classement;

    //View
    private TextView tvClass, tvVict, tvDef, tvPts, tvTotal, tvBonus, tvBilan, tvPEC, tvDelta;
    private RelativeLayout layoutBilan;
    private ListView listViewMatch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcul_details);

        if ( getIntent().getExtras().get("classement") != null){
            classement = (int) getIntent().getExtras().get("classement");
        }
        else {
            classement = 0;
        }

        if ( getIntent().getExtras().get("mode") != null){
            modeCalcul = (int) getIntent().getExtras().get("mode");
        }
        else {
            modeCalcul = 0;
        }

        // Recupération des view
        initComp();

        fillViewCalcul();

    }

    /**
     * Initialisation des composants graphiques
     */
    public void initComp(){


        // Bilan
        layoutBilan = (RelativeLayout) findViewById(R.id.relativLayoutBilan);
        tvClass = (TextView) findViewById(R.id.textViewClassement);
        tvVict = (TextView) findViewById(R.id.textViewVictoire);
        tvDef = (TextView) findViewById(R.id.textViewDefaite);
        tvPts = (TextView) findViewById(R.id.textViewPts);
        tvTotal = (TextView) findViewById(R.id.textViewTotal);
        tvBonus = (TextView) findViewById(R.id.textViewBonus);
        tvPEC = (TextView) findViewById(R.id.textViewPEC);
        tvDelta = (TextView) findViewById(R.id.textViewDelta);
        tvBilan = (TextView) findViewById(R.id.textViewBilan);


        listViewMatch = (ListView) findViewById(R.id.listViewMatch);
    }

    /**
     * Cette fonction rempli les différentes view avec le résultat de la simulation
     */
    public void fillViewCalcul(){

        Profil mainProfil = ProfilSingleton.getInstance().getProfil();

        if(mainProfil != null) {
            int points = Classement.calculPointMatchs(classement, mainProfil.getMatchs(), modeCalcul);
            int bonus = Classement.bonusAbsenceDefaiteSignificative(mainProfil.getMatchs(), classement, modeCalcul) + Classement.bonusChampionnat(mainProfil.getMatchs());

            //Classement
            tvClass.setText(tvClass.getText() + " " + Classement.convertirClassementInt(classement));
            //Victoire
            tvVict.setText(tvVict.getText() + " " + mainProfil.getNbreVictoire());
            //Defaite
            tvDef.setText(tvDef.getText() + " " + mainProfil.getNbreDefaite());
            // Points
            tvPts.setText(tvPts.getText() + " " + points);
            //Delta
            tvDelta.setText(tvDelta.getText() + " " + Classement.calculVE2I5G(mainProfil.getMatchs(), classement, modeCalcul));
            // PEC
            tvPEC.setText(tvPEC.getText() + " " + Classement.calculNbrVictoirePEC(mainProfil.getMatchs(), classement, modeCalcul));
            // Bonus
            tvBonus.setText(tvBonus.getText() + " " + bonus);
            // Total
            tvTotal.setText(tvTotal.getText() + " " + (points + bonus));
            // Bilan
            tvBilan.setText(tvBilan.getText() + " " + Classement.convertirClassementInt(Classement.calculClassement(mainProfil, modeCalcul)));


            //La listview
            LinkedList<Match> matchsIntoAccount = Classement.getMatchsIntoAccount(mainProfil.getJoueurProfil().getClassement(), mainProfil.getMatchs(), modeCalcul);
            MatchAdapter adapterM = new MatchAdapterPoints(this, matchsIntoAccount, 1, classement, modeCalcul);

            View headerView = (View) getLayoutInflater().inflate(R.layout.listview_header, null);

            listViewMatch.addHeaderView(headerView);

            listViewMatch.setAdapter(adapterM);
        }
    }

}
