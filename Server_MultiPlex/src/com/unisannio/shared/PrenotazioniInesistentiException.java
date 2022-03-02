package com.unisannio.shared;

@SuppressWarnings("serial")
public class PrenotazioniInesistentiException extends Exception {

	public PrenotazioniInesistentiException(){
		super("Non è stata effettuata nessuna prenotazione");
	}
}
