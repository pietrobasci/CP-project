package com.unisannio.webResouces;

import java.sql.SQLException;
import org.restlet.data.Status;
import org.restlet.resource.Delete;
import org.restlet.resource.ServerResource;
import com.google.gson.Gson;
import com.unisannio.theSystem.DatabaseSystem;

public class Logout extends ServerResource{
	                
	@Delete
	public String deleteToken(){
		
		Gson gson = new Gson();
		DatabaseSystem db = null;
		Status status;
			
		try {
			db = DatabaseSystem.getInstance();
			String json=db.removeToken((this.getAttribute("token")));
			return gson.toJson(json,String.class);
			}
				
			catch (SQLException e) {
				status = new Status(Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE,"Errore Database","Errore accesso al DataBase",null);
				setStatus(status);
				return gson.toJson(status, Status.class);
			} 
	
    }
	
}	
