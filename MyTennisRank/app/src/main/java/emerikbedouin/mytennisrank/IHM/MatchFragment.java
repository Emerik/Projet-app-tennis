package emerikbedouin.mytennisrank.IHM;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.DAO.ProfilSingleton;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.R;

/**
 * Created by emerikbedouin on 16/08/16.
 */
public class MatchFragment extends Fragment {

    // View
    private ListView listViewVictoire, listViewDefaite;
    private FloatingActionButton btnAddMatch;

    public MatchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_match, container, false);




        if(ProfilSingleton.getInstance().getProfil() != null){
            initComposant(rootView);
        }

        return rootView;
    }


    void initComposant(View rootView){

        btnAddMatch = (FloatingActionButton) rootView.findViewById(R.id.btnFloatAddMatch);
        btnAddMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMatch();
            }
        });




        listViewVictoire = (ListView) rootView.findViewById(R.id.listViewV);
        listViewDefaite = (ListView) rootView.findViewById(R.id.listViewD);

        LinkedList<Match> listMatch = ProfilSingleton.getInstance().getProfil().getMatchs();
        LinkedList<Match> listVictoire = new LinkedList<>();
        LinkedList<Match> listDefaite = new LinkedList<>();


        for(int i=0; i < listMatch.size() ; i++){
            if(listMatch.get(i).getGagnant().equals(ProfilSingleton.getInstance().getProfil().getJoueurProfil()) ){
                listVictoire.add(listMatch.get(i));
            }
            else {
                listDefaite.add(listMatch.get(i));
            }
        }

        //Définition de l'adapter
        // ArrayAdapter<Match> adapterV = new ArrayAdapter<Match>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listVictoire);
        // ArrayAdapter<Match> adapterD = new ArrayAdapter<Match>(this, android.R.layout.simple_list_item_1, android.R.id.text1, listDefaite);
        MatchAdapterDelete adapterV = new MatchAdapterDelete(getActivity(), listVictoire);
        MatchAdapterDelete adapterD = new MatchAdapterDelete(getActivity(), listDefaite);
        listViewVictoire.setAdapter(adapterV);
        listViewDefaite.setAdapter(adapterD);
        //On click
        listViewVictoire.setOnItemClickListener(new ClickOnItemMatch());
        listViewDefaite.setOnItemClickListener(new ClickOnItemMatch());

    }

    public void addMatch(){
        //Lancement de la fenetre d'ajout de match
        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
        intent.putExtra("mode","add");
        startActivity(intent);
    }

    public void swipeToDelete(View rootView){
        SwipeLayout swipeLayout =  (SwipeLayout)rootView.findViewById(R.id.swipeLayoutMatch);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, getActivity().findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
                Toast.makeText( getActivity().getApplicationContext(), "Animer", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });
    }

    public void modifyMatch(Match matchSelected){
        // Lancement de l'activité MatchDetailActivity
        Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
        //Passage du profil
        //intent.putExtra("profil", (Parcelable) mainProfil);
        intent.putExtra("mode","modify");
        intent.putExtra("match", (Parcelable) matchSelected);
        startActivity(intent);
    }

    class ClickOnItemMatch implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            // l'index de l'item dans notre ListView
            int itemPosition = position;

            // On récupère le match  cliqué
            Match matchSelected = (Match) parent.getItemAtPosition(position);

            // Lancement de l'activité MatchDetailActivity
            Intent intent = new Intent(getActivity(), MatchDetailActivity.class);
            //Passage du profil
            //intent.putExtra("profil", (Parcelable) mainProfil);
            intent.putExtra("mode","modify");
            intent.putExtra("match", (Parcelable) matchSelected);
            startActivity(intent);



        }

    }


}