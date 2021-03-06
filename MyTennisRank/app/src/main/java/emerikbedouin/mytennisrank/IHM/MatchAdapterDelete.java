package emerikbedouin.mytennisrank.ihm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.daimajia.swipe.adapters.BaseSwipeAdapter;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.dao.ProfilSingleton;
import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.R;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 06/04/16.
 */
public class MatchAdapterDelete extends BaseSwipeAdapter implements AdapterView.OnItemClickListener {

    private LinkedList<Match> alistMatchs;
    private Context context;

    public MatchAdapterDelete(Context context, LinkedList<Match> listMatchs){
        this.alistMatchs = listMatchs;
        this.context = context;

        //Trie
        this.alistMatchs = Match.sortDesc(listMatchs);

    }


    @Override
    public int getCount() {
        return alistMatchs.size();
    }

    @Override
    public Object getItem(int position) {
        return alistMatchs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return alistMatchs.indexOf(getItem(position));
    }


    @Override
    public int getSwipeLayoutResourceId(int position) {
        return R.id.swipeLayoutMatch;
    }


    //ATTENTION: Never bind listener or fill values in generateView.
    //           You have to do that in fillValues method.
    @Override
    public View generateView(int position, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_listviewmatch_v2, null);
        //final int pos = position; //sale
        /*view.findViewById(R.id.buttonDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Le delete", Toast.LENGTH_SHORT).show();
                //int pos = (int)v.getTag();
                listMatchs.remove(pos);
                MatchAdapterDelete.this.notifyDataSetChanged();

                // A faire autre part ! actualisation du profil suite à la suppression
                ProfilSingleton.getInstance().getProfil().setMatchs(listMatchs);
            }
        });*/

        return view;
    }

    @Override
    public void fillValues(final int position, View convertView) {
        ViewHolder viewHolder = null;

        // au premier appel ConvertView est null, on inflate notre layout
        //if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            //convertView = mInflater.inflate(R.layout.layout_listviewmatch, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewClass = (TextView) convertView.findViewById(R.id.textViewClassement);
            viewHolder.textViewNom = (TextView) convertView.findViewById(R.id.textViewNom);
            viewHolder.textViewScore = (TextView) convertView.findViewById(R.id.textViewScore);


            // nous attribuons comme tag notre MyViewHolder à convertView
            convertView.setTag(viewHolder);
        //} else {
            // convertView n'est pas null, nous récupérons notre objet MyViewHolder
            // et évitons ainsi de devoir retrouver les vues à chaque appel de getView
          //  viewHolder = (ViewHolder) convertView.getTag();
        //}

        // nous récupérons l'item de la liste demandé par getView
        Match match = (Match) getItem(position);

        // Attribution des valeurs aux vues
        viewHolder.textViewClass.setText(Classement.convertirClassementInt(match.getJ2().getClassement()));
        viewHolder.textViewNom.setText(String.valueOf(match.getJ2().getNom()));
        viewHolder.textViewScore.setText(String.valueOf(match.getScore()));

        // On place l'identifiant dans le tag du bouton
        convertView.findViewById(R.id.buttonDelete).setTag(position);

        convertView.findViewById(R.id.buttonDelete).setOnClickListener(new DeleteClickListener());



        /*convertView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Toast.makeText(context, "ça test dure !", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        return false;
                }

                return true;
            }
        });*/
    }


    public void deleteMatch(int position){

        Match tempMatch = alistMatchs.get(position);
        for (int i = 0; i < ProfilSingleton.getInstance().getProfil().getMatchs().size(); i++) {
            if (ProfilSingleton.getInstance().getProfil().getMatchs().get(i).equals(tempMatch)) {

                ProfilSingleton.getInstance().getProfil().getMatchs().remove(i);

            }
        }
        alistMatchs.remove(position);
        dataUpdated();

    }

    public void dataUpdated(){


        MatchAdapterDelete.this.notifyDataSetChanged();

    }

    private class ViewHolder {
        private TextView textViewClass, textViewNom, textViewScore;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Unused
    }

    public class DeleteClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //Toast.makeText(context, "Le delete", Toast.LENGTH_SHORT).show();
            int pos = (int) v.getTag();

            deleteMatch(pos);
        }
    }
}

