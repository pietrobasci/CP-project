package com.unisannio.theSystem;


public class Prenotazione{
	
	private String codice, username, programmazione;
	private int numeroPosti;
	
	public Prenotazione(String codice, String username, String programmazione, int numeroPosti){
		this.codice = codice;
		this.username = username;
		this.programmazione = programmazione;
		this.numeroPosti=numeroPosti;
	}

	public String getCodice(){
		return codice;
	}

	public String getUsername(){
		return username;
	}

	public String getProgrammazione(){
		return programmazione;
	}
	

	public int getNumeroPosti(){
		return numeroPosti;
	}

	@Override
	public String toString(){
		return "Prenotazione [idPrenotazione=" + codice + ", username=" + username + ", spettacolo=" + programmazione + "]";
	}

	@Override
	public boolean equals(Object obj){
		Prenotazione tmp = (Prenotazione) obj;
		return tmp.getCodice().equals(this.getCodice());
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public void setProgrammazione(String programmazione){
		this.programmazione = programmazione;
	}

	public void setNumeroPosti(int numeroPosti){
		this.numeroPosti = numeroPosti;
	}
	
	
	
}
