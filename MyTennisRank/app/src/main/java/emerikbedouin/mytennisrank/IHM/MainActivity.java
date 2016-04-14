package emerikbedouin.mytennisrank.IHM;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import emerikbedouin.mytennisrank.Controler.FileManager;
import emerikbedouin.mytennisrank.Modele.Profil;
import emerikbedouin.mytennisrank.R;

public class MainActivity extends AppCompatActivity {

    private Profil mainProfil = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Recuperation d'un eventuel profil en memoire
        if(mainProfil == null) {
            loadProfil();
        }

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.balle);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case R.id.action_settings:

                return true;
            case R.id.action_new_profil:
                newProfil();
                return true;
            case R.id.action_save_profil:
                saveProfil();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    // Fonction

    public Profil getMainProfil() {
        return mainProfil;
    }

    public void setMainProfil(Profil mainProfil) {
        this.mainProfil = mainProfil;
    }


    // Fonction des items du menu

    public void newProfil(){
        //Lancement de la fenetre de création d'un nouveau profil
        Intent intent = new Intent(MainActivity.this, NewProfilActivity.class);
        startActivity(intent);
    }

    public void saveProfil(){

        int res = FileManager.saveProfil(this, mainProfil, "profil.txt");

        if (res == 1)
            Toast.makeText(getApplicationContext(), "Profil sauvegardé !", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();

    }

    public void loadProfil(){

        mainProfil = FileManager.loadProfil(this, "profil.txt");

        if (mainProfil != null)
            Toast.makeText(getApplicationContext(), "Profil chargé !", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();
    }
}
