package com.unisannio.testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.Test;

import com.unisannio.shared.SalaEsistenteException;
import com.unisannio.shared.SalaNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Sala;



public class GestioneSaleTest {

	@Test
	public void addSalaTest() throws SQLException, SalaEsistenteException, SalaNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Sala sala = new Sala("salaProva1",50);
		int length_before = db.getAllSale().length;		
		assertEquals(sala,db.addSala(sala));
		assertEquals(length_before + 1, db.getAllSale().length);
	}
	
	@Test
	public void UpdateSalatest() throws SQLException, SalaEsistenteException, SalaNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Sala sala = new Sala("salaProva1",50);
		Sala sala2 = new Sala("salaProva1",30);
		assertEquals(sala2,db.updateSala(sala2,sala.getNome()));
	}
	
	@Test
	public void deleteSalatest() throws SQLException, SalaEsistenteException, SalaNonEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Sala sala = new Sala("salaProva1",30);
		int length_before = db.getAllSale().length;		
		assertEquals(sala,db.removeSala(sala.getNome()));
		assertEquals(length_before - 1, db.getAllSale().length);
	}
	
	@Test
	public void getAllSaleTest() throws SQLException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		assertTrue(db.getAllSale().length>0);
	}
	
}
