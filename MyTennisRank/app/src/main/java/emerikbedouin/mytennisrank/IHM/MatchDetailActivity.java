package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import emerikbedouin.mytennisrank.Controler.ProfilSingleton;
import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Joueur;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

public class MatchDetailActivity extends AppCompatActivity {

    //private Profil mainProfil;
    private int mode;
    private Match matchDetailed;
    private boolean fieldsValid;
    private ArrayAdapter<String> adapterClassement;

    // View
    private EditText editTextJ2, editTextScore, editTextSurface;
    private RadioGroup radioGroupVD;
    private Button btnValid, btnCancel;
    private Spinner spinnerClassement, spinnerFuturClassement;
    private CheckBox checkBoxBonusChpt, checkBoxWo;

    public MatchDetailActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_detail);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        try {
            if (getIntent().getExtras().get("mode") != null) {
                //mainProfil = getIntent().getExtras().getParcelable("profil");
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
            else{
                //mainProfil = new Profil();
                mode = 2;
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

        spinnerFuturClassement = (Spinner) findViewById(R.id.spinnerFuturClassement);
        spinnerFuturClassement.setAdapter(adapterClassement);

        // Checkbox du bonus championnat
        checkBoxBonusChpt = (CheckBox) findViewById(R.id.checkBoxBonusChpt);
        checkBoxWo = (CheckBox) findViewById(R.id.checkBoxWo);

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
                    //intent.putExtra("profil", (Parcelable) mainProfil);
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
                //intent.putExtra("profil", (Parcelable) mainProfil);
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

        spinnerFuturClassement.setSelection(adapterClassement.getPosition(Classement.convertirClassementInt(match.getJ2().getFuturClassement())));

        editTextScore.setText(match.getScore());

        editTextSurface.setText(match.getSurface());

        if(match.getGagnant().equals(match.getJ2())){
            radioGroupVD.check(R.id.radioButtonD);
        }
        else{
            radioGroupVD.check(R.id.radioButtonV);
        }

        if(match.getBonusChpt() == 1){
            checkBoxBonusChpt.setChecked(true);
        }
        else{
            checkBoxBonusChpt.setChecked(false);
        }

        if(match.getWo() == 1){
            checkBoxWo.setChecked(true);
        }
        else{
            checkBoxWo.setChecked(false);
        }

    }

    public Match getMatchFromEntry(){
        Match match = new Match();
        Joueur j2 = new Joueur();

        j2.setNom(editTextJ2.getText().toString());

        j2.setClassement(Classement.convertirClassementString((String) spinnerClassement.getSelectedItem()));

        j2.setFuturClassement(Classement.convertirClassementString((String) spinnerFuturClassement.getSelectedItem()));

        match.setJ1(ProfilSingleton.getInstance().getProfil().getJoueurProfil());
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

        if(checkBoxBonusChpt.isChecked()){
            match.setBonusChpt(1);
        }
        else{
            match.setBonusChpt(0);
        }

        if(checkBoxWo.isChecked()){
            match.setWo(1);
        }
        else{
            match.setWo(0);
        }

        return match;

    }

    public void addMatch(){

        // Ajout au profil
        if(ProfilSingleton.getInstance().getProfil() != null) {
            Match matchTemp = getMatchFromEntry();
            ProfilSingleton.getInstance().getProfil().getMatchs().add(matchTemp);
        }
        else{
            Toast.makeText(getApplicationContext(), "Erreur : Aucun profil !", Toast.LENGTH_LONG).show();
        }
    }

    public void modifyMatch(){

        if(ProfilSingleton.getInstance().getProfil() != null) {
            Match matchTemp = getMatchFromEntry();
            for (int i = 0; i < ProfilSingleton.getInstance().getProfil().getMatchs().size(); i++) {
                if (ProfilSingleton.getInstance().getProfil().getMatchs().get(i).equals(matchDetailed)) {

                    ProfilSingleton.getInstance().getProfil().getMatchs().remove(i);

                }
            }
            ProfilSingleton.getInstance().getProfil().getMatchs().add(matchTemp);
        }
        else{
            Toast.makeText(getApplicationContext(), "Erreur : Aucun profil !", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Controle des champs obligatoire
     * @return
     */
    public boolean controlField(){
        if (radioGroupVD.getCheckedRadioButtonId() == -1) return false;

        return true;
    }
}
