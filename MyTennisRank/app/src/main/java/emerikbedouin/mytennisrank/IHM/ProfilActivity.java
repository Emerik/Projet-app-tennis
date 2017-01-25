package emerikbedouin.mytennisrank.ihm;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import emerikbedouin.mytennisrank.BuildConfig;
import emerikbedouin.mytennisrank.dao.FileManager;
import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Activity destiné à l'ajout/modification d'un profil
 */
public class ProfilActivity extends AppCompatActivity {

    private Button btnCancel, btnValid;
    private EditText editTextName;
    private Spinner spinnerClassement;
    private ArrayAdapter<String> adapterClassement;
    private int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);

        initComp();
    }

    /**
     * Cette fonction initialise les composants de l'activity
     */
    public void initComp(){
        editTextName = (EditText) findViewById(R.id.editTextName);

        spinnerClassement = (Spinner) findViewById(R.id.spinnerClassement);
        //Remplissage de la spinner
        adapterClassement = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Classement.getClassements());
        spinnerClassement.setAdapter(adapterClassement);


        // Récupération des view boutons
        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnValid = (Button) findViewById(R.id.buttonValid);

        mode = (int) getIntent().getExtras().get("mode");


        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validProfil();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour à la fenêtre principale
                Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        if(mode == 1){
            editTextName.setText(ProfilSingleton.getInstance().getProfil().getNom());
            spinnerClassement.setSelection(adapterClassement.getPosition(Classement.convertirClassementInt(ProfilSingleton.getInstance().getProfil().getJoueurProfil().getClassement())));
        }

    }

    /**
     * Cette fonction va en fonction du mode créer ou modifier le profil principal
     */
    public void validProfil(){
        if(editTextName.getText().toString().length() > 0 && editTextName.getText().toString().length() < 25) {


            if(mode == 0) {
                // Creation nouveau profil
                Profil newProfil = new Profil();
                newProfil.setNom(editTextName.getText().toString());

                newProfil.getJoueurProfil().setNom(newProfil.getNom());
                newProfil.getJoueurProfil().setClassement(Classement.convertirClassementString((String) spinnerClassement.getSelectedItem()));

                ProfilSingleton.getInstance().setProfil(newProfil);
            }
            else if (mode == 1){
                // Modification du profil
                ProfilSingleton.getInstance().getProfil().setNom(editTextName.getText().toString());
                ProfilSingleton.getInstance().getProfil().getJoueurProfil().setClassement(Classement.convertirClassementString((String) spinnerClassement.getSelectedItem()));

            }


            // Enregistrement
            if( FileManager.saveProfilJSON(ProfilActivity.this, ProfilSingleton.getInstance().getProfil(), BuildConfig.SAVE_FILE_NAME) ){
                Toast.makeText(getApplicationContext(), "Profil sauvegardé", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Erreur : Profil non sauvegardé", Toast.LENGTH_LONG).show();
            }


            //Lancement de la fenetre principale
            Intent intent = new Intent(ProfilActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            // Nom absent ou trop long
            Toast.makeText(getApplicationContext(), "Nom absent ou trop long (25 caractères maximum)", Toast.LENGTH_LONG).show();
        }
    }



}
