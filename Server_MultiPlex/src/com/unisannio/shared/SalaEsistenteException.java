package com.unisannio.shared;

@SuppressWarnings("serial")
public class SalaEsistenteException extends Exception 
{
	public SalaEsistenteException(String nome){
		super("La sala " + nome + " è già presente nel database");
	}
}
