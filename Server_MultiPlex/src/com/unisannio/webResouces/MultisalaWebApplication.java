package com.unisannio.webResouces;



import org.restlet.Application;
import org.restlet.Component;
import org.restlet.Restlet;
import org.restlet.data.Protocol;
import org.restlet.routing.Filter;
import org.restlet.routing.Router;



public class MultisalaWebApplication extends Application {

	@Override
	public Restlet createInboundRoot(){
		Router router = new Router(getContext());
		Filter filter = new RequestFilter(getContext());
		
		router.attach("/sale/all/", CinemaSaleAllResourceJson.class);
		router.attach("/sale/{nome}&{token}", CinemaSalaResourceJson.class);
		
		router.attach("/prenotazioni/all/{token}", CinemaPrenotazioniAllResourceJson.class);//
		router.attach("/prenotazioni/{codiceProg}&{token}", CinemaPostPrenotazioneResourceJson.class);
		router.attach("/prenotazioniAll/username/{username}&{token}", CinemaGetPrenotazioneUserResourceJson.class);
		router.attach("/prenotazioni/username/deletePrenot/{codicePrenotazione}&{token}",CinemaDeletePrenotResourceJson.class);
		
		router.attach("/film/all/",CinemaFilmAllResourceJson.class );
		router.attach("/film/getFilm/{codice}", CinemaGetFilmResourceJson.class);
		router.attach("/film/{codice}&{token}", CinemaFilmResourceJson.class);
		
		router.attach("/prog/all/", CinemaProgAllResourceJson.class);
		router.attach("/prog/getProg/{codice}", CinemaGetProgResourceJson.class);
		router.attach("/progAll/{codiceFilm}", CinemaProgAllFilterCodiceFilmResourceJson.class);
		router.attach("/prog/{codice}&{token}", CinemaProgResourceJson.class);
		
		router.attach("/user/{username}&{token}", CinemaUserResourceJson.class);//
		router.attach("/user/all/{token}", CinemaUserAllResourceJson.class);//
		
		router.attach("/login/{username}&{password}", LoginResourceJson.class);
		router.attach("/delete/token/{token}",Logout.class);
		
		filter.setNext(router);
		return filter;
	}
	
	public static void main(String[] args){
		Component component = new Component();
		component.getServers().add(Protocol.HTTP, 8182);
		component.getDefaultHost().attach(new MultisalaWebApplication());
		try {
			component.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
	}
	
	
}
