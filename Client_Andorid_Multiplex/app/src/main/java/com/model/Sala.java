package com.model;



public class Sala 
{
	private String nome;
	private int posti;
	
	public Sala(String nome, int posti){
		super();
		this.nome = nome;
		this.posti = posti;
	}
	

	public String getNome(){
		return nome;
	}

	public int getPosti(){
		return posti;
	}

	@Override
	public String toString(){
		return "Sala: '" +this.nome+"' -Posti: '"+this.posti + "'";
	}
	
	@Override
	public boolean equals(Object obj){
		Sala s = (Sala)obj;
		return this.getNome().equalsIgnoreCase(s.getNome());
	}


	public void setNome(String nome){
		this.nome = nome;
	}


	public void setPosti(int posti){
		this.posti = posti;
	}

}
