package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

public class MatchDetailActivity extends AppCompatActivity {

    private Profil mainProfil;
    private int mode;
    private Match matchDetailed;
    private boolean fieldsValid;
    private ArrayAdapter<String> adapterClassement;

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

        try {
            if (getIntent().getExtras().getParcelable("profil") != null) {
                mainProfil = getIntent().getExtras().getParcelable("profil");
                if(getIntent().getExtras().get("mode").equals("modify")){
                    mode = 1;
                    matchDetailed = getIntent().getExtras().getParcelable("match");
                }
                else if (getIntent().getExtras().get("mode").equals("add")){
                    mode = 2;
                }
                else{
                    mode = 0;
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            mode = 0;

        }

        // Recueperation des view ---------------------------------------------------
        // Les EditText
        editTextJ2 = (EditText) findViewById(R.id.editTextJ2);
        editTextScore = (EditText) findViewById(R.id.editTextScore);
        editTextSurface = (EditText) findViewById(R.id.editTextSurface);

        // Les radio button
        radioGroupVD = (RadioGroup) findViewById(R.id.radioGroupVD);


        spinnerClassement = (Spinner) findViewById(R.id.spinnerClassement);
        //Remplissage de la spinner
        adapterClassement = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Classement.getClassements());
        spinnerClassement.setAdapter(adapterClassement);


        // Les boutons
        btnValid = (Button) findViewById(R.id.buttonValid);
        btnCancel = (Button) findViewById(R.id.buttonCancel);

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(controlField()) {
                    if (mode == 1) {
                        modifyMatch();
                    } else if (mode == 2) {
                        addMatch();
                    }

                    //Lancement de la fenetre des matchs
                    Intent intent = new Intent(MatchDetailActivity.this, MatchActivity.class);
                    //Passage du profil
                    intent.putExtra("profil", (Parcelable) mainProfil);
                    startActivity(intent);
                }
                else{
                    // Match incomlet
                    Toast.makeText(getApplicationContext(), "Match incomplet", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Lancement de la fenetre des matchs
                Intent intent = new Intent(MatchDetailActivity.this, MatchActivity.class);
                //Passage du profil
                intent.putExtra("profil", (Parcelable) mainProfil);
                startActivity(intent);
            }
        });

        if(matchDetailed != null){
            fillMatchItem(matchDetailed);
        }
    }

    void fillMatchItem(Match match){
        editTextJ2.setText(match.getJ2().getNom());

        spinnerClassement.setSelection(adapterClassement.getPosition(Classement.convertirClassementInt(match.getJ2().getClassement())));

        editTextScore.setText(match.getScore());

        editTextSurface.setText(match.getSurface());

        if(match.getGagnant().equals(match.getJ2())){
            radioGroupVD.check(R.id.radioButtonD);
        }
        else{
            radioGroupVD.check(R.id.radioButtonV);
        }

    }

    public Match getMatchFromEntry(){
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

        return match;

    }

    public void addMatch(){
        Match matchTemp = getMatchFromEntry();
        // Ajout au profil
        mainProfil.getMatchs().add(matchTemp);
    }

    public void modifyMatch(){
        Match matchTemp = getMatchFromEntry();

        for (int i=0 ; i < mainProfil.getMatchs().size() ; i++){
            if(mainProfil.getMatchs().get(i).equals(matchDetailed)){
                mainProfil.getMatchs().remove(i);
            }
        }
        mainProfil.getMatchs().add(matchTemp);
    }

    public boolean controlField(){
        if (radioGroupVD.getCheckedRadioButtonId() == -1) return false;

        return true;
    }
}
