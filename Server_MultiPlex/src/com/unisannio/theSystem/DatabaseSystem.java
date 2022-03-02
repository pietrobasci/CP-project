package com.unisannio.theSystem;

import java.sql.Connection;


import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.restlet.engine.util.Base64;

import com.unisannio.shared.*;


public class DatabaseSystem 
{
	private static DatabaseSystem instance;
	private Connection accDB;
	
DatabaseSystem() throws SQLException{
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}
		
		accDB = DriverManager.getConnection(CostantiDatabase.URL, CostantiDatabase.DB_USER, CostantiDatabase.DB_PASSWORD);
		Statement create = accDB.createStatement();
		try {
			create.execute(CostantiDatabase.CREATE_DATABASE);
		} catch (SQLException e) {
			System.err.println("Database già esistente");
		}

		accDB = DriverManager.getConnection(CostantiDatabase.URL+"/"+CostantiDatabase.DB_NAME, CostantiDatabase.DB_USER, CostantiDatabase.DB_PASSWORD);
		create = accDB.createStatement();
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_SALE);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_SALE+" già esistente");
		}
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_FILM);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_FILM+" già esistente");
		}
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_PROGRAMMAZIONE);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" già esistente");
		}
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_UTENTI);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_UTENTI+" già esistente");
		}
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_PRENOTAZIONI);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+" già esistente");
		}
		try{
			create.execute(CostantiDatabase.CREATE_TABLE_TOKEN);
		}catch (SQLException e) {
			System.err.println("Tabella "+CostantiDatabase.NAME_TABLE_TOKEN+" già esistente");
		}
		
	}
	
	public static synchronized DatabaseSystem getInstance() throws SQLException{
		if(instance==null)
			instance=new DatabaseSystem();
		return instance;
	}


	public synchronized Film[] getAllFilm() throws SQLException{
		ArrayList<Film> listaFilm = new ArrayList<Film>();
		Statement query = accDB.createStatement();
		ResultSet risultato = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_FILM);
		while(risultato.next()){
			Film nuovoFilm = new Film(risultato.getString("CODICE_FILM"), risultato.getString("NOME"),
					risultato.getInt("DURATA"), risultato.getString("GENERE"), risultato.getString("REGISTA"),risultato.getString("DESCRIZIONE"), Base64.decode(risultato.getString("COPERTINA")));	
			listaFilm.add(nuovoFilm);
		}
		
		Film[] films = new Film[listaFilm.size()];
		int i = 0;
		for(Film f:listaFilm){
			films[i++] = f;
		}
		return films;
	}


	public synchronized Film getFilm(String codice) throws SQLException, FilmNonEsistenteException{
		Statement query = accDB.createStatement();
		ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_FILM+" WHERE CODICE_FILM='"+codice+"'");
		if(!result.first())
			throw new FilmNonEsistenteException(codice);
		Film nuovoFilm = new Film(result.getString("CODICE_FILM"), result.getString("NOME"),result.getInt("DURATA"), result.getString("GENERE"), result.getString("REGISTA"),result.getString("DESCRIZIONE"), Base64.decode(result.getString("COPERTINA")));	
		return nuovoFilm;
	}
	

	public synchronized Film inserisciFilm(Film f) throws SQLException, FilmEsistenteException, FilmNonEsistenteException{
		try{
			getFilm(f.getCodice());
			throw new FilmEsistenteException(f.getCodice());
		}
		catch(FilmNonEsistenteException ex)
		{
			Statement insert = accDB.createStatement();
			insert.execute("INSERT INTO " + CostantiDatabase.NAME_TABLE_FILM +" (CODICE_FILM, NOME, DURATA, GENERE, REGISTA, DESCRIZIONE, COPERTINA) VALUES ('"+
							f.getCodice()+"','"+f.getTitolo()+"','"+f.getDurata()+"','"+f.getGenere()+"','"+f.getRegista()+"','"+f.getDescrizione()+"','"+Base64.encode(f.getCopertina(), false)+"');");
			return getFilm(f.getCodice());
		}
	}
		
		public synchronized Film updateFilm(Film filmAggiornato,String codiceFilmDaModificare ) throws  FilmEsistenteException, SQLException, FilmNonEsistenteException{ 
			try{
				if(filmAggiornato.getCodice().equalsIgnoreCase(codiceFilmDaModificare))
				throw  new 	FilmNonEsistenteException(filmAggiornato.getCodice());
				getFilm(filmAggiornato.getCodice());
				throw new FilmEsistenteException(filmAggiornato.getCodice());
			}
			catch(FilmNonEsistenteException ex)
			{
			 Statement query = accDB.createStatement();
			 query.executeUpdate("UPDATE "+CostantiDatabase.NAME_TABLE_FILM+" SET CODICE_FILM='"+filmAggiornato.getCodice()+"',"+" NOME='"+
			                      filmAggiornato.getTitolo()+"',"+" DURATA='"+filmAggiornato.getDurata()+"',"+"  GENERE='"+filmAggiornato.getGenere()+
			                      "',"+" REGISTA='"+filmAggiornato.getRegista()+"',"+" DESCRIZIONE='"+filmAggiornato.getDescrizione()+"',"+" COPERTINA='"+Base64.encode(filmAggiornato.getCopertina(), false)+"' WHERE CODICE_FILM='"+codiceFilmDaModificare+"';");
			 return getFilm(filmAggiornato.getCodice());
			}
  

		}
		
		public synchronized Film removeFilm(String codice) throws SQLException, FilmNonEsistenteException{
			Film tmp = getFilm(codice);
			Statement insert = accDB.createStatement();
			insert.execute("DELETE FROM " + CostantiDatabase.NAME_TABLE_FILM + " WHERE CODICE_FILM='"+tmp.getCodice()+"';");
			return tmp;
		}
		
		public synchronized Programmazione updateProgra(Programmazione progAggiornat,String codiceProgrDaModificare ) throws ProgrammazioneEsistenteException, SQLException, ProgrammazioneNonEsistenteException{ 
			try{
				if(progAggiornat.getCodice().equalsIgnoreCase(codiceProgrDaModificare))
				  throw new ProgrammazioneNonEsistenteException(progAggiornat.getCodice());	
				getProgrammazione(progAggiornat.getCodice());
				throw new ProgrammazioneEsistenteException(progAggiornat.getCodice());
			}
			catch(ProgrammazioneNonEsistenteException ex1){
				try{
					Programmazione p=getProgrammazione(codiceProgrDaModificare);
					if(progAggiornat.getSala().equalsIgnoreCase(p.getSala()) && progAggiornat.getData().equalsIgnoreCase(p.getData()) && progAggiornat.getOra().equalsIgnoreCase(p.getOra()))
				      throw new ProgrammazioneNonEsistenteException(progAggiornat.getCodice());	
					getProgrammazione(progAggiornat.getSala(),progAggiornat.getData(),progAggiornat.getOra());
					throw new ProgrammazioneEsistenteException(progAggiornat.getSala(),progAggiornat.getData(),progAggiornat.getOra());
				}
				
				catch(ProgrammazioneNonEsistenteException e){
					 Statement query = accDB.createStatement();
					 query.executeUpdate("UPDATE "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" SET COD_PROG='"+progAggiornat.getCodice()+"',"+" NOME_SALA='"+progAggiornat.getSala()+"',"+
					                     " CODICE_FILM='"+progAggiornat.getCodiceFilm()+"',"+" NOME_FILM='"+progAggiornat.getNomeFilm()+"',"+" DATA='"+progAggiornat.getData()+"',"+" ORA='"+
							             progAggiornat.getOra()+"',"+" POSTI_PRENOTATI='"+progAggiornat.getPostiPrenotati()+"' WHERE COD_PROG='"+codiceProgrDaModificare+"';");
					 return getProgrammazione(progAggiornat.getCodice());
				}
			}	
		}
		
		
		public synchronized Programmazione getProgrammazione(String codice) throws ProgrammazioneNonEsistenteException, SQLException{
			Statement query = accDB.createStatement();
			ResultSet risultato = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" WHERE COD_PROG='"+codice+"'");
			if(!risultato.first())
				throw new ProgrammazioneNonEsistenteException(codice);
			Programmazione nuovo = new Programmazione(risultato.getString("COD_PROG"), risultato.getString("NOME_SALA"),
						 risultato.getString("CODICE_FILM"),risultato.getString("NOME_FILM"), risultato.getString("DATA"),risultato.getString("ORA"),risultato.getInt("POSTI_PRENOTATI"));
			return nuovo;
		}
		
		public synchronized Programmazione[] getAllProgrammazioni() throws SQLException{
			ArrayList<Programmazione> listaProgrammazioni = new ArrayList<Programmazione>();
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE);
			while(result.next()){
				Programmazione nuovaProgrammazione = new Programmazione(result.getString("COD_PROG"), result.getString("NOME_SALA"),
						 result.getString("CODICE_FILM"),result.getString("NOME_FILM"),result.getString("DATA") , result.getString("ORA"),result.getInt("POSTI_PRENOTATI"));	
				listaProgrammazioni.add(nuovaProgrammazione);
			}
			
			Programmazione[] tempProgrammazione = new Programmazione[listaProgrammazioni.size()];
			int i=0;
			for(Programmazione p:listaProgrammazioni){
				tempProgrammazione[i++]=p;
			}
			return tempProgrammazione;
		}
		
		public synchronized Programmazione getProgrammazione(String nomeSala, String data, String ora) throws ProgrammazioneNonEsistenteException, SQLException{
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" WHERE NOME_SALA='"+nomeSala+"' AND DATA='"+data+"' AND ORA='"+ora+"'");
			if(!result.first())
				throw new ProgrammazioneNonEsistenteException(nomeSala,data,ora);
			Programmazione nuovo = new Programmazione(result.getString("COD_PROG"), result.getString("NOME_SALA"),result.getString("CODICE_FILM"), 
					result.getString("NOME_FILM"),result.getString("DATA") , result.getString("ORA"),result.getInt("POSTI_PRENOTATI"));
			return nuovo;
		}
		

		public synchronized Programmazione addProgrammazione(Programmazione p) throws SQLException, ProgrammazioneEsistenteException, ProgrammazioneNonEsistenteException{
			try{
				getProgrammazione(p.getCodice());
				throw new ProgrammazioneEsistenteException(p.getCodice());
			}
			catch(ProgrammazioneNonEsistenteException ex1){
				try{
					getProgrammazione(p.getSala(),p.getData(),p.getOra());
					throw new ProgrammazioneEsistenteException(p.getSala(),p.getData(),p.getOra());
				}
				catch(ProgrammazioneNonEsistenteException ex2){
					Statement insert = accDB.createStatement();
					insert.execute("INSERT INTO "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" (COD_PROG, NOME_SALA, CODICE_FILM, NOME_FILM, DATA, ORA, POSTI_PRENOTATI) VALUES ('"+
							p.getCodice()+"','"+p.getSala()+"','"+p.getCodiceFilm()+"','"+p.getNomeFilm()+"','"+p.getData()+"','"+p.getOra()+"','"+p.getPostiPrenotati()+"');");
					return getProgrammazione(p.getCodice());
				}
			}
		}
		
		public synchronized Programmazione removeProgrammazione(String codice) throws SQLException, ProgrammazioneNonEsistenteException{
			Programmazione tmp = getProgrammazione(codice);
			Statement insert = accDB.createStatement();
			insert.execute("DELETE FROM "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" WHERE COD_PROG='"+tmp.getCodice()+"';");
			return tmp;
		}
		
				

		public synchronized Sala updateSala(Sala salaAgg,String nomeSalaDaModificare ) throws SQLException, SalaEsistenteException, SalaNonEsistenteException{ 
			try{
			if(salaAgg.getNome().equalsIgnoreCase(nomeSalaDaModificare))
			  throw new SalaNonEsistenteException(salaAgg.getNome());
			  getSala(salaAgg.getNome());
			 throw new SalaEsistenteException(salaAgg.getNome());
			}
			catch(SalaNonEsistenteException e){
			 Statement query = accDB.createStatement();
             query.executeUpdate("UPDATE "+CostantiDatabase.NAME_TABLE_SALE+" SET NOME='"+salaAgg.getNome()+"',"+" POSTI='"+salaAgg.getPosti()+"' WHERE NOME='"+nomeSalaDaModificare+"';");
			 return getSala(salaAgg.getNome());
			}
			
		}
		
	
		
		public synchronized Programmazione[] filterProgrammazioniByFilm(String codiceFilm) throws SQLException, ProgrammazioneNonEsistenteException{
			ArrayList<Programmazione> programmazioni = new ArrayList<Programmazione>();
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" WHERE CODICE_FILM='"+codiceFilm+"'");
			if(!result.first())
				throw new ProgrammazioneNonEsistenteException();	
			Programmazione nuovo = new Programmazione(result.getString("COD_PROG"), result.getString("NOME_SALA"),
					 result.getString("CODICE_FILM"), result.getString("NOME_FILM"), result.getString("DATA"),result.getString("ORA"),result.getInt("POSTI_PRENOTATI"));	
			programmazioni.add(nuovo);
			while(result.next()){
				 nuovo = new Programmazione(result.getString("COD_PROG"), result.getString("NOME_SALA"),
						 result.getString("CODICE_FILM"), result.getString("NOME_FILM"), result.getString("DATA"),result.getString("ORA"),result.getInt("POSTI_PRENOTATI"));	
				programmazioni.add(nuovo);
			}
			Programmazione[] tmpprog = new Programmazione[programmazioni.size()];
			int i=0;
			for(Programmazione p:programmazioni){
				tmpprog[i++]=p;
			}
			return tmpprog;
		}
		
		public synchronized Sala[] getAllSale() throws SQLException{
			ArrayList<Sala> sale = new ArrayList<Sala>();
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_SALE);
			while(result.next()){
				Sala tmp = new Sala(result.getString("NOME"), result.getInt("POSTI"));
				sale.add(tmp);
			}
			Sala[] tmpsale = new Sala[sale.size()];
			int i=0;
			for(Sala s:sale){
				tmpsale[i++]=s;
			}
			return tmpsale;
		}
		
		public synchronized Sala getSala(String nomeSala) throws SQLException, SalaNonEsistenteException{
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_SALE+" WHERE NOME='"+nomeSala+"'");
			if(!result.first())
				throw new SalaNonEsistenteException(nomeSala);
			Sala nuova = new Sala(result.getString("NOME"), result.getInt("POSTI"));
			return nuova;
		}
		

		public synchronized Sala addSala(Sala s) throws SQLException, SalaNonEsistenteException, SalaEsistenteException{
			try{
				getSala(s.getNome());
				throw new SalaEsistenteException(s.getNome());
			}
			catch(SalaNonEsistenteException ex){
				Statement insert = accDB.createStatement();
				insert.execute("INSERT INTO "+CostantiDatabase.NAME_TABLE_SALE+" (NOME, POSTI) VALUES ('"+
								s.getNome()+"','"+s.getPosti()+"');");
				return getSala(s.getNome());
			}
		}
		
		public synchronized Sala removeSala(String nome) throws SQLException, SalaNonEsistenteException{
			Sala tmp = getSala(nome);
			Statement insert = accDB.createStatement();
			insert.execute("DELETE FROM "+CostantiDatabase.NAME_TABLE_SALE+" WHERE NOME='"+tmp.getNome()+"';");
			return tmp;
		}	
		
		public synchronized Utente getUtenteByEmail(String email) throws UtenteNonEsistenteException, SQLException{
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_UTENTI+" WHERE EMAIL='"+email+"'");
			if(!result.first())
				throw new UtenteNonEsistenteException(email);
			Utente nuovo = new Utente(result.getBoolean("ADMIN"),result.getString("USERNAME"),result.getString("PASSWORD"),result.getString("NOME"),
					result.getString("COGNOME"),result.getString("EMAIL"));
			return nuovo;
		}
		

	
		public synchronized Utente getUtenteByUsername(String username) throws UtenteNonEsistenteException, SQLException{
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_UTENTI+" WHERE USERNAME='"+username+"'");
			if(!result.first())
				throw new UtenteNonEsistenteException(username);
			Utente nuovo = new Utente(result.getBoolean("ADMIN"),result.getString("USERNAME"),result.getString("PASSWORD"),result.getString("NOME"),
					result.getString("COGNOME"),result.getString("EMAIL"));
			return nuovo;
		}


		public synchronized Utente[] getAllUtenti() throws SQLException{
			ArrayList<Utente> utenti = new ArrayList<Utente>();
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_UTENTI);
			while(result.next()){
				Utente nuovo = new Utente(result.getBoolean("ADMIN"),result.getString("USERNAME"),result.getString("PASSWORD"),result.getString("NOME"),
						result.getString("COGNOME"),result.getString("EMAIL"));	
				utenti.add(nuovo);
			}
			
			Utente[] tempUtenti = new Utente[utenti.size()];
			int i=0;
			for(Utente u: utenti){
				tempUtenti[i++]=u;
			}
			return tempUtenti;
		}
		
		
		public synchronized Utente addUser(Utente u) throws UtenteNonEsistenteException, SQLException, UtenteEsistenteException, UtenteAmministratoreGiaRegistratoException{
			Statement insert = accDB.createStatement();
			ResultSet result = insert.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_UTENTI+" WHERE ADMIN=("+1+")");
			if(result.first() && u.isAdmin())
				throw new UtenteAmministratoreGiaRegistratoException();
			try{
				getUtenteByUsername(u.getUsername());
				throw new UtenteEsistenteException(u.getUsername());
			}
			catch(UtenteNonEsistenteException ex){
				try{
					getUtenteByEmail(u.getEmail());
					throw new UtenteEsistenteException(u.getUsername());
				}catch(UtenteNonEsistenteException ex2){
					 insert = accDB.createStatement();
					insert.execute("INSERT INTO "+CostantiDatabase.NAME_TABLE_UTENTI+" (ADMIN, USERNAME, PASSWORD, NOME, COGNOME, EMAIL) VALUES (b'"+(u.isAdmin()?"1":"0")
							+"','"+u.getUsername()+"','"+u.getPassword()+"','"+u.getNome()+"','"+u.getCognome()+"','"+u.getEmail()+ "');");
					return getUtenteByUsername(u.getUsername());
				}
			}
		}
		
		public synchronized Utente removeUtente(String user) throws SQLException, UtenteNonEsistenteException{
			Utente tmp = getUtenteByUsername(user);
			Statement insert = accDB.createStatement();
			insert.execute("DELETE FROM "+CostantiDatabase.NAME_TABLE_UTENTI+" WHERE USERNAME='"+tmp.getUsername()+"';");
			return tmp;
		}	
		
		public synchronized Prenotazione addPrenotazione(Prenotazione p) throws SQLException, PrenotazioniInesistentiException, ProgrammazioneNonEsistenteException, SalaNonEsistenteException, PostiNonSufficientiException {
			Programmazione prog = getProgrammazione(p.getProgrammazione());
			Sala tmpsala = getSala(prog.getSala());
			int postiTot= prog.getPostiPrenotati()+p.getNumeroPosti();
			if(postiTot<=tmpsala.getPosti()){
				Statement insert = accDB.createStatement();
				insert.execute("INSERT INTO "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+" (COD_PRENOTAZIONE, USERNAME, COD_SPETTACOLO, POSTI_PRENOTATI) VALUES ('"+
						p.getCodice()+"','"+p.getUsername()+"','"+p.getProgrammazione()+"','"+p.getNumeroPosti()+"');");
				insert.execute("UPDATE "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" SET POSTI_PRENOTATI='"+postiTot+"' WHERE COD_PROG='"+prog.getCodice()+"';");
				return getPrenotazione(p.getCodice());
			} 
			else
				throw new PostiNonSufficientiException();
		}
		
		public synchronized Prenotazione getPrenotazione(String codice) throws PrenotazioniInesistentiException, SQLException{
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+" WHERE COD_PRENOTAZIONE='"+codice+"'");
			if(!result.first())
				throw new PrenotazioniInesistentiException();
			Prenotazione nuova = new Prenotazione(result.getString("COD_PRENOTAZIONE"), result.getString("USERNAME"), result.getString("COD_SPETTACOLO"), result.getInt("POSTI_PRENOTATI"));
			return nuova;
		}
		

		public synchronized Prenotazione[] getAllPrenotazioni() throws SQLException{
			ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI);
			while(result.next()){
				Prenotazione nuova = new Prenotazione(result.getString("COD_PRENOTAZIONE"), result.getString("USERNAME"), result.getString("COD_SPETTACOLO"), result.getInt("POSTI_PRENOTATI"));
				prenotazioni.add(nuova);
			}
			
			Prenotazione[] temp = new Prenotazione[prenotazioni.size()];
			int i=0;
			for(Prenotazione p: prenotazioni){
				temp[i++]=p;
			}
			return temp;
		}

		
		public synchronized Prenotazione[] filterPrenotazioniByUsername(String username) throws SQLException, PrenotazioniInesistentiException{
			Statement query = accDB.createStatement();
			ArrayList<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+" WHERE USERNAME='"+username+"'");
			if(!result.first())
				throw new PrenotazioniInesistentiException();
			Prenotazione nuova = new Prenotazione(result.getString("COD_PRENOTAZIONE"), result.getString("USERNAME"), result.getString("COD_SPETTACOLO"), result.getInt("POSTI_PRENOTATI"));
			prenotazioni.add(nuova);
			while(result.next()){
				 nuova = new Prenotazione(result.getString("COD_PRENOTAZIONE"), result.getString("USERNAME"), result.getString("COD_SPETTACOLO"), result.getInt("POSTI_PRENOTATI"));
				prenotazioni.add(nuova);
			}
			
			Prenotazione[] tmppren = new Prenotazione[prenotazioni.size()];
			int i=0;
			for(Prenotazione p:prenotazioni){
				tmppren[i++]=p;
			}
			return tmppren;
		}
	
	
		public synchronized Prenotazione removePrenotazione(String codicePrenotazione) throws SQLException, PrenotazioniInesistentiException, ProgrammazioneNonEsistenteException, SalaNonEsistenteException{
			Prenotazione p=getPrenotazione(codicePrenotazione);
			Programmazione prog = getProgrammazione(p.getProgrammazione());
			Statement query = accDB.createStatement();
			query.execute("DELETE FROM "+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+" WHERE COD_PRENOTAZIONE='"+p.getCodice()+"';");
			int postiTot= prog.getPostiPrenotati()-p.getNumeroPosti();
			query.executeUpdate("UPDATE "+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+" SET POSTI_PRENOTATI='"+postiTot+"' WHERE COD_PROG='"+prog.getCodice()+"';");
	       
	        return p;	  		    
		}
		
		public synchronized void associaToken(String token,String Username) throws SQLException{
			
				Statement insert = accDB.createStatement();
				insert.execute("INSERT INTO "+CostantiDatabase.NAME_TABLE_TOKEN+" (TOKE, USERNAME) VALUES ('"+token+"','"+Username+"');");		
		}

		public synchronized String getUsernameByToken(String token) throws SQLException, AccessoNonAutorizzato {
			Statement query = accDB.createStatement();
			ResultSet result = query.executeQuery("SELECT * FROM "+CostantiDatabase.NAME_TABLE_TOKEN+" WHERE TOKE='"+token+"'");
			if(!result.first())
				throw new AccessoNonAutorizzato();
			String user = result.getString("USERNAME");
			return user;
		}
		
		public synchronized String removeToken(String token) throws SQLException{
			String toke=token;
			Statement query = accDB.createStatement();
			query.execute("DELETE FROM "+CostantiDatabase.NAME_TABLE_TOKEN+" WHERE TOKE='"+token+"';");
	       	return toke;
		}
		
		
		
}
