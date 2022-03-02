package com.unisannio.shared;

@SuppressWarnings("serial")
public class FilmEsistenteException extends Exception {
	
	public FilmEsistenteException(String codice){
		super("Il film " + codice + "  è gia  presente nel database");
	}
}
