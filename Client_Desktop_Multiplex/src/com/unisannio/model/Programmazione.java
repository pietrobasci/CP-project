package com.unisannio.model;



public class Programmazione {
	
	private String codice, sala, codiceFilm, nomeFilm, data, ora;
	private int postiPrenotati;
	
	public Programmazione(String codice, String sala, String codiceFilm, String nomeFilm, String data, String ora, int postiPrenotati) {
		this.codice = codice;
		this.sala = sala;
		this.codiceFilm = codiceFilm;
		this.nomeFilm=nomeFilm;
		this.data = data;
		this.ora = ora;
		this.postiPrenotati = postiPrenotati;
	}

	
	public String getCodice(){
		return codice;
	}

	public String getNomeFilm(){
		return nomeFilm;
	}

	public String getSala(){
		return sala;
	}

	public String getCodiceFilm(){
		return codiceFilm;
	}

	public String getData(){
		return data;
	}

	public String getOra(){
		return ora;
	}

	public int getPostiPrenotati(){
		return postiPrenotati;
	}

	@Override
	public String toString(){
		return "Codice Programmazione: '" + this.codice + "' -Film: '"+this.codiceFilm + "' -Titolo film: '"+this.codiceFilm +"' -Sala: '" + this.sala + "' -Data: '"+ this.data + "' -Ora: '" + this.ora + "' -Prenotati: '" + this.postiPrenotati + "'";
	}

	@Override
	public boolean equals(Object arg0){
		Programmazione tmp = (Programmazione)arg0;
		return this.getCodice().equalsIgnoreCase(tmp.getCodice());
	}

	public void setCodice(String codice){
		this.codice = codice;
	}

	public void setSala(String sala){
		this.sala = sala;
	}

	public void setCodiceFilm(String codiceFilm){
		this.codiceFilm = codiceFilm;
	}

	public void setNomeFilm(String nomeFilm){
		this.nomeFilm = nomeFilm;
	}

	public void setData(String data){
		this.data = data;
	}

	public void setOra(String ora){
		this.ora = ora;
	}

	public void setPostiPrenotati(int postiPrenotati){
		this.postiPrenotati = postiPrenotati;
	}
}
