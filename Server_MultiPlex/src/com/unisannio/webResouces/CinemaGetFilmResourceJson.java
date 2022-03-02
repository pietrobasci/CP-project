package com.unisannio.webResouces;

import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.FilmEsistenteException;
import com.unisannio.shared.FilmNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Film;


public class CinemaGetFilmResourceJson extends ServerResource {
	
	@Get
	public String getFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			return gson.toJson(db.getFilm(this.getAttribute("codice")),Film.class);
		} 
		catch (FilmNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_FILM_INESISTENTE,"Film Inesistente"," film non trovato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} 
		catch (SQLException e) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);		
		}
	}
	

	
}
