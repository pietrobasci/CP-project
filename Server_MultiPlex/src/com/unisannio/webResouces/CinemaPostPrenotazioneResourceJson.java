package com.unisannio.webResouces;

import java.sql.SQLException;



import org.restlet.data.Status;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.AccessoNonAutorizzato;
import com.unisannio.shared.PostiNonSufficientiException;
import com.unisannio.shared.PrenotazioniInesistentiException;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.shared.SalaNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Prenotazione;

public class CinemaPostPrenotazioneResourceJson extends ServerResource {
	
	@Post
	public String addPrenotazione(String body){
		Gson gson = new Gson();
		Prenotazione pr = gson.fromJson(body, Prenotazione.class);
		DatabaseSystem db;
		
		Status status;
		try {
			db = DatabaseSystem.getInstance();
			//gerenra un eccezione se il token non è presente nel DB (non autenticato)
			db.getUsernameByToken(this.getAttribute("token"));
			//gerenra un eccezione se la programmazione non è presente nel DB
			db.getProgrammazione(this.getAttribute("codiceProg"));
			String t=gson.toJson(db.addPrenotazione(pr),Prenotazione.class);			
			return t;
		
		} catch (SalaNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata","La sala non è stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		catch (PostiNonSufficientiException e) {
			status = new Status(Costanti.ECCEZIONE_POSTI_INSUFFICIENTI,"Posti Insufficienti","Non ci sono posti in aula per lo spettacolo",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}catch (ProgrammazioneNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE,"Programmazione non trovata","La programmazione non è stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}catch (SQLException ex) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} catch (PrenotazioniInesistentiException e) {
			status = new Status(Costanti.ECCEZIONE_PRENOTAZIONI_INESISTENTI,"Prenotazione Non Trovata","La prenotazione non � stata trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
			
		} catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
		
	}

}
