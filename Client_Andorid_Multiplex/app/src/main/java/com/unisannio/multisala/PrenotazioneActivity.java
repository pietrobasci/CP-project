package com.unisannio.multisala;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import java.io.IOException;
import java.util.UUID;
import com.model.Prenotazione;
import com.model.Costanti;

public class PrenotazioneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prenotazione);

        Intent intent = getIntent();

        titolo=(TextView)findViewById(R.id.textView34);
        data=(TextView)findViewById(R.id.textView36);
        ora=(TextView)findViewById(R.id.textView38);
        sala=(TextView)findViewById(R.id.textView40);

        titol=intent.getStringExtra("titoloFilm");
        dat=intent.getStringExtra("data");
        or=intent.getStringExtra("ora");
        sal=intent.getStringExtra("sala");
        codProg=intent.getStringExtra("codiceProg");

        titolo.setText(titol);
        data.setText(dat);
        ora.setText(or);
        sala.setText(sal);

    }

    public void procedi(View view) {

        postiPrenotati=(EditText)findViewById(R.id.editText9);
        String postiPre=postiPrenotati.getText().toString();
        if(!postiPre.isEmpty()) {
            setContentView(R.layout.conferma_prenotazione);

            titolo=(TextView)findViewById(R.id.textView34);
            data=(TextView)findViewById(R.id.textView36);
            ora=(TextView)findViewById(R.id.textView38);
            sala=(TextView)findViewById(R.id.textView40);

            EditText biglietti = (EditText) findViewById(R.id.editText9);
            biglietti.setText(postiPre);

            TextView totale = (TextView) findViewById(R.id.textView51);
            int total = (Integer.parseInt(postiPre) * 5) + Integer.parseInt(postiPre);
            totale.setText(total + getString(R.string.euro));

            titolo.setText(titol);
            data.setText(dat);
            ora.setText(or);
            sala.setText(sal);
        }
        else{
            Toast toast1 = Toast.makeText(getApplicationContext(), R.string.inserire_posti_da_prenotare, Toast.LENGTH_SHORT);
            toast1.show();
        }

    }

    public void confermaPrenotazione(View view) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PrenotazioneActivity.this);
        alertDialogBuilder.setTitle(R.string.conferma);
        alertDialogBuilder.setMessage(getString(R.string.conf_pren_di)+" "+postiPrenotati.getText().toString()+" "+getString(R.string.n_posti)+" "+titol+" "+getString(R.string.del)+" "+dat+" "+getString(R.string.ora)+" "+or);
        alertDialogBuilder.setPositiveButton(R.string.si, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                new PostPrenotazione().execute();
            }
        }).setNegativeButton(R.string.no,new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public class PostPrenotazione extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            String codicePren= UUID.randomUUID().toString();
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            String token=prefs.getString("Token", null);
            String userOnline=prefs.getString("UserOnLine", null);

            try {
                Prenotazione pr = new Prenotazione(codicePren, userOnline, codProg,Integer.parseInt(postiPrenotati.getText().toString()));
                String uri = "http://10.0.2.2:8182/prenotazioni/"+codProg+"&"+token;
                ClientResource cr = new ClientResource(uri);
                Series<Header> responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );

                String json = cr.post(gson.toJson(pr,Prenotazione.class)).getText();
                org.restlet.data.Status stato = cr.getStatus();
                if(stato.getCode()!=200) {
                    switch(stato.getCode()) {
                        case Costanti.ECCEZIONE_SALA_INESISTENTE:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.errore_sala_inesistente, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
                        case Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.non_autorizzato, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;

                        case Costanti.ECCEZIONE_POSTI_INSUFFICIENTI:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.errore_posti, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;

                        case Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(),R.string.errore_programmazione_inesistente, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
                        case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.errore_database, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
                    }
                }
                else
                {
                    final Prenotazione p = gson.fromJson(json, Prenotazione.class);
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            Intent i = new Intent(PrenotazioneActivity.this,MainActivity.class);
                            i.putExtra("codiceP",p.getCodice());

                            SharedPreferences prenotazEffettuata=PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            SharedPreferences.Editor editor = prenotazEffettuata.edit();
                            editor.putBoolean("Prenotazione_eseguita",true).apply();
                            startActivity(i);
                            PrenotazioneActivity.this.finish();
                        }
                    });
                }
            }
            catch (ResourceException | IOException e)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast toast1 = Toast.makeText(getApplicationContext(), getString(R.string.errore_server), Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                });
            }
            catch (NumberFormatException e)
            {
                runOnUiThread(new Runnable()
                {
                    public void run()
                    {
                        Toast toast1 = Toast.makeText(getApplicationContext(),R.string.inserisci_numero_valido, Toast.LENGTH_SHORT);
                        toast1.show();
                    }
                });
            }

            return null;
        }
    }

    private TextView titolo, data, ora, sala;
    private EditText postiPrenotati;
    private String titol, dat, or, sal, codProg;


}
