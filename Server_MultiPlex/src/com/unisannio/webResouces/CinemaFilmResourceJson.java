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
import com.unisannio.shared.FilmEsistenteException;
import com.unisannio.shared.FilmNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Film;


public class CinemaFilmResourceJson extends ServerResource {
	
	@Post
	public String addFilm(String body){
		
		Gson gson = new Gson();
		Film film = gson.fromJson(body, Film.class);
		DatabaseSystem db;
		Status status;
		   
		try{
			   db = DatabaseSystem.getInstance();
				db.getUsernameByToken(this.getAttribute("token"));
			   return gson.toJson(db.inserisciFilm(film),Film.class);
			}
			catch (FilmEsistenteException e1)
		    {
			    status = new Status(Costanti.ECCEZIONE_FILM_DUPLICATO,"Film Esistente","il film è già presente nel database",null);
			    setStatus(status);
			    return gson.toJson(status, Status.class);
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
		    catch (AccessoNonAutorizzato e) {
				status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			}
	}
	
	@Delete
	public String deleteFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.removeFilm(this.getAttribute("codice")),Film.class);
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
		catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}


	@Put
	public String aggiornaFilm(String body){
		
		Gson gson = new Gson();
		Film film = gson.fromJson(body, Film.class);
		DatabaseSystem db;
		Status status;
		   
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.updateFilm(film, this.getAttribute("codice")),Film.class);
		}  
		    catch (FilmEsistenteException e) {
		    	status = new Status(Costanti.ECCEZIONE_FILM_DUPLICATO,"Film esistente","il film è già presente nel database",null);
			    setStatus(status);
			    return gson.toJson(status, Status.class); 
			} 
			catch (FilmNonEsistenteException e) {
				status = new Status(Costanti.ECCEZIONE_FILM_INESISTENTE,"Film non trovato"," film non trovato",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			} 
			catch (SQLException e) {
				status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
				setStatus(status);
				return gson.toJson(status, Status.class);		
			} 
		    catch (AccessoNonAutorizzato e) {
				status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			}
		
		   
	}
	
}
