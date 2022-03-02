package com.unisannio.shared;

@SuppressWarnings("serial")
public class AccessoNonAutorizzato extends Exception {

	public AccessoNonAutorizzato(){
		super("Accesso non autorizzato");
	}
}
