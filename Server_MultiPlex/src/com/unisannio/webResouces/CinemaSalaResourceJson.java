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
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Sala;


public class CinemaSalaResourceJson extends ServerResource {
	
	@Get
	public String getSale(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.getSala(this.getAttribute("nome")),Sala.class);
		} 
		catch (SalaNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata"," La sala non trovata",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		} 
		catch (SQLException e) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);		
		} catch (AccessoNonAutorizzato e) {
			status = new Status(Costanti.ECCEZIONE_Accesso_NON_AUTORIZZATO,"Accesso non autorizzato","Errore accesso non autorizzato",null);
			setStatus(status);
			return gson.toJson(status, Status.class);		
		}	
	}
	
	@Post
	public String addSala(String body){
		
		Gson gson = new Gson();
		Sala sal = gson.fromJson(body, Sala.class);
		DatabaseSystem db;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.addSala(sal),Sala.class);
		}
		
		catch (SalaEsistenteException e1){
			    status = new Status(Costanti.ECCEZIONE_SALA_DUPLICATA,"Sals Duplicato","la sala è già presente nel database",null);
			    setStatus(status);
			    return gson.toJson(status, Status.class);
		    }
		catch (SalaNonEsistenteException e) {
				status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata"," La sala non trovata",null);
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
	public String deleteSAla(){
		
		Gson gson = new Gson();
		DatabaseSystem db;
		Status status;
		
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.removeSala(this.getAttribute("nome")),Sala.class);
		}
		
		catch (SalaNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata"," La sala non trovata",null);
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
	public String aggiornaSala(String body){
		
		Gson gson = new Gson();
		Sala sala = gson.fromJson(body, Sala.class);
		DatabaseSystem db;
		Status status;
		   
		try{
			db = DatabaseSystem.getInstance();
			db.getUsernameByToken(this.getAttribute("token"));
			return gson.toJson(db.updateSala(sala, this.getAttribute("nome")),Sala.class);
		}
		
		catch (SalaEsistenteException e1){
			    status = new Status(Costanti.ECCEZIONE_SALA_DUPLICATA,"Sals esistente","la sala è già presente nel database",null);
			    setStatus(status);
			    return gson.toJson(status, Status.class);
			}
		catch (SalaNonEsistenteException e) {
				status = new Status(Costanti.ECCEZIONE_SALA_INESISTENTE,"Sala non trovata"," La sala non trovata",null);
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
