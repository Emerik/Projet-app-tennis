package emerikbedouin.mytennisrank.IHM;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.LinkedList;

import emerikbedouin.mytennisrank.Modele.Classement;
import emerikbedouin.mytennisrank.Modele.Match;
import emerikbedouin.mytennisrank.R;

/**
 * Created by emerikbedouin on 06/04/16.
 */
abstract class MatchAdapter extends BaseAdapter {

    protected LinkedList<Match> listMatchs;
    protected Context context;

    public MatchAdapter(Context context, LinkedList<Match> listMatchs){
        this.listMatchs = listMatchs;
        this.context = context;

    }

    @Override
    public abstract int getCount();

    @Override
    public abstract Object getItem(int position);

    @Override
    public abstract long getItemId(int position);

    @Override
    public abstract View getView(int position, View convertView, ViewGroup parent);


}
