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

import emerikbedouin.mytennisrank.dao.FileManager;
import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Profil;
import emerikbedouin.mytennisrank.R;

public class NewProfilActivity extends AppCompatActivity {

    private Button btnCancel, btnValid;
    private EditText editTextName;
    private Spinner spinnerClassement;
    private ArrayAdapter<String> adapterClassement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_profil);


        editTextName = (EditText) findViewById(R.id.editTextName);

        spinnerClassement = (Spinner) findViewById(R.id.spinnerClassement);
        //Remplissage de la spinner
        adapterClassement = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Classement.getClassements());
        spinnerClassement.setAdapter(adapterClassement);



        btnCancel = (Button) findViewById(R.id.buttonCancel);
        btnValid = (Button) findViewById(R.id.buttonValid);

        btnValid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(editTextName.getText().toString().length() > 0 && editTextName.getText().toString().length() < 25) {

                    Profil newProfil = new Profil();
                    newProfil.setNom(editTextName.getText().toString());

                    newProfil.getJoueurProfil().setNom(newProfil.getNom());
                    newProfil.getJoueurProfil().setClassement(Classement.convertirClassementString((String) spinnerClassement.getSelectedItem()));


                    // Enregistrement
                    if( FileManager.saveProfil(NewProfilActivity.this, newProfil, "profil.txt") ){
                        Toast.makeText(getApplicationContext(), "Profil sauvegardé", Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Erreur : Profil non sauvegardé", Toast.LENGTH_LONG).show();
                    }

                    ProfilSingleton.getInstance().setProfil(newProfil);

                    //Lancement de la fenetre principale
                    Intent intent = new Intent(NewProfilActivity.this, MainActivity.class);
                    //Passage du profil
                    //intent.putExtra("profil", (Parcelable) newProfil);
                    startActivity(intent);
                }
                else{
                    // Nom absent ou trop long
                    Toast.makeText(getApplicationContext(), "Nom absent ou trop long (25 caractères maximum)", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retour à la fenêtre principale
                Intent intent = new Intent(NewProfilActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}
