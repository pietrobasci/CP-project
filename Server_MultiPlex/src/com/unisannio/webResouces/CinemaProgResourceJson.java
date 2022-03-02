package com.unisannio.webResouces;

import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.Post;
import org.restlet.resource.Put;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.ProgrammazioneEsistenteException;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Programmazione;

public class CinemaProgResourceJson extends ServerResource {
	
	
	@Post
	public String addProgrammazione(String body){
		
		Gson gson = new Gson();
		Programmazione prog = gson.fromJson(body, Programmazione.class);
		DatabaseSystem db;
		Status status;
		
		try {
		   db = DatabaseSystem.getInstance();
		   db.getUsernameByToken(this.getAttribute("token"));
		   return gson.toJson(db.addProgrammazione(prog),Programmazione.class);
		} 
		
		catch (ProgrammazioneEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_DUPLICATA,"Programmazione esistente","proiezione gia esistente",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
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
		catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}
	
	@Delete
	public String deleteProgrammazione(){
		
		Gson gson = new Gson();
		DatabaseSystem db;
		Status status;
		
		try{
		   db = DatabaseSystem.getInstance();
		   db.getUsernameByToken(this.getAttribute("token"));
		   return gson.toJson(db.removeProgrammazione(this.getAttribute("codice")),Programmazione.class);
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
		catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}
	
	@Put
	public String aggiornaProgrammazione(String body){
		
		Gson gson = new Gson();
		Programmazione prog = gson.fromJson(body, Programmazione.class);
		DatabaseSystem db;
		Status status;
		
		try {
		   db = DatabaseSystem.getInstance();
	       db.getUsernameByToken(this.getAttribute("token"));
		   return gson.toJson(db.updateProgra(prog, this.getAttribute("codice")),Programmazione.class);
		} 
		
		catch (ProgrammazioneEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_DUPLICATA,"ProgrammazioneStillExistent","proiezione gia esistente",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
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
		catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}

}

