package emerikbedouin.mytennisrank.ihm;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import emerikbedouin.mytennisrank.R;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import emerikbedouin.mytennisrank.dao.FileManager;
import emerikbedouin.mytennisrank.dao.ProfilSingleton;


public class MainActivity extends AppCompatActivity {

    //private Profil mainProfil = null;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Recuperation d'un eventuel profil en memoire
        loadProfil();

        setContentView(R.layout.activity_main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.balle);

        toolbar.setTitle("Tennis Rank");

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        try {
            if (getIntent().getExtras().get("tab") != null) {
                viewPager.setCurrentItem(1);
            }

        }
        catch (NullPointerException ex){
            System.out.println("[ERROR] MainActivity "+ex.getMessage());
        }


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
                settings();
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


    // Fonction des items du menu

    public void settings(){
        // To define
    }

    public void newProfil(){
        //Lancement de la fenetre de création d'un nouveau profil
        Intent intent = new Intent(MainActivity.this, NewProfilActivity.class);
        startActivity(intent);
    }

    public void saveProfil(){

        boolean res = FileManager.saveProfil(this, ProfilSingleton.getInstance().getProfil(), "profil.txt");

        if (res)
            Toast.makeText(getApplicationContext(), "Profil sauvegardé !", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(getApplicationContext(), "Erreur", Toast.LENGTH_LONG).show();

    }

    public void loadProfil(){

        if(ProfilSingleton.getInstance().getProfil() == null) {

            ProfilSingleton.getInstance().setProfil( FileManager.loadProfil(this, "profil.txt") );

            if (ProfilSingleton.getInstance().getProfil() != null)
                Toast.makeText(getApplicationContext(), "Profil chargé !", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getApplicationContext(), "Aucun profil en mémoire", Toast.LENGTH_LONG).show();
        }

        // Else c'est déja chargé

    }


    private void setupViewPager(final ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainActivityFragment(), "Bilan");
        adapter.addFragment(new MatchFragment(), "Matchs");
        viewPager.setAdapter(adapter);

    }


    public ViewPager getViewPager(){
        return viewPager;
    }


}

