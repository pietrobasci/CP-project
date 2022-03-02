package com.unisannio.testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.unisannio.shared.FilmEsistenteException;
import com.unisannio.shared.FilmNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Film;

public class GestioneFilmTest {

	@Test
	public void addFilmtest() throws SQLException, FilmEsistenteException, FilmNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Film film1 = new Film("Cod4","Il Gladiatore",120,"storico","tarantino","film molto bello", new byte[0]);
		int length_before = db.getAllFilm().length;		
		assertEquals(film1,db.inserisciFilm(film1));
		assertEquals(length_before + 1, db.getAllFilm().length);
	}

	@Test
	public void UpdateFilmtest() throws SQLException, FilmEsistenteException, FilmNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Film film1 = new Film("Cod4","Il Gladiatore",120,"storico","tarantino","film molto bello", new byte[0]);
		Film film2=new Film("Cod2","Il Mago",130,"fantascienza","russel crow","film molto brutto", new byte[0]);
		assertEquals(film2,db.updateFilm(film2,film1.getCodice()));
	}
	
	@Test
	public void delteteFilmtest() throws SQLException, FilmEsistenteException, FilmNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Film film1 = new Film("Cod2","Il Mago",130,"fantascienza","russel crow","film molto brutto", new byte[0]);
		int length_before = db.getAllFilm().length;		
		assertEquals(film1,db.removeFilm(film1.getCodice()));
		assertEquals(length_before - 1, db.getAllFilm().length);
	}
	
	@Test
	public void getAllFilmTest() throws SQLException, FilmEsistenteException, FilmNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		assertTrue(db.getAllFilm().length>0);
	}
	
	
}
