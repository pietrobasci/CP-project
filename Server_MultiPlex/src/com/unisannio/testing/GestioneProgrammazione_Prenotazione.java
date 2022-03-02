package com.unisannio.testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unisannio.shared.ProgrammazioneEsistenteException;
import com.unisannio.shared.PostiNonSufficientiException;
import com.unisannio.shared.PrenotazioniInesistentiException;
import com.unisannio.shared.ProgrammazioneNonEsistenteException;
import com.unisannio.shared.SalaNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Film;
import com.unisannio.theSystem.Prenotazione;
import com.unisannio.theSystem.Programmazione;
import com.unisannio.theSystem.Sala;


public class GestioneProgrammazione_Prenotazione {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Film film1 = new Film("Cod4","Il Gladiatore",120,"storico","tarantino","film molto bello", new byte[0]);
		db.inserisciFilm(film1);
		Sala sala = new Sala("sala89",20);
		db.addSala(sala);
		Programmazione prog1 = new Programmazione("Cod12","sala89","Cod4","Il Gladiatore","18/01/2017","20:00",0);
		db.addProgrammazione(prog1);
		Prenotazione prenot = new Prenotazione("ffff","utente1","Cod12",2);
        db.addPrenotazione(prenot);


	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Film film1 = new Film("Cod4","Il Gladiatore",120,"storico","tarantino","film molto bello", new byte[0]);
		db.removeFilm(film1.getCodice());
		Sala sala = new Sala("sala89",20);
		db.removeSala(sala.getNome());
	}

	@Test
	public void deletePrenotazioneTest() throws SQLException, PrenotazioniInesistentiException, ProgrammazioneNonEsistenteException, SalaNonEsistenteException  {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Prenotazione prenot = new Prenotazione("ffff","utente1","Cod12",2);
		int length_before = db.getAllPrenotazioni().length;		
	    assertEquals(prenot,db.removePrenotazione(prenot.getCodice()));
	    assertEquals(length_before - 1, db.getAllPrenotazioni().length);
	}
	
	@Test(expected=PrenotazioniInesistentiException.class)
	public void filterPrenotazioneByUsernameTest() throws SQLException, PrenotazioniInesistentiException, ProgrammazioneNonEsistenteException, SalaNonEsistenteException, PostiNonSufficientiException  {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Prenotazione prenot = new Prenotazione("ffff","utente","Cod12",2);
	    assertTrue(db.filterPrenotazioniByUsername(prenot.getUsername()).length>0);
	}
	
	@Test
	public void AddPrenotazioneTest() throws SQLException, PrenotazioniInesistentiException, ProgrammazioneNonEsistenteException, SalaNonEsistenteException, PostiNonSufficientiException  {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Prenotazione prenot = new Prenotazione("ffff","utente1","Cod12",2);
		int length_before = db.getAllPrenotazioni().length;		
	    assertEquals(prenot,db.addPrenotazione(prenot));
	    assertEquals(length_before + 1, db.getAllPrenotazioni().length);
	}
	
	
	
	@Test(expected=ProgrammazioneNonEsistenteException.class)
	public void filterProgrammazioneByFilmtest() throws SQLException, ProgrammazioneEsistenteException, ProgrammazioneNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
        assertTrue(db.filterProgrammazioniByFilm("Codk87k").length==0);
	}
	
	@Test(expected=ProgrammazioneEsistenteException.class)
	public void AddProgrammazionetest() throws SQLException, ProgrammazioneEsistenteException, ProgrammazioneNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Programmazione prog1 = new Programmazione("Cod12","sala89","Cod4","Il Gladiatore","18/01/2017","20:00",0);
        assertEquals(prog1,db.addProgrammazione(prog1));
	}
	
	@Test
	public void UpdateProgrammazioneTest() throws SQLException, ProgrammazioneEsistenteException, ProgrammazioneNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Programmazione prog1 = new Programmazione("Cod12","sala89","Cod4","Il Gladiatore","18/01/2017","20:00",0);
		Programmazione prog2 = new Programmazione("Cod999","sala89","Cod4","Il Gladiatore","18/01/2017","20:00",0);
		assertEquals(prog2,db.updateProgra(prog2,prog1.getCodice()));
	}
	
	@Test
	public void DeleteProgrammazionetest() throws SQLException, ProgrammazioneNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Programmazione prog1 = new Programmazione("Cod999","sala89","Cod4","Il Gladiatore","18/01/2017","20:00",0);
		int length_before = db.getAllProgrammazioni().length;		
		assertEquals(prog1,db.removeProgrammazione(prog1.getCodice()));
	    assertEquals(length_before - 1, db.getAllProgrammazioni().length);
	}

}
