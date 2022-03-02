package com.unisannio.theSystem;


public class CostantiDatabase 
{
	public final static String SERVER_NAME = "localhost";
	public final static String PORT_NUMBER = "3306";
	public final static String URL = "jdbc:mysql://"+CostantiDatabase.SERVER_NAME+":"+CostantiDatabase.PORT_NUMBER;
	public final static String DB_USER = "root";
	public final static String DB_PASSWORD = "";
	public final static String DB_NAME = "Multisala2";

	public final static String NAME_TABLE_UTENTI = "Utenti";
	public final static String NAME_TABLE_PROGRAMMAZIONE = "Programmazioni";
	public final static String NAME_TABLE_SALE = "Sale";
	public final static String NAME_TABLE_FILM = "Film";
	public final static String NAME_TABLE_PRENOTAZIONI = "Prenotazioni";
	public final static String NAME_TABLE_TOKEN = "Token";
	
	public final static String CREATE_DATABASE = "CREATE DATABASE "+CostantiDatabase.DB_NAME;
	public final static String CREATE_TABLE_SALE =	"CREATE TABLE `"+CostantiDatabase.NAME_TABLE_SALE+"` ("+
													"`NOME` VARCHAR(50) NOT NULL,"+
													"`POSTI` INT(11) NOT NULL,"+
													"PRIMARY KEY (`NOME`)"+
													")";
	
	public final static String CREATE_TABLE_FILM = "CREATE TABLE `" + CostantiDatabase.NAME_TABLE_FILM + "` ("+
											 		"`CODICE_FILM` CHAR(10) NOT NULL,"+
											 		"`NOME` VARCHAR(50) NOT NULL,"+
											 		"`DURATA` INT(11) NOT NULL,"+
											 		"`GENERE` VARCHAR(50) NOT NULL,"+
											 		"`REGISTA` VARCHAR(50) NOT NULL,"+
											 		"`DESCRIZIONE` TEXT NOT NULL,"+
											 		"`COPERTINA` MEDIUMTEXT NOT NULL,"+
											 		"PRIMARY KEY (`CODICE_FILM`)"+
											 		")";
	
	public final static String CREATE_TABLE_PROGRAMMAZIONE = "CREATE TABLE `"+CostantiDatabase.NAME_TABLE_PROGRAMMAZIONE+"` ("+
															 "`COD_PROG` CHAR(10) NOT NULL,"+
															 "`NOME_SALA` VARCHAR(50) NOT NULL,"+
															 "`CODICE_FILM` CHAR(10) NOT NULL,"+
															 "`NOME_FILM` VARCHAR(50) NOT NULL,"+
															 "`DATA` VARCHAR(50) NOT NULL,"+
															 "`ORA` VARCHAR(50) NOT NULL,"+
															 "`POSTI_PRENOTATI` INT(11) NOT NULL,"+
															 "PRIMARY KEY (`COD_PROG`),"+
															 "UNIQUE (`NOME_SALA`, `DATA`, `ORA`),"+
															 "FOREIGN KEY (`CODICE_FILM`) REFERENCES `film` (`CODICE_FILM`) ON UPDATE CASCADE ON DELETE CASCADE,"+
															 "FOREIGN KEY (`NOME_SALA`) REFERENCES `sale` (`NOME`) ON UPDATE CASCADE ON DELETE CASCADE"+
															 ")";
	
	public final static String CREATE_TABLE_UTENTI = "CREATE TABLE `"+CostantiDatabase.NAME_TABLE_UTENTI+"` ("+
													 "`USERNAME` VARCHAR(50) NOT NULL,"+
													 "`PASSWORD` VARCHAR(50) NOT NULL,"+
													 "`NOME` VARCHAR(50) NOT NULL,"+
													 "`COGNOME` VARCHAR(50) NOT NULL,"+
													 "`EMAIL` VARCHAR(50) NOT NULL,"+
													 "`ADMIN` BIT(1) NOT NULL,"+
													 "PRIMARY KEY (`USERNAME`),"+
													 "UNIQUE (`EMAIL`)"+
													 ")";
	
	public final static String CREATE_TABLE_PRENOTAZIONI = "CREATE TABLE `"+CostantiDatabase.NAME_TABLE_PRENOTAZIONI+"` ("+
														   "`COD_PRENOTAZIONE` VARCHAR(50) NOT NULL ,"+
														   "`USERNAME` CHAR(10) NOT NULL,"+
														   "`COD_SPETTACOLO` CHAR(10) NOT NULL,"+
														   "`POSTI_PRENOTATI` INT NOT NULL,"+
														   "PRIMARY KEY (`COD_PRENOTAZIONE`),"+
														   "FOREIGN KEY (`COD_SPETTACOLO`) REFERENCES `programmazioni` (`COD_PROG`) ON UPDATE CASCADE ON DELETE CASCADE"+
														   ")"; 
	
	public final static String CREATE_TABLE_TOKEN =	"CREATE TABLE `"+CostantiDatabase.NAME_TABLE_TOKEN+"` ("+
			"`TOKE` VARCHAR(50) NOT NULL,"+
			"`USERNAME` VARCHAR(50) NOT NULL,"+
			"PRIMARY KEY (`TOKE`)"+
			")";
}
