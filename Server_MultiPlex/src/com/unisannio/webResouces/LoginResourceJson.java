package com.unisannio.webResouces;

import java.sql.SQLException;
import java.util.UUID;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.shared.UtenteAmministratoreGiaRegistratoException;
import com.unisannio.shared.UtenteEsistenteException;
import com.unisannio.shared.UtenteNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Utente;

public class LoginResourceJson extends ServerResource{
	                
	@Get
	public String getUtente(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Utente utent = null;
		String adminAndToken[]=new String[2];
		Status status;
			
		try {
			db = DatabaseSystem.getInstance();
			utent=db.getUtenteByUsername(this.getAttribute("username"));
			if(utent.getPassword().equals(this.getAttribute("password"))){
				if(utent.isAdmin()){
					String token=UUID.randomUUID().toString();
					db.associaToken(token,utent.getUsername());
					adminAndToken[0]="admin";
					adminAndToken[1]=token;
					return gson.toJson(adminAndToken,String[].class); 
					}
					else{
						String token=UUID.randomUUID().toString();
						db.associaToken(token, this.getAttribute("username"));
						adminAndToken[0]="noadmin";
						adminAndToken[1]=token;
						return gson.toJson(adminAndToken,String[].class);
					}
					
				}
				else{
					status = new Status(Costanti.ECCEZIONE_PASSWORD_ERRATA,"Password errata","La password inserita non è corretta",null);
					setStatus(status);
					return gson.toJson(status, Status.class);
				}
			
		    } 
		
			catch (SQLException e) {
				status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			} 
			catch (UtenteNonEsistenteException e) {
				status = new Status(Costanti.ECCEZIONE_UTENTE_INESISTENTE,"Utente non trovato"," utente non trovato",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			}
			
	}


	
	@Post
	public String addUtente(String utente) {
		
		Gson gson=new Gson();
		Utente utent=gson.fromJson(utente, Utente.class);
        DatabaseSystem database =null;
        Status status;
        
        try {
			database=DatabaseSystem.getInstance();
			return gson.toJson(database.addUser(utent),Utente.class);
        }	
			 
        catch (SQLException e) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);
		}
        catch (UtenteEsistenteException e) {
				status = new Status(Costanti.ECCEZIONE_UTENTE_DUPLICATO,"Utente esistente","l utente è già presente nel database",null);
			    setStatus(status);
			    return gson.toJson(status, Status.class);
		} 
        catch (UtenteNonEsistenteException e) {
			status = new Status(Costanti.ECCEZIONE_UTENTE_INESISTENTE,"Utente inesistente","l utente  non è  presente nel database",null);
		    setStatus(status);
		    return gson.toJson(status, Status.class);		} 
        catch (UtenteAmministratoreGiaRegistratoException e) {
        	status = new Status(Costanti.ECCEZIONE_AmministratoreEsistente,"Amministratore esistente","l amministratore è stato già registrato nel database",null);
		    setStatus(status);
		    return gson.toJson(status, Status.class);
		}
        
        
	}
	
}
