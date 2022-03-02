package com.unisannio.webResouces;




import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.PrenotazioniInesistentiException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Prenotazione;


public class CinemaGetPrenotazioneUserResourceJson extends ServerResource {

	@Get
	public String getPrenotazioni(){
		
		Gson gson = new Gson();
		DatabaseSystem database = null;
		Status status;
		Prenotazione[] prenotazioni=null;
		
		try{
			database = DatabaseSystem.getInstance();
			//gerenra un eccezione se il token non è presente nel DB (non autenticato)
			database.getUsernameByToken(this.getAttribute("token"));
			prenotazioni=database.filterPrenotazioniByUsername(this.getAttribute("username"));
			return gson.toJson(prenotazioni, Prenotazione[].class);
		}
		
		catch( SQLException e){
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);	
		} catch (PrenotazioniInesistentiException e) {
			status = new Status(Costanti.ECCEZIONE_PRENOTAZIONI_INESISTENTI,"Prenotazione non trovata","Non è stata effettuata nessuna prenotazione",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} 
		
	}
	
}