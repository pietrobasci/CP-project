package com.unisannio.webResouces;
import java.sql.SQLException;


import org.restlet.data.Status;
import org.restlet.resource.Get;
import org.restlet.resource.ServerResource;

import com.google.gson.Gson;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Sala;


public class CinemaSaleAllResourceJson extends ServerResource {
	
	@Get
	public String getAllFilm(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
		Sala[] sala = null;
		
		try{
			db = DatabaseSystem.getInstance();
			sala=db.getAllSale();
			return gson.toJson(sala,Sala[].class); 
		}
		 
		catch (SQLException e) {
			status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
			setStatus(status);
			return gson.toJson(status, Status.class);		
		}
		
	}

}
