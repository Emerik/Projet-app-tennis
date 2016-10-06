package emerikbedouin.mytennisrank.ihm;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.modele.Classement;
import emerikbedouin.mytennisrank.modele.Match;
import emerikbedouin.mytennisrank.R;

/**
 * Copyright (C) 2016 Emerik Bedouin - All Rights Reserved
 * Created by emerikbedouin on 22/06/16.
 */
public class MatchAdapterPoints extends MatchAdapter implements AdapterView.OnItemClickListener {

    protected int mode;
    protected int modeCalcul;
    protected int classement;

    public MatchAdapterPoints(Context context, LinkedList<Match> listMatchs, int mode, int classement) {
        super(context, listMatchs);

        this.mode = mode;
        this.classement = classement;
        this.modeCalcul = 0;
    }

    public MatchAdapterPoints(Context context, LinkedList<Match> listMatchs, int mode, int classement, int modeCalcul) {
        super(context, listMatchs);

        this.mode = mode;
        this.classement = classement;
        this.modeCalcul = modeCalcul;
    }

    @Override
    public int getCount() {
        return listMatchs.size();
    }

    @Override
    public Object getItem(int position) {
        return listMatchs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return listMatchs.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        // au premier appel ConvertView est null, on inflate notre layout
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) context
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            convertView = mInflater.inflate(R.layout.layout_listviewmatch, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.textViewClass = (TextView) convertView.findViewById(R.id.textViewClassement);
            viewHolder.textViewNom = (TextView) convertView.findViewById(R.id.textViewNom);
            viewHolder.textViewScore = (TextView) convertView.findViewById(R.id.textViewScore);


            // nous attribuons comme tag notre MyViewHolder à convertView
            convertView.setTag(viewHolder);
        } else {
            // convertView n'est pas null, nous récupérons notre objet MyViewHolder
            // et évitons ainsi de devoir retrouver les vues à chaque appel de getView
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // nous récupérons l'item de la liste demandé par getView
        Match match = (Match) getItem(position);

        // Attribution des valeurs aux vues
        if(mode == 1) {
            viewHolder.textViewClass.setText(Classement.convertirClassementInt(Classement.modeClassementAdversaire(modeCalcul, match.getJ2())));
            viewHolder.textViewNom.setText(String.valueOf(match.getJ2().getNom()));
            viewHolder.textViewScore.setText(String.valueOf(Classement.ptsMatch(classement, Classement.modeClassementAdversaire(modeCalcul, match.getJ2()))));
        }
        else{
            viewHolder.textViewClass.setText(Classement.convertirClassementInt(match.getJ2().getClassement()));
            viewHolder.textViewNom.setText(String.valueOf(match.getJ2().getNom()));
            viewHolder.textViewScore.setText(String.valueOf(match.getScore()));
        }

        return convertView;
    }

    private class ViewHolder {
        public TextView textViewClass, textViewNom, textViewScore;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // For futur usage
    }
}
