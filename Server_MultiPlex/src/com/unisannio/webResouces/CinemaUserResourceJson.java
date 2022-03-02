package com.unisannio.webResouces;

import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.SalaEsistenteException;
import com.unisannio.shared.SalaNonEsistenteException;
import com.unisannio.shared.UtenteNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Sala;
import com.unisannio.theSystem.Utente;


public class CinemaUserResourceJson extends ServerResource {
	
	@Get
	public String getUser(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.getUtenteByUsername(this.getAttribute("username")),Utente.class);
		} 
		catch (SQLException e) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);		
		} catch (UtenteNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_UTENTE_INESISTENTE,"Utente non trovato","utente non trovato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}
}