package com.unisannio.shared;

@SuppressWarnings("serial")
public class SalaNonEsistenteException extends Exception 
{
	public SalaNonEsistenteException(String nome){
		super("La sala " + nome + " non è presente nel database");
	}
}
