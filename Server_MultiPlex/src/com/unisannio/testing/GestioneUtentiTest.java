package com.unisannio.testing;

import static org.junit.Assert.*;

import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.unisannio.shared.UtenteAmministratoreGiaRegistratoException;
import com.unisannio.shared.UtenteEsistenteException;
import com.unisannio.shared.UtenteNonEsistenteException;
import com.unisannio.theSystem.DatabaseSystem;
import com.unisannio.theSystem.Utente;



public class GestioneUtentiTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut2 = new Utente(false,"user2","pass2","pietro2","pietro2","pietro2@gmail.com");
		db.addUser(ut2);

	}
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut = new Utente(false,"user","pass","pietro","pietro","pietro@gmail.com");
		Utente ut2 = new Utente(false,"user2","pass2","pietro2","pietro2","pietro2@gmail.com");
		db.removeUtente(ut2.getUsername());
		db.removeUtente(ut.getUsername());		
	}

	@Test
	public void addUtentetest() throws SQLException, UtenteNonEsistenteException, UtenteEsistenteException, UtenteAmministratoreGiaRegistratoException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut = new Utente(false,"user","pass","pietro","pietro","pietro@gmail.com");
		int length_before = db.getAllUtenti().length;		
		assertEquals(ut,db.addUser(ut));
		assertEquals(length_before + 1, db.getAllUtenti().length);
	}

	@Test(expected = UtenteEsistenteException.class)
	public void addUtente2test() throws SQLException, UtenteNonEsistenteException, UtenteEsistenteException, UtenteAmministratoreGiaRegistratoException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut2 = new Utente(false,"user2","pass2","pietro2","pietro2","pietro2@gmail.com");
		int length_before = db.getAllUtenti().length;		
		assertEquals(ut2,db.addUser(ut2));
		assertEquals(length_before + 1, db.getAllUtenti().length);
	}
	
	@Test
	public void getUtenteByUsernametest() throws SQLException, UtenteNonEsistenteException, UtenteEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut = new Utente(false,"user2","pass2","pietro2","pietro2","pietro2@gmail.com");
		assertEquals(ut,db.getUtenteByUsername(ut.getUsername()));
	}
	
	@Test
	public void getUtenteByEmailtest() throws SQLException, UtenteNonEsistenteException, UtenteEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut = new Utente(false,"user2","pass2","pietro2","pietro2","pietro2@gmail.com");
		assertEquals(ut,db.getUtenteByEmail(ut.getEmail()));
	}
	
	@Test(expected=UtenteNonEsistenteException.class)
	public void getUtenteNonEsistentetest() throws SQLException, UtenteNonEsistenteException, UtenteEsistenteException {
		DatabaseSystem db = DatabaseSystem.getInstance();
		Utente ut = new Utente(false,"non","non","non","non","non@gmail.com");
		assertEquals(ut,db.getUtenteByUsername(ut.getUsername()));
	}

	
}
