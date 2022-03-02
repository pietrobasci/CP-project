package com.unisannio.shared;

@SuppressWarnings("serial")
public class UtenteNonEsistenteException extends Exception 
{
	public UtenteNonEsistenteException(String username){
		super("L'utente " + username + " non è presente nel database");
	}

}
