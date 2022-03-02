package com.unisannio.theSystem;


public class Film 
{
	private String codice, titolo, descrizione, regista, genere;
	private int durata;
	private byte[] copertina;

	public Film(String codice, String titolo,int durata, String genere, String regista, String descrizione, byte[] copertina ){
		this.codice = codice;
		this.titolo = titolo;
		this.descrizione = descrizione;
		this.regista = regista;
		this.genere = genere;
		this.durata = durata;
		this.copertina = copertina;
	}

	public String getCodice() {
		return codice;
	}

	public String getTitolo() {
		return titolo;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public String getRegista(){
		return regista;
	}

	public String getGenere() {
		return genere;
	}

	public int getDurata() {
		return durata;
	}

	
	public String toString() {
		return "Codice Film: "+this.codice +"-Titolo: "+this.titolo + " -Regista: " + this.regista;
	}

	@Override
	public boolean equals(Object obj) 
	{
		Film tmp = (Film)obj;
		return this.getCodice().equalsIgnoreCase(tmp.getCodice());
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public void setTitolo(String titolo) {
		this.titolo = titolo;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public void setRegista(String regista) {
		this.regista = regista;
	}

	public void setGenere(String genere) {
		this.genere = genere;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public byte[] getCopertina() {
		return copertina;
	}

	public void setCopertina(byte[] copertina) {
		this.copertina = copertina;
	}
	
	
}
