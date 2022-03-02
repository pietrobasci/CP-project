package com.unisannio.webResouces;


import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.PrenotazioniInesistentiException;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.shared.SalaNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Prenotazione;



public class CinemaDeletePrenotResourceJson extends ServerResource {
	
	@Delete
	public String deletePrenotazione(){
		Gson gson = new Gson();
		DatabaseSystem db;
		String json;
		Status status;
		try {
			db = DatabaseSystem.getInstance();
			//gerenra un eccezione se il token non è presente nel DB (non autenticato)
			db.getUsernameByToken(this.getAttribute("token"));
			//gerenra un eccezione se la prenotazione non è presente nel DB
			db.getPrenotazione(this.getAttribute("codicePrenotazione"));
			json=gson.toJson(db.removePrenotazione(this.getAttribute("codicePrenotazione")),Prenotazione.class);			
		    return json;
		         
		} catch (PrenotazioniInesistentiException e) {
			status = new Status(Costanti.ECCEZIONE_PRENOTAZIONI_INESISTENTI,"Prenotazione non trovata","Non è stata effettuata nessuna prenotazione",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (ProgrammazioneNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE,"Programmazione non trovata","La programmazione non è stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (SalaNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata","La sala non è stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (SQLException e) {
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
