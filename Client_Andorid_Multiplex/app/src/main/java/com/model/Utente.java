package com.model;



public class Utente
{
	private boolean admin;
	private String username, password, nome, cognome, email;
	
	public Utente(boolean admin, String username, String password, String nome, String cognome, String email){
		this.admin = admin;
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.cognome = cognome;
		this.email = email;
	}
	

	public boolean isAdmin(){
		return admin;
	}

	public String getUsername(){
		return username;
	}

	public String getPassword(){
		return password;
	}

	public String getNome(){
		return nome;
	}

	public String getCognome(){
		return cognome;
	}

	public String getEmail(){
		return email;
	}

	@Override
	public String toString(){
		return "Utente [admin: '" + admin + "' -username: '" + username
				+ "' -nome=" + nome + "' -cognome=" + cognome + "' -email="
				+ email + "' ]";
	}
	
	@Override
	public boolean equals(Object obj){
		Utente u = (Utente)obj;
		return this.getUsername().equals(u.getUsername());
	}	
	
	
}
