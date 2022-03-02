package com.unisannio.multisala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.*;

import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FilmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film);

        Toolbar toolbar = (Toolbar) findViewById(R.id.MyToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        codicefilm = intent.getStringExtra("codiceFilm");

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapse_toolbar);
        collapsingToolbar.setTitle(intent.getStringExtra("titolo"));
        collapsingToolbar.setExpandedTitleColor(Color.alpha(0));
        collapsingToolbar.setCollapsedTitleTextColor(Color.WHITE);
        img = (ImageView) findViewById(R.id.bgheader);

        byte[] cop = intent.getByteArrayExtra("copertina");           //im.setBackgroundResource(R.drawable.logo1);
        Bitmap bitmap = BitmapFactory.decodeByteArray(cop, 0, cop.length);
        img.setImageBitmap(bitmap);

        regista=(TextView)findViewById(R.id.textView6);
        genere=(TextView)findViewById(R.id.textView7);
        durata=(TextView)findViewById(R.id.textView8);
        descrizione=(TextView)findViewById(R.id.textView5);

        regista.setText(intent.getStringExtra("regista"));
        genere.setText(intent.getStringExtra("genere"));
        durata.setText(intent.getStringExtra("durata") + "'");
        descrizione.setText(intent.getStringExtra("descrizione"));

        expandableListView= (ExpandableListView) findViewById(R.id.expandableListView);

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if (prefs.getString("Authentication_Status", null).equalsIgnoreCase("online")) {
                    final String selected = (String) expandableListAdapter.getChild(groupPosition, childPosition);
                    final String data = (String) expandableListAdapter.getGroup(groupPosition);

                    Intent inten = new Intent(FilmActivity.this, PrenotazioneActivity.class);
                    inten.putExtra("titoloFilm", collapsingToolbar.getTitle().toString());
                    inten.putExtra("data", data);
                    inten.putExtra("ora", selected);
                    for (Programmazione p : progr) {
                        if (p.getOra().equalsIgnoreCase(selected) && p.getData().equalsIgnoreCase(data) && p.getNomeFilm().equalsIgnoreCase(collapsingToolbar.getTitle().toString())) {
                            inten.putExtra("sala", p.getSala());
                            inten.putExtra("codiceProg", p.getCodice());
                        }
                    }
                    startActivity(inten);
                    return true;
                } else {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                    SharedPreferences.Editor editor = prefs.edit();
                                    editor.putBoolean("Richiesta_login", true).apply();
                                    Intent vailogin=new Intent(FilmActivity.this, MainActivity.class);
                                    startActivity(vailogin);
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(FilmActivity.this);
                    builder.setMessage(getString(R.string.devi_effettuare_login)).setPositiveButton(getString(R.string.loginString), dialogClickListener).setNegativeButton(getString(R.string.annulla), dialogClickListener).show();
                    return true;
                }
            }
        });


        new GetProgrammazioneFilm().execute();


    }

    public class GetProgrammazioneFilm extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            dateProgrammazione = new ArrayList<String>();
            orariProgrammazione = new HashMap<String, ArrayList<String>>();

            try{
                ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/progAll/"+codicefilm);
                Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
                progr = new Programmazione[0];
                String json=clientResource.get().getText();
                org.restlet.data.Status stato = clientResource.getStatus();
                if (stato.getCode() != 200) {
                    switch (stato.getCode()) {
                        case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_database, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    TextView nessunProgramm=(TextView)findViewById(R.id.textView52);
                                    nessunProgramm.setText(getString(R.string.nessuna_prog_pre_il_film));
                                    nessunProgramm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                                }
                            });
                            break;
                    }
                }
                else {
                    progr = gson.fromJson(json, Programmazione[].class);
                    if (progr[0] != null) {
                        Programmazione pro = progr[0];
                        String data = pro.getData();
                        dateProgrammazione.add(data);
                        for (Programmazione p : progr) {
                            if (!data.equalsIgnoreCase(p.getData())) {
                                dateProgrammazione.add(p.getData());
                                data = p.getData();
                            }
                        }
                    }

                    int i =0;
                    for(String date:dateProgrammazione){
                        ArrayList<String> orari=new ArrayList<>();
                        for(Programmazione p:progr){
                            if(date.equalsIgnoreCase(p.getData())){
                                orari.add(p.getOra());
                                orariProgrammazione.put(dateProgrammazione.get(i),orari);
                            }
                        }
                        i++;
                    }

                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        if(!dateProgrammazione.isEmpty()) {
                            //expandableListView.setVisibility(View.VISIBLE);
                            expandableListAdapter = new ExpandableListAdapter(FilmActivity.this, dateProgrammazione, orariProgrammazione);
                            expandableListView.setAdapter(expandableListAdapter);
                        }
                        else{
                            TextView nessunProgramm=(TextView)findViewById(R.id.textView52);
                            nessunProgramm.setText(getString(R.string.nessuna_prog_pre_il_film));
                            nessunProgramm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                        }
                    }
                });
            }
            catch (ResourceException | IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {

                        Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_server, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
            return null;
        }

    }

    private String codicefilm;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView img;
    private TextView regista, genere, durata, descrizione;
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;
    private ArrayList<String> dateProgrammazione;
    private HashMap<String, ArrayList<String>> orariProgrammazione;
    private Programmazione[] progr;

}
