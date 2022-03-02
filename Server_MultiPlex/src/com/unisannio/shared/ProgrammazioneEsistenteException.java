package com.unisannio.shared;

@SuppressWarnings("serial")
public class ProgrammazioneEsistenteException extends Exception {

	public ProgrammazioneEsistenteException(String codice){
		super("La programmazione " + codice + " è gia esistente");
	}
	
    public ProgrammazioneEsistenteException(String sala,String data, String ora ){
		super("La programmazione in  " + sala  + "il " + data + " alle " + ora + " è gia esistente");
	}
}
