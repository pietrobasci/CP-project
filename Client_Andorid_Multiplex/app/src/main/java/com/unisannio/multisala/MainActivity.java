package com.unisannio.multisala;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.model.*;

import org.restlet.engine.Engine;
import org.restlet.engine.connector.HttpClientHelper;
import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.logo1);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        filmInprogrammazione = (ListView) findViewById(R.id.listView);
        filmInprogrammazione.setOnItemClickListener(MainActivity.this);

        final Engine engine = Engine.getInstance();
        engine.getRegisteredClients().clear();
        engine.getRegisteredClients().add(new HttpClientHelper(null));

        new GetFilmAll().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(prefs.getBoolean("Prenotazione_eseguita", false) && prefs.getString("Authentication_Status", null).equalsIgnoreCase("online")){

            setContentView(R.layout.prenotazioni_effettuate);
            prenotazioniEffettutate = (ListView) findViewById(R.id.listView);
            prenotazioniEffettutate.setOnItemClickListener(MainActivity.this);
            new GetPrenotaAll().execute();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Prenotazione_eseguita", false).apply();
            Intent i = new Intent(this, NotificationService.class);
            startService(i);

        } else if(prefs.getBoolean("Richiesta_login", false)){

            setContentView(R.layout.login);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Richiesta_login", false).apply();

        } else if(prefs.getBoolean("Richiesta_notifica", false)){

            setContentView(R.layout.prenotazioni_effettuate);
            prenotazioniEffettutate = (ListView) findViewById(R.id.listView);
            prenotazioniEffettutate.setOnItemClickListener(MainActivity.this);
            new GetPrenotaAll().execute();
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("Richiesta_notifica", false).apply();

        }

    }

    public void onImageButtonClicked(View v) {
        switch (v.getId()){
            case R.id.imageButton:
                setContentView(R.layout.activity_main);
                filmInprogrammazione = (ListView) findViewById(R.id.listView);
                filmInprogrammazione.setOnItemClickListener(MainActivity.this);
                new GetFilmAll().execute();
                break;

            case R.id.imageButton2:
                setContentView(R.layout.prenotazioni_effettuate);
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if(prefs.getString("Authentication_Status", null).equalsIgnoreCase("online")) {
                    prenotazioniEffettutate = (ListView) findViewById(R.id.listView);
                    prenotazioniEffettutate.setOnItemClickListener(MainActivity.this);
                    new GetPrenotaAll().execute();
                } else {
                    prenotazioniEffettutate = (ListView) findViewById(R.id.listView);
                    prenotazioniEffettutate.setOnItemClickListener(MainActivity.this);
                    TextView nessunProgramm=(TextView)findViewById(R.id.textView20);
                    nessunProgramm.setText(getString(R.string.prenotazioni_effettua_login));
                    nessunProgramm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                }
                break;

            case R.id.imageButton3:
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                if(!pref.getString("Authentication_Status", null).equalsIgnoreCase("online")){
                    setContentView(R.layout.login);
                    user = (EditText) findViewById(R.id.editText);
                    pass = (EditText) findViewById(R.id.editText2);
                }
                else {
                    setContentView(R.layout.logout);
                    TextView usr = (TextView) findViewById(R.id.textView22);
                    usr.setText(pref.getString("UserOnLine", null));
                }
                break;

            default:
                setContentView(R.layout.activity_main);
                break;
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getAdapter() instanceof FilmInProgrammazioneAdapter) {

            Film film = (Film) parent.getItemAtPosition(position);
            Intent intent = new Intent(getApplicationContext(), FilmActivity.class);
            intent.putExtra("codiceFilm", film.getCodice());
            intent.putExtra("titolo", film.getTitolo());
            intent.putExtra("regista", film.getRegista());
            intent.putExtra("genere", film.getGenere());
            intent.putExtra("durata", Integer.toString(film.getDurata()));
            intent.putExtra("descrizione", film.getDescrizione());
            intent.putExtra("copertina", film.getCopertina());
            startActivity(intent);

        } else if (parent.getAdapter() instanceof PrenotazioneAdapter) {

            final Prenotazione prenotazione = (Prenotazione) parent.getItemAtPosition(position);
            final Dialog dialog = new Dialog(MainActivity.this, R.style.Dialog);
            dialog.setContentView(R.layout.custom_dialog);
            dialog.setTitle(R.string.dettagli_prenotazione);

            TextView codicePreno=(TextView)dialog.findViewById(R.id.textView29);
            TextView film=(TextView)dialog.findViewById(R.id.textView30);
            TextView dataora=(TextView)dialog.findViewById(R.id.textView31);
            TextView posti=(TextView)dialog.findViewById(R.id.textView32);
            codicePreno.setText(getString(R.string.codice_string)+" "+prenotazione.getCodice());
            posti.setText(getString(R.string.posti_prenotati)+" "+prenotazione.getNumeroPosti());

            TextView evento=(TextView)parent.getAdapter().getView(position, view, parent).findViewById(R.id.textView14);
            TextView titolo=(TextView)parent.getAdapter().getView(position, view, parent).findViewById(R.id.textView13);

            //TextView evento=(TextView)parent.findViewById(R.id.textView14);
            //TextView titolo=(TextView)parent.findViewById(R.id.textView13);
            String dat=evento.getText().toString();
            dat=dat.substring(11,27);
            film.setText(getString(R.string.film)+" "+titolo.getText().toString());
            dataora.setText(getString(R.string.data_ora_evento)+" "+dat);

            Button chiudi = (Button) dialog.findViewById(R.id.button6);
            chiudi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            Button annullaPre = (Button) dialog.findViewById(R.id.button5);
            annullaPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            switch (which){
                                case DialogInterface.BUTTON_POSITIVE:
                                    codicePrenotazione=prenotazione.getCodice();
                                    new DeletePrenotazione().execute();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    break;
                            }
                        }
                    };
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage(getString(R.string.annullare_prenotazione)).setPositiveButton(getString(R.string.annulla_prenotazione), dialogClickListener).setNegativeButton(getString(R.string.chiudi), dialogClickListener).show();
                    dialog.dismiss();
                }
            });

            dialog.show();

        }

    }

    public void login(View view){
        if(!user.getText().toString().isEmpty() && !pass.getText().toString().isEmpty())
            new Login().execute();
        else{
            Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_campi_incompleti, Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void registrati(View view){
        Intent registr=new Intent(this,RegistrazioneActivity.class);
        startActivity(registr);
    }

    public void logout(View view){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        setContentView(R.layout.login);
                        user = (EditText) findViewById(R.id.editText);
                        pass = (EditText) findViewById(R.id.editText2);
                        new LogoutAndDeleteToken().execute();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Sei sicuro di voler uscire?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
    }

    public class GetFilmAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            final ArrayList<Film> films = new ArrayList<Film>();
            try{
                ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/film/all/");
                Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
                Film[] pFilm = new Film[0];
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

                                    Toast toast = Toast.makeText(getApplicationContext(),R.string.errore_generico , Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                } else
                    pFilm = gson.fromJson(json, Film[].class);


                int i = 0;
                while (i < pFilm.length) {
                    films.add(pFilm[i]);
                    i++;
                }
                runOnUiThread(new Runnable() {
                    public void run() {
                        if(!films.isEmpty()) {
                            adapter = new FilmInProgrammazioneAdapter(getApplicationContext(), R.layout.layout_listview_film, films);
                            filmInprogrammazione.setAdapter(adapter);
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

    public class GetPrenotaAll extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            final ArrayList<Prenotazione> prenotazion = new ArrayList<Prenotazione>();
            final ArrayList<Programmazione> progPre = new ArrayList<Programmazione>();

            try{
                SharedPreferences tokenEuser = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                usernameOnline=tokenEuser.getString("UserOnLine", null);
                token=tokenEuser.getString("Token", null);
                ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/prenotazioniAll/username/"+usernameOnline+"&"+token);
                Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
                Prenotazione[] prenotazioi = new Prenotazione[0];
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
                        case Costanti.ECCEZIONE_PRENOTAZIONI_INESISTENTI:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    TextView nessunProgramm=(TextView)findViewById(R.id.textView20);
                                    nessunProgramm.setText(getString(R.string.nessuna_prenotazione));
                                    nessunProgramm.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
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

                        default:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                }
                else
                    prenotazioi = gson.fromJson(json, Prenotazione[].class);


                int i = 0;
                while (i < prenotazioi.length) {
                    prenotazion.add(prenotazioi[i]);
                    i++;
                }
                clientResource = new ClientResource("http://10.0.2.2:8182/prog/all/");
                responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
                Programmazione[] programmaz = new Programmazione[0];
                json=clientResource.get().getText();
                stato = clientResource.getStatus();
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

                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                }
                else
                    programmaz = gson.fromJson(json, Programmazione[].class);


                i = 0;
                while (i < programmaz.length) {
                    progPre.add(programmaz[i]);
                    i++;
                }

                runOnUiThread(new Runnable() {
                    public void run() {
                        if(!prenotazion.isEmpty()) {

                            adapter2 = new PrenotazioneAdapter(getApplicationContext(), R.layout.layout_listview_prenotazioni, prenotazion, progPre);
                            prenotazioniEffettutate.setAdapter(adapter2);
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

    public class DeletePrenotazione extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            Gson gson = new Gson();
            final ArrayList<Film> films = new ArrayList<Film>();
            try{
                SharedPreferences tokenEuser = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                token=tokenEuser.getString("Token", null);
                ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/prenotazioni/username/deletePrenot/"+codicePrenotazione+"&"+token);
                Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
                String json=clientResource.delete().getText();
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
                        case Costanti.ECCEZIONE_PRENOTAZIONI_INESISTENTI:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.nessuna_prenotazione, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
                        case Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE:
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    Toast toast1 = Toast.makeText(getApplicationContext(), R.string.errore_programmazione_inesistente, Toast.LENGTH_SHORT);
                                    toast1.show();
                                }
                            });
                            break;
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
                        default:
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run() {

                            Toast toast = Toast.makeText(getApplicationContext(), R.string.prenotazione_annullata, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setContentView(R.layout.prenotazioni_effettuate);
                        prenotazioniEffettutate = (ListView) findViewById(R.id.listView);
                        prenotazioniEffettutate.setOnItemClickListener(MainActivity.this);
                        new GetPrenotaAll().execute();

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        int tmp = prefs.getInt("Numero_Prenot", 0);
                        if (tmp > 0){
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                            for (int i=0; i<tmp; i++)
                                alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
                        }
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putInt("Numero_Prenot", 0).apply();

                        Intent i =  new Intent(getApplicationContext(), NotificationService.class);
                        startService(i);
                    }
                });////////////

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

    public class Login extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            String usr = user.getText().toString();
            String psw = pass.getText().toString();

            Gson gson = new Gson();
            ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/login/"+ usr+ "&"+ psw);
            Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
            if (responseHeaders == null) {
                responseHeaders = new Series<Header>(Header.class);
                clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
            }
            responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
            String json= null;
            try {
                json = clientResource.get().getText();
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
                        case Costanti.ECCEZIONE_UTENTE_INESISTENTE:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_utente_inesistente, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                        case Costanti.ECCEZIONE_PASSWORD_ERRATA:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_password, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                        default:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                } else {

                    String[] adminAndToken = gson.fromJson(json, String[].class);
                    if (adminAndToken[0].equalsIgnoreCase(getString(R.string.no_admin))) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.login_successo, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("Token", adminAndToken[1]).apply();//
                        editor.putString("UserOnLine", usr).apply();//
                        editor.putString("Authentication_Status", "online").apply();//

                    }
                    else if(adminAndToken[0].equalsIgnoreCase(getString(R.string.admin))){
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), R.string.accesso_come_admin, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });

                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                        SharedPreferences.Editor editor = prefs.edit();

                        editor.putString("Token", adminAndToken[1]).apply();//
                        editor.putString("UserOnLine", usr);//
                        editor.putString("Authentication_Status", "online").apply();//

                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setContentView(R.layout.logout);
                            TextView usr = (TextView) findViewById(R.id.textView22);
                            usr.setText(user.getText());
                            Intent i =  new Intent(getApplicationContext(), NotificationService.class);
                            startService(i);
                        }
                    });

                }
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

    public class LogoutAndDeleteToken extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            token=prefs.getString("Token", null);
            ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/delete/token/"+token);
            Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
            if (responseHeaders == null) {
                responseHeaders = new Series<Header>(Header.class);
                clientResource.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
            }
            responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
            try {
                String json = clientResource.delete().getText();
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

                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_generico, Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                            break;
                    }
                }
                else{
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("Authentication_Status","offline").apply();
                    editor.putString("Token", null).apply();
                    editor.putString("UserOnLine", null).apply();
                }
            }
            catch (ResourceException | IOException e) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(),R.string.errore_server, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
            int tmp = prefs.getInt("Numero_Prenot", 0);
            if (tmp > 0){
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(getApplicationContext(), AlertReceiver.class);
                for (int i=0; i<tmp; i++)
                    alarmManager.cancel(PendingIntent.getBroadcast(getApplicationContext(), i, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("Numero_Prenot", 0).apply();

        }

    }


    private ListView filmInprogrammazione,prenotazioniEffettutate;
    private FilmInProgrammazioneAdapter adapter;
    private PrenotazioneAdapter adapter2;
    private String usernameOnline,codicePrenotazione,token;
    private EditText user, pass;

}
