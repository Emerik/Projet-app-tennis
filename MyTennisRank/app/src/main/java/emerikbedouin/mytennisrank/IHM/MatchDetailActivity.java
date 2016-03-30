package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

public class MatchDetailActivity extends AppCompatActivity {

    private Profil mainProfil;

    // View
    private EditText editTextJ2, editTextScore, editTextSurface;
    private RadioGroup radioGroupVD;
    private Button btnValid, btnCancel;
    private Spinner spinnerClassement;

    public MatchDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        mainProfil = getIntent().getExtras().getParcelable("profil");

        // Les EditText
        editTextJ2 = (EditText) findViewById(R.id.editTextJ2);
        editTextScore = (EditText) findViewById(R.id.editTextScore);
        editTextSurface = (EditText) findViewById(R.id.editTextSurface);

        // Les radio button
        radioGroupVD = (RadioGroup) findViewById(R.id.radioGroupVD);


        spinnerClassement = (Spinner) findViewById(R.id.spinnerClassement);
        //Remplissage de la spinner
        ArrayAdapter<String> adapterClassement = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Classement.getClassements());
        spinnerClassement.setAdapter(adapterClassement);


        // Les boutons
        btnValid = (Button) findViewById(R.id.buttonValid);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Match match = new Match();
                Joueur j2 = new Joueur();

                j2.setNom(editTextJ2.getText().toString());
                j2.setClassement(Classement.convertirClassementString((String) spinnerClassement.getSelectedItem()));

                match.setJ1(mainProfil.getJoueurProfil());
                match.setJ2(j2);

                int selectedId = radioGroupVD.getCheckedRadioButtonId();
                if (selectedId == R.id.radioButtonV){
                    match.setResultat(1);
                }
                else{
                    match.setResultat(0);
                }
                match.setScore(editTextScore.getText().toString());
                match.setSurface(editTextSurface.getText().toString());

                // Ajout au profil
                mainProfil.getMatchs().add(match);


                //Lancement de la fenetre des matchs
                Intent intent = new Intent(MatchDetailActivity.this, MatchActivity.class);
                //Passage du profil
                intent.putExtra("profil", mainProfil);
                startActivity(intent);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de la fenetre des matchs
                Intent intent = new Intent(MatchDetailActivity.this, MatchActivity.class);
                //Passage du profil
                intent.putExtra("profil", mainProfil);
                startActivity(intent);
            }
        });
    }


}
