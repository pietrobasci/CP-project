package com.unisannio.multisala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.model.Film;

import java.util.ArrayList;

/**
 * Created by pietrobasci on 03/01/17.
 */
public class FilmInProgrammazioneAdapter extends ArrayAdapter<Film> {


    public FilmInProgrammazioneAdapter(Context context, int resource, ArrayList<Film> objects)
    {
        super(context, resource, objects);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_listview_film, null);

        TextView titolo = (TextView)convertView.findViewById(R.id.textView5);
        TextView genere = (TextView)convertView.findViewById(R.id.textView6);
        TextView durata = (TextView)convertView.findViewById(R.id.textView7);

        Film c = getItem(position);
        titolo.setText(c.getTitolo());
        genere.setText(getContext().getString(R.string.Genere_film)+c.getGenere());
        durata.setText(getContext().getString(R.string.Durata_film)+ Integer.toString(c.getDurata())+"'");

        return convertView;

    }

}