package com.unisannio.shared;

@SuppressWarnings("serial")
public class UtenteEsistenteException extends Exception 
{
	public UtenteEsistenteException(String utente){
		super("L'utente " + utente + " è già presente nel database.");
	}

}
