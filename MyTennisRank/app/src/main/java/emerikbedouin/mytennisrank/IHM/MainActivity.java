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
import emerikbedouin.mytennisrank.modele.Epreuve;
import emerikbedouin.mytennisrank.modele.Joueur;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.modele.Profil;


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
            case R.id.action_modify_profil:
                modifyProfil();
                return true;
            case R.id.action_save_profil:
                saveProfil();
                return true;
            case R.id.action_delete_profil:
                deleteProfil();
                return true;
            case R.id.action_demo_profil:
                demoProfil();
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
        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
        intent.putExtra("mode",0);
        startActivity(intent);
    }

    public void modifyProfil(){
        //Lancement de la fenetre de création d'un nouveau profil
        Intent intent = new Intent(MainActivity.this, ProfilActivity.class);
        intent.putExtra("mode",1);
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

    public void deleteProfil(){
        if(ProfilSingleton.getInstance().getProfil() != null) {

            boolean success = FileManager.deleteProfil(this, "profil.txt");

            if(success){
                ProfilSingleton.getInstance().setProfil(null);
                Toast.makeText(getApplicationContext(), "Profil supprimé !", Toast.LENGTH_LONG).show();
                ((ViewPagerAdapter) viewPager.getAdapter()).updateItems();
            }
            else {
                Toast.makeText(getApplicationContext(), "Erreur !", Toast.LENGTH_LONG).show();
            }

            System.out.println("Profil encore la : "+ProfilSingleton.getInstance().getProfil());

        }
    }

    public void demoProfil(){

        creationProfilComplet();
        ((ViewPagerAdapter) viewPager.getAdapter()).updateItems();
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


    // Pour les tests ------ To delete
    public void creationProfilComplet(){

        Profil p = new Profil();

        p.setNom("Federer");

        Joueur j1 = new Joueur(0, "Roger Federer", 10, 10, 0); // Principal 15/3
        Joueur j2 = new Joueur(0, "David", 10, 9, 0);
        Joueur j3 = new Joueur(0, "Yannick", 11, 10, 0);
        Joueur j4 = new Joueur(0, "Victor", 9, 8, 0);
        Joueur j5 = new Joueur(0, "gwenael", 9, 8, 0);
        Joueur j6 = new Joueur(0, "Jerome", 8, 8, 0);
        Joueur j7 = new Joueur(0, "Gaylor", 8, 7, 0);
        Joueur j8 = new Joueur(0, "Sebastien", 8, 9, 0);
        Joueur j9 = new Joueur(0, "Noam", 11, 11, 0);
        Joueur j10 = new Joueur(0, "Pierre", 10, 11, 0);



        Match m1 = new Match(j1, j2, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m2 = new Match(j1, j3, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m3 = new Match(j1, j4, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m4 = new Match(j1, j5, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m5 = new Match(j1, j6, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m6 = new Match(j1, j7, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m7 = new Match(j1, j8, "6/0,6/0","surface",1, new Epreuve(),0,0);
        Match m8 = new Match(j1, j6, "6/0,6/0","surface",1, new Epreuve(),0,0);

        Match m9 = new Match(j1, j9, "6/0,6/0","surface",0, new Epreuve(),0,0);
        Match m10 = new Match(j1, j10, "6/0,6/0","surface",0, new Epreuve(),0,0);

        p.setJoueurProfil(j1);

        p.getMatchs().add(m1);
        p.getMatchs().add(m2);
        p.getMatchs().add(m3);
        p.getMatchs().add(m4);
        p.getMatchs().add(m5);
        p.getMatchs().add(m6);
        p.getMatchs().add(m7);
        p.getMatchs().add(m8);
        p.getMatchs().add(m9);
        p.getMatchs().add(m10);

        ProfilSingleton.getInstance().setProfil(p);

        Toast.makeText(this.getApplicationContext(), "Profil temp !", Toast.LENGTH_LONG).show();

    }

}

