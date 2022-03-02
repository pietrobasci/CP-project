package com.unisannio.shared;

@SuppressWarnings("serial")
public class ProgrammazioneNonEsistenteException extends Exception {

	public ProgrammazioneNonEsistenteException(String codice){
		super("La programmazione "+codice+" non è presente nel database");
	}
	
	public ProgrammazioneNonEsistenteException(String nomeSala, String data, String ora){
		super("Nella sala " + nomeSala + " in data " + data + "-" + ora + " non è presente nessuna proiezione");
	}
	
	public ProgrammazioneNonEsistenteException(){
		super();
	}
}
