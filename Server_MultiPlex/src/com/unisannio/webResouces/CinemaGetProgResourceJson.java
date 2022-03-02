package com.unisannio.webResouces;

import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.ProgrammazioneEsistenteException;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Programmazione;

public class CinemaGetProgResourceJson extends ServerResource {
	
	@Get
	public String getProgrammazione(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			return gson.toJson(db.getProgrammazione(this.getAttribute("codice")),Programmazione.class);
		} 
		
		catch (ProgrammazioneNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE,"Programmazione non trovata","La programmazione non è stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} 
		catch (SQLException e) {
			 status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore database","errore accessso al database",null);
			 setStatus(status);
			 return gson.toJson(status, Status.class);
		}
	}
	

}

