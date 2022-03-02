package com.unisannio.shared;

@SuppressWarnings("serial")
public class UtenteAmministratoreGiaRegistratoException extends Exception {
	
	public UtenteAmministratoreGiaRegistratoException(){
		super("Già è stato registrato un amministratore ");
	}

}
