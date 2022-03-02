package com.unisannio.webResouces;
import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Programmazione;

public class CinemaProgAllFilterCodiceFilmResourceJson extends ServerResource {

	
	@Get
	public String getAllFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		Programmazione[] progr = null;
		
		try{
			db = DatabaseSystem.getInstance();
			progr=db.filterProgrammazioniByFilm(this.getAttribute("codiceFilm"));
			return gson.toJson(progr,Programmazione[].class); 
		}
		catch (SQLException e) {
			 status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","the database file was not found",null);
			 setStatus(status);
			 return gson.toJson(status, Status.class);
		} catch (ProgrammazioneNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE,"Programmazione non trovata","Programmazione non esistente",null);
			 setStatus(status);
			 return gson.toJson(status, Status.class);
		}
		
	}
	
}
