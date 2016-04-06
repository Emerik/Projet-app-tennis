package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.Modele.Epreuve;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

public class MatchActivity extends AppCompatActivity {

    private Profil mainProfil;

    // View
    private ListView listViewVictoire, listViewDefaite;
    private FloatingActionButton btnAddMatch;
    private Button btnBilan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        mainProfil = getIntent().getExtras().getParcelable("profil");

        /* to delete ----------------------------------------------------------------
        Joueur j1 = new Joueur(1, "John", 9, 10, 0);
        Joueur j2 = new Joueur(1, "Grigor", 9, 10, 0);
        Epreuve e1 = new Epreuve(1, "Championnat", 1);
        Profil p1 = new Profil(1, "Emerik", j1);

        Match m1 = new Match(p1.getJoueurProfil(), j2, "7/5-7/5", "GreenSet", 1, e1);
        Match m2 = new Match(p1.getJoueurProfil(), j2, "6/3-6/3", "Terre Battue", 2, e1);
        Match m3 = new Match(p1.getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 1, e1);
        Match m4 = new Match(p1.getJoueurProfil(), j2, "7/5-6/4", "GreenSet", 2, e1);

        p1.getJoueurProfil().addMatch(m1);
        p1.getJoueurProfil().addMatch(m2);
        p1.getJoueurProfil().addMatch(m3);
        p1.getJoueurProfil().addMatch(m4);

        */// --------------------------------------------------------------------------

        listViewVictoire = (ListView) findViewById(R.id.listViewV);
        listViewDefaite = (ListView) findViewById(R.id.listViewD);


        LinkedList<Match> listMatch = mainProfil.getMatchs();
        LinkedList<Match> listVictoire = new LinkedList<>();
        LinkedList<Match> listDefaite = new LinkedList<>();


        for(int i=0; i < listMatch.size() ; i++){
            if(listMatch.get(i).getGagnant().equals(mainProfil.getJoueurProfil()) ){
                listVictoire.add(listMatch.get(i));
            }
            else {
                listDefaite.add(listMatch.get(i));
            }
        }

        //Définition de l'adapter
        ArrayAdapter<Match> adapterV = new ArrayAdapter<Match>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listVictoire);
        ArrayAdapter<Match> adapterD = new ArrayAdapter<Match>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listDefaite);

        listViewVictoire.setAdapter(adapterV);
        listViewDefaite.setAdapter(adapterD);

        //On click
        listViewVictoire.setOnItemClickListener(new ClickOnMatch());
        listViewDefaite.setOnItemClickListener(new ClickOnMatch());

        btnAddMatch = (FloatingActionButton) findViewById(R.id.btnAddMatchView);

        btnAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de la fenetre d'ajout de match
                Intent intent = new Intent(MatchActivity.this, MatchDetailActivity.class);
                //Passage du profil
                intent.putExtra("profil", mainProfil);
                intent.putExtra("mode","add");
                startActivity(intent);
            }
        });

        btnBilan = (Button) findViewById(R.id.buttonBilan);
        btnBilan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de la fenetre d'ajout de match
                Intent intent = new Intent(MatchActivity.this, MainActivity.class);
                //Passage du profil
                intent.putExtra("profil", mainProfil);
                startActivity(intent);
            }
        });

    }

    // A définir -----------------------------------------

    void initComposant(){

    }

    void recuperationMatch(){

    }

    public void addMatch(){
        //Lancement de l'écran Match détails
    }

    class ClickOnMatch implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // l'index de l'item dans notre ListView
            int itemPosition = position;

            // On récupère le match  cliqué
            Match matchSelected = (Match) parent.getItemAtPosition(position);

            // On affiche ce texte avec un Toast
            //Toast.makeText( getApplicationContext(), "Position :" + itemPosition + "  ListItem : " + itemValue, Toast.LENGTH_LONG).show();

            // Lancement de l'activité MatchDetailActivity
            Intent intent = new Intent(MatchActivity.this, MatchDetailActivity.class);
            //Passage du profil
            intent.putExtra("profil", mainProfil);
            intent.putExtra("mode","modify");
            intent.putExtra("match", matchSelected);
            startActivity(intent);
        }
    }
}
