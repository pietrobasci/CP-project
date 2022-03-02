package com.unisannio.webResouces;



import java.sql.SQLException;

import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Programmazione;


public class CinemaProgAllResourceJson extends ServerResource {
	
	@Get
	public String getAllFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		Programmazione[] progr = null;
		
		try{
			db = DatabaseSystem.getInstance();
			progr=db.getAllProgrammazioni();
		}
		catch (SQLException e) {
			 status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore database","errore accessso al database",null);
			 setStatus(status);
			 return gson.toJson(status, Status.class);
		}
		
		return gson.toJson(progr,Programmazione[].class); 

	}

}
