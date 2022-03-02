package com.unisannio.multisala;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.model.Prenotazione;
import com.model.Programmazione;

import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by pietrobasci on 21/01/17.
 */
public class NotificationService extends IntentService {

    public NotificationService() {
        super("NotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        if(prefs.getString("Authentication_Status", null).equalsIgnoreCase("online")) {

            Gson gson = new Gson();
            final ArrayList<Prenotazione> prenotazion = new ArrayList<Prenotazione>();
            final ArrayList<Programmazione> progPre = new ArrayList<Programmazione>();

            try {
                SharedPreferences tokenEuser = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                String usernameOnline = tokenEuser.getString("UserOnLine", null);
                String token = tokenEuser.getString("Token", null);
                ClientResource clientResource = new ClientResource("http://10.0.2.2:8182/prenotazioniAll/username/" + usernameOnline + "&" + token);
                Series<Header> responseHeaders = (Series<Header>) clientResource.getRequestAttributes().get("org.restlet.http.headers");
                if (responseHeaders == null) {
                    responseHeaders = new Series<Header>(Header.class);
                    clientResource.getRequestAttributes().put("org.restlet.http.headers", responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent), getString(R.string.userAgentAndroid));
                Prenotazione[] prenotazioi = new Prenotazione[0];
                String json = clientResource.get().getText();
                org.restlet.data.Status stato = clientResource.getStatus();
                if (stato.getCode() == 200)
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
                    clientResource.getRequestAttributes().put("org.restlet.http.headers", responseHeaders);
                }
                responseHeaders.set(getString(R.string.userAgent), getString(R.string.userAgentAndroid));
                Programmazione[] programmaz = new Programmazione[0];
                json = clientResource.get().getText();
                stato = clientResource.getStatus();
                if (stato.getCode() == 200)
                    programmaz = gson.fromJson(json, Programmazione[].class);

                i = 0;
                while (i < programmaz.length) {
                    progPre.add(programmaz[i]);
                    i++;
                }

            } catch (ResourceException | IOException e) {

            }


            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            Intent intent1 = new Intent(this, AlertReceiver.class);
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy-hh:mm");
            Date now = new Date();
            int tmp = 0;

            for (Prenotazione prenot : prenotazion) {
                for (Programmazione prog : progPre) {
                    if (prenot.getProgrammazione().equalsIgnoreCase(prog.getCodice())) {

                        intent1.putExtra("titolo_notifica", getString(R.string.appuntamento));
                        intent1.putExtra("text_notifica", prog.getNomeFilm() + " " + getString(R.string.ora) + prog.getOra());

                        try {

                            Date evento = dateFormat.parse(prog.getData() + "-" + prog.getOra());
                            Calendar c = Calendar.getInstance();
                            c.setTime(evento);
                            c.add(Calendar.HOUR, -8);
                            evento = c.getTime();

                            long alertTime = evento.getTime();

                            if (alertTime - now.getTime() < 0) {
                                if (alertTime - now.getTime() < -7*60*60*1000)
                                    continue;
                                alertTime = now.getTime() + 3*1000;
                            }

                            alarmManager.set(AlarmManager.RTC_WAKEUP, alertTime, PendingIntent.getBroadcast(this, tmp, intent1, PendingIntent.FLAG_UPDATE_CURRENT));
                            tmp++;

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("Numero_Prenot", tmp).apply();

        }

        stopSelf();

    }


}
