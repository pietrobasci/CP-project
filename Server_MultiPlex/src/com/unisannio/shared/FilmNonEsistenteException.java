package com.unisannio.shared;

@SuppressWarnings("serial")
public class FilmNonEsistenteException extends Exception{
	
	public FilmNonEsistenteException(String codice){
		super("Il film " + codice + " non è presente nel database");
	}
}
