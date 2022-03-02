package com.unisannio.multisala;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import com.model.*;
import java.io.IOException;

public class RegistrazioneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrazione);
        nome = (EditText) findViewById(R.id.editText3);
        cognome = (EditText) findViewById(R.id.editText4);
        username = (EditText) findViewById(R.id.editText5);
        password = (EditText) findViewById(R.id.editText6);
        email = (EditText) findViewById(R.id.editText8);
        conferma_password = (EditText) findViewById(R.id.editText7);
    }

    public void registra(View v){
        if (!nome.getText().toString().isEmpty() && !cognome.getText().toString().isEmpty() && !username.getText().toString().isEmpty() && !password.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !conferma_password.getText().toString().isEmpty()) {
            if (password.getText().toString().equals(conferma_password.getText().toString())) {
                boolean emaailvalida = android.util.Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches();
                if (emaailvalida) {
                    new Registrazione().execute();
                }
                else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_email_non_valida, Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            }
            else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast toast = Toast.makeText(getApplicationContext(), R.string.password_differenti, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }
        else{
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_campi_incompleti, Toast.LENGTH_SHORT);
                    toast.show();
                }
            });

        }
    }

    public class Registrazione extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            ClientResource cr;
            Gson gson = new Gson();
            org.restlet.data.Status status;
            String json = null;
            Utente p = new Utente(false, username.getText().toString(), password.getText().toString(), nome.getText().toString(), cognome.getText().toString(), email.getText().toString());
            String uri = "http://10.0.2.2:8182/login/" + p.getUsername() + "&" + p.getPassword();
            cr = new ClientResource(uri);
            Series<Header> responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers");
            if (responseHeaders == null) {
                responseHeaders = new Series<Header>(Header.class);
                cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders);
            }
            responseHeaders.set(getString(R.string.userAgent),getString(R.string.userAgentAndroid) );
            try {
                json = cr.post(gson.toJson(p, Utente.class)).getText();
                status = cr.getStatus();
                if (status.getCode() != 200) {
                    switch (status.getCode()) {
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
                        case Costanti.ECCEZIONE_UTENTE_DUPLICATO:
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), R.string.errore_utente_giaesistente, Toast.LENGTH_SHORT);
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
                    final Utente user = gson.fromJson(json, Utente.class);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            RegistrazioneActivity.this.finish();
                            Toast toast = Toast.makeText(getApplicationContext(), "L'utente " + user.getUsername() + " è stato aggiunto correttamente", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
            } catch (ResourceException | IOException e) {
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

    private EditText nome, cognome, username, password, email, conferma_password;

}
