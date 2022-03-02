package com.unisannio.multisala;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.model.Prenotazione;
import com.model.Programmazione;

import java.util.ArrayList;

/**
 * Created by pietrobasci on 08/01/17.
 */
public class PrenotazioneAdapter extends ArrayAdapter<Prenotazione> {

    public PrenotazioneAdapter(Context context, int resource, ArrayList<Prenotazione> objects, ArrayList<Programmazione> prog) {
        super(context, resource, objects);
        this.progr=prog;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.layout_listview_prenotazioni, null);

        TextView nomeFilmPrenotato = (TextView)convertView.findViewById(R.id.textView13);
        TextView dataOra = (TextView)convertView.findViewById(R.id.textView14);
        TextView posti = (TextView) convertView.findViewById(R.id.textView15);
        TextView statoPrenotazione = (TextView)convertView.findViewById(R.id.textView16);

        Prenotazione c = getItem(position);
        for(Programmazione p:progr){
            if(c.getProgrammazione().equalsIgnoreCase(p.getCodice())){
                nomeFilmPrenotato.setText(p.getNomeFilm());
                dataOra.setText(getContext().getString(R.string.evento)+p.getData()+" "+ p.getOra());
                posti.setText(getContext().getString(R.string.posti) + c.getNumeroPosti());
                statoPrenotazione.setText(getContext().getString(R.string.stato_prenotazione));
                break;
            }
        }

        return convertView;

    }

    private ArrayList<Programmazione> progr;
}
