package com.unisannio.webResouces;
import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.UtenteNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Film;
import com.unisannio.theSystem.Utente;

public class CinemaUserAllResourceJson extends ServerResource {
	
	@Get
	public String getAllFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		Utente[] utenti = null;
		
		try{
			db = DatabaseSystem.getInstance();
			String username = db.getUsernameByToken(this.getAttribute("token"));
			if(!db.getUtenteByUsername(username).isAdmin())
				throw new AccessoNonAutorizzato();
			utenti=db.getAllUtenti();
		}
		catch (SQLException e) {
			 status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore database","Errore accesso al database",null);
			 setStatus(status);
			 return gson.toJson(status, Status.class);
		} catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (UtenteNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_UTENTE_INESISTENTE,"Utente non trovato","utente non trovato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
		return gson.toJson(utenti,Utente[].class); 

	}

}
