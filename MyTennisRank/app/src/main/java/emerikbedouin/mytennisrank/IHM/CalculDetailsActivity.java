package emerikbedouin.mytennisrank.ihm;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

public class CalculDetailsActivity extends AppCompatActivity {


    private int modeCalcul = 0;
    private int classement;

    //View
    private TextView tvClass, tvVict, tvDef, tvPts, tvTotal, tvBonus, tvBilan, tvPEC, tvDelta;
    private RelativeLayout layoutBilan;
    private Button btnBack;
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


    public void initComp(){

        btnBack = (Button) findViewById(R.id.buttonBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CalculDetailsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


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
}
