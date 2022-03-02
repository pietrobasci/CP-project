package com.unisannio.gui;

import com.unisannio.model.*;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;

import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import com.google.gson.Gson;

import javax.swing.ListSelectionModel;


@SuppressWarnings({"serial", "rawtypes", "unchecked"})
public class GUIGestore extends JFrame {

	private JPanel contentPane;
	private JList list, list_1, list_2;
	private Programmazione programmazioni[];
	private Film film[];
	private Sala sale[];
	private Series<Header> responseHeaders;

	/**
	 * Create the frame.
	 */

	public GUIGestore(String User, String token) {
		setTitle("Gestore");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 700, 450);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		ClientResource cr;
		String uri, json;
		Gson gson = new Gson();
		
		try{
			uri = "http://localhost:8182/film/all/";
			cr = new ClientResource(uri);
			
			responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
			if (responseHeaders == null) { 
			    responseHeaders = new Series<Header>(Header.class); 
			    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
			}
			responseHeaders.set("User-Agent", "ClientJava"); 
			
			json = cr.get().getText();
			Status stato = cr.getStatus();
			if(stato.getCode()!=200){
				switch(stato.getCode()){
					case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
						break;
					default: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
						break;
				}
			}
			else
				film = gson.fromJson(json, Film[].class);
		
			uri = "http://localhost:8182/sale/all/";
			cr = new ClientResource(uri);
			
			responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
			if (responseHeaders == null) { 
			    responseHeaders = new Series<Header>(Header.class); 
			    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
			}
			responseHeaders.set("User-Agent", "ClientJava");
			
			json = cr.get().getText();
			stato = cr.getStatus();
			if(stato.getCode()!=200){
				switch(stato.getCode()){
					case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
						break;
					default: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
						break;
				}
			}
			else
				sale = gson.fromJson(json, Sala[].class);
			
			uri = "http://localhost:8182/prog/all/";
			cr = new ClientResource(uri);
			
			responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
			if (responseHeaders == null) { 
			    responseHeaders = new Series<Header>(Header.class); 
			    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
			}
			responseHeaders.set("User-Agent", "ClientJava");
			
			json = cr.get().getText();
			stato=cr.getStatus();
			if(stato.getCode()!=200){
				switch(stato.getCode()){
					case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
						break;
					default: 
						JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
						break;
				}
			}
			else
				programmazioni = gson.fromJson(json,Programmazione[].class);
			
		}
		catch (ResourceException | IOException e1){
			JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al Server");
			System.exit(-1);
		}	

		
		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		
		JMenu mnAmministratore = new JMenu("Amministratore");
		mnAmministratore.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuBar.add(mnAmministratore);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Account");
		mntmNewMenuItem.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		mnAmministratore.add(mntmNewMenuItem);
		mntmNewMenuItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ClientResource cr;
				String uri, json;
				Gson gson = new Gson();
				Utente u = null;
				
				try{
					uri = "http://localhost:8182/user/" + User + "&" + token;
					cr = new ClientResource(uri);
					
					responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
					if (responseHeaders == null) { 
					    responseHeaders = new Series<Header>(Header.class); 
					    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
					}
					responseHeaders.set("User-Agent", "ClientJava"); 
					
					json = cr.get().getText();
					Status stato = cr.getStatus();
					if(stato.getCode()!=200){
						switch(stato.getCode()){
							case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
								break;
							default: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
								break;
						}
					}
					else
						u = gson.fromJson(json, Utente.class);
				}
				catch (ResourceException | IOException e1){
					JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al Server");
					System.exit(-1);
				}

				JFrame frmAccount = new JFrame();
				frmAccount.setTitle("Account");
				frmAccount.setBounds(100, 100, 450, 300);
				frmAccount.getContentPane().setLayout(null);
				
				JLabel lblNewLabel = new JLabel("Amministratore");
				lblNewLabel.setBounds(0, 1, 450, 55);
				lblNewLabel.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				frmAccount.getContentPane().add(lblNewLabel);
				
				JLabel lblNewLabel_1 = new JLabel(u.getNome());
				lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_1.setBounds(0, 65, 450, 55);
				frmAccount.getContentPane().add(lblNewLabel_1);
				
				JLabel lblNewLabel_2 = new JLabel(u.getCognome());
				lblNewLabel_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblNewLabel_2.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_2.setBounds(0, 105, 450, 55);
				frmAccount.getContentPane().add(lblNewLabel_2);
				
				JLabel lblNewLabel_3 = new JLabel(u.getEmail());
				lblNewLabel_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblNewLabel_3.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_3.setBounds(0, 145, 450, 55);
				frmAccount.getContentPane().add(lblNewLabel_3);
				
				JLabel lblNewLabel_4 = new JLabel(u.getUsername());
				lblNewLabel_4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblNewLabel_4.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_4.setBounds(0, 185, 450, 55);
				frmAccount.getContentPane().add(lblNewLabel_4);	
				frmAccount.setVisible(true);
			}	
		});
		
		JMenuItem mntmLogout = new JMenuItem("Logout");
		mntmLogout.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		mnAmministratore.add(mntmLogout);
		mntmLogout.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				GUIGestore.this.dispose();
		        new GUIAccesso();		
			}	
		});
		
		JMenu mnUtenti = new JMenu("Utenti");
		mnAmministratore.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuBar.add(mnUtenti);
		
		JMenuItem mntmAllUser = new JMenuItem("Tutti gli utenti");
		mntmAllUser.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		mnUtenti.add(mntmAllUser);
		mntmAllUser.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ClientResource cr;
				String uri, json;
				Gson gson = new Gson();
				Utente utenti[] = null;
				
				try{
					uri = "http://localhost:8182/user/all/" + token;
					cr = new ClientResource(uri);
					
					responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
					if (responseHeaders == null) { 
					    responseHeaders = new Series<Header>(Header.class); 
					    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
					}
					responseHeaders.set("User-Agent", "ClientJava"); 
					
					json = cr.get().getText();
					Status stato = cr.getStatus();
					if(stato.getCode()!=200){
						switch(stato.getCode()){
							case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
								break;
							default: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
								break;
						}
					}
					else
						utenti = gson.fromJson(json, Utente[].class);
				}
				catch (ResourceException | IOException e1){
					JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al Server");
					System.exit(-1);
				}
				
				JFrame frame = new JFrame();
				frame.setBounds(100, 100, 450, 300);
				frame.getContentPane().setLayout(new BorderLayout(0, 0));
				
				JLabel label = new JLabel("Tutti gli utenti");
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				frame.getContentPane().add(label, BorderLayout.NORTH);
				
				JList users = new JList();
				users.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				users.setListData(utenti);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(users);
				frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
				frame.setVisible(true);
			}
		});
		
		JMenu mnPren = new JMenu("Prenotazioni");
		mnPren.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		menuBar.add(mnPren);
		
		JMenuItem mntmAllPren = new JMenuItem("Tutte le prenotazioni");
		mntmAllPren.setFont(new Font("Helvetica Neue", Font.PLAIN, 14));
		mnPren.add(mntmAllPren);
		mntmAllPren.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				ClientResource cr;
				String uri, json;
				Gson gson = new Gson();
				Prenotazione prenotazioni[] = null;
				
				try{
					uri = "http://localhost:8182/prenotazioni/all/" + token;
					cr = new ClientResource(uri);
					
					responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
					if (responseHeaders == null) { 
					    responseHeaders = new Series<Header>(Header.class); 
					    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
					}
					responseHeaders.set("User-Agent", "ClientJava"); 
					
					json = cr.get().getText();
					Status stato = cr.getStatus();
					if(stato.getCode()!=200){
						switch(stato.getCode()){
							case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
								break;
							default: 
								JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
								break;
						}
					}
					else
						prenotazioni = gson.fromJson(json, Prenotazione[].class);
				}
				catch (ResourceException | IOException e1){
					JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al Server");
					System.exit(-1);
				}
				
				JFrame frame = new JFrame();
				frame.setBounds(100, 100, 450, 300);
				frame.getContentPane().setLayout(new BorderLayout(0, 0));
				
				JLabel label = new JLabel("Tutti le prenotazioni");
				label.setHorizontalAlignment(SwingConstants.CENTER);
				label.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				frame.getContentPane().add(label, BorderLayout.NORTH);
				
				JList users = new JList();
				users.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				users.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				users.setListData(prenotazioni);
				JScrollPane scrollPane = new JScrollPane();
				scrollPane.setViewportView(users);
				frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
				frame.setVisible(true);
			}
		});
		
		JLabel lblMultisala = new JLabel("Multisala");
		lblMultisala.setFont(new Font("Helvetica Neue", Font.ITALIC, 60));
		lblMultisala.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblMultisala, BorderLayout.NORTH);
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("FILM", null, panel, null);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel.add(panel_3, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("Aggiungi");
		btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_3.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				JFrame frame = new JFrame();
				frame.setBounds(450, 100, 500, 630);
		    	frame.setTitle("Aggiungi");
				frame.setResizable(false);
				frame.getContentPane().setLayout(null);
				
				JLabel lblNuovoFilm = new JLabel("Nuovo Film");
				lblNuovoFilm.setHorizontalAlignment(SwingConstants.CENTER);
				lblNuovoFilm.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				lblNuovoFilm.setBounds(0, 11, 500, 28);
				frame.getContentPane().add(lblNuovoFilm);
				
				JLabel lblCodiceFilm = new JLabel("Codice Film");
				lblCodiceFilm.setHorizontalAlignment(SwingConstants.CENTER);
				lblCodiceFilm.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblCodiceFilm.setBounds(10, 71, 185, 28);
				frame.getContentPane().add(lblCodiceFilm);
				
				JLabel lblTitolo = new JLabel("Titolo Film");
				lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
				lblTitolo.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblTitolo.setBounds(10, 120, 185, 28);
				frame.getContentPane().add(lblTitolo);
				
				JLabel lblGenere = new JLabel("Genere");
				lblGenere.setHorizontalAlignment(SwingConstants.CENTER);
				lblGenere.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblGenere.setBounds(10, 168, 185, 28);
				frame.getContentPane().add(lblGenere);
				
				JLabel lblDurata = new JLabel("Regista");
				lblDurata.setHorizontalAlignment(SwingConstants.CENTER);
				lblDurata.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDurata.setBounds(10, 216, 185, 28);
				frame.getContentPane().add(lblDurata);
				
				JLabel lblDurata_1 = new JLabel("Durata");
				lblDurata_1.setHorizontalAlignment(SwingConstants.CENTER);
				lblDurata_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDurata_1.setBounds(10, 264, 185, 28);
				frame.getContentPane().add(lblDurata_1);
				
				JLabel lblCopertina = new JLabel("Copertina");
				lblCopertina.setHorizontalAlignment(SwingConstants.CENTER);
				lblCopertina.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblCopertina.setBounds(10, 312, 185, 28);
				frame.getContentPane().add(lblCopertina);
				
				JLabel lblDescrizione = new JLabel("Descrizione");
				lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
				lblDescrizione.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDescrizione.setBounds(10, 372, 185, 28);
				frame.getContentPane().add(lblDescrizione);
				
				JTextField txtField = new JTextField();
				txtField.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
				txtField.setBounds(276, 72, 198, 27);
				frame.getContentPane().add(txtField);
				txtField.setColumns(10);
				
				JTextField txtField_0 = new JTextField();
				txtField_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				txtField_0.setColumns(10);
				txtField_0.setBounds(276, 120, 198, 27);
				frame.getContentPane().add(txtField_0);
				
				JTextField textField_1 = new JTextField();
				textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_1.setColumns(10);
				textField_1.setBounds(276, 168, 198, 27);
				frame.getContentPane().add(textField_1);
				
				JTextField textField_2 = new JTextField();
				textField_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_2.setColumns(10);
				textField_2.setBounds(276, 216, 198, 27);
				frame.getContentPane().add(textField_2);
				
				JTextField textField_3 = new JTextField();
				textField_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_3.setColumns(10);
				textField_3.setBounds(276, 264, 198, 27);
				frame.getContentPane().add(textField_3);
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField.setColumns(10);
				textField.setBounds(276, 312, 198, 27);
				frame.getContentPane().add(textField);
				
				JTextArea textArea = new JTextArea();
				textArea.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textArea.setLineWrap(true);
				textArea.setBounds(276, 317, 198, 197);

				JScrollPane scrollPane = new JScrollPane(textArea, 
						   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
				scrollPane.setBounds(276, 376, 198, 197);
				frame.getContentPane().add(scrollPane);
				
				JButton btnNewButton = new JButton("Aggiungi");
				btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				btnNewButton.setBounds(59, 477, 109, 38);
				frame.getContentPane().add(btnNewButton);
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!txtField.getText().isEmpty() && !txtField_0.getText().isEmpty() && !textField_3.getText().isEmpty() && !textField_1.getText().isEmpty()&& !textField_2.getText().isEmpty()&& !(textArea.getText().isEmpty())&& !(textField.getText().isEmpty())){ 
							int risposta=JOptionPane.showConfirmDialog(frame, "Vuoi procedere con il salvataggio?","AGGIUNGI",JOptionPane.YES_NO_OPTION);
			                if(risposta==JOptionPane.YES_OPTION){	
			                	ClientResource cr;
								Gson gson = new Gson();
								Status status;
								String URI, json;
									
								File fi = new File(textField.getText());
								byte[] file = null;
								try {
									file = Files.readAllBytes(fi.toPath());
								} catch (IOException e1) {
									JOptionPane.showMessageDialog(frame, "Indirizzo immagine non corretto.\n L'indirizzo deve assumere la forma: \n'/Users/pietrobasci/Desktop/example.jpg'");
									return;
								}
								
		                        Film p = new Film(txtField.getText().toString(),txtField_0.getText().toString(),Integer.parseInt(textField_3.getText().toString()),textField_1.getText().toString(),textField_2.getText().toString(),textArea.getText().toString(), file);
								URI = "http://localhost:8182/film/"+p.getCodice()+"&"+token;   
								cr = new ClientResource(URI);
								
								responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
								if (responseHeaders == null) { 
									responseHeaders = new Series<Header>(Header.class); 
								    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								}
								responseHeaders.set("User-Agent", "ClientJava");
		                        
								try {
									json = cr.post(gson.toJson(p,Film.class)).getText();
									status = cr.getStatus();
									
									if(status.getCode()!=200){
										switch(status.getCode()){
											case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
												JOptionPane.showMessageDialog(frame, "Impossibile collegarsi al database");
												break;
											case Costanti.ECCEZIONE_FILM_DUPLICATO: 
												JOptionPane.showMessageDialog(frame, "Attenzione il film con codice '"+p.getCodice()+"' già è presente nel database");
											 	break;
											case Costanti.ECCEZIONE_FILM_INESISTENTE: 
												JOptionPane.showMessageDialog(frame, "Impossibile inserire il film nel database");
												break;
											default: 
												JOptionPane.showMessageDialog(frame, "Errore generico");
												break;
										}
									} 
									else {
										URI = "http://localhost:8182/film/all/";
										cr = new ClientResource(URI);
										
										responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
										if (responseHeaders == null) { 
											responseHeaders = new Series<Header>(Header.class); 
											cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
											}
										responseHeaders.set("User-Agent", "ClientJava");
										
										json=cr.get().getText();
										film=gson.fromJson(json, Film[].class);	
									 }
							    							    
							         list.setListData(film);
						             frame.dispose();
							     }
							     catch(ResourceException | IOException e5){
							    	 e5.printStackTrace();
							     }
								}
			                      
							  }
							    else{
									 JOptionPane.showMessageDialog(frame, "Completare tutti i campi");
							    }
							    	
						}
				});
				
				frame.setVisible(true);
			}
				
		});
		
		JButton btnNewButton_1 = new JButton("Modifica");
		btnNewButton_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_3.add(btnNewButton_1);
		btnNewButton_1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!list.isSelectionEmpty()){
					int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi modificare il film: "+ film[list.getSelectedIndex()].getTitolo() + "?", "Modifica", JOptionPane.YES_NO_OPTION);
                    if(risposta==JOptionPane.YES_OPTION){
                    	JFrame frame = new JFrame();
                    	frame.setTitle("Modifica");
        				frame.setBounds(450, 100, 500, 600);
        				frame.setResizable(false);
        				frame.getContentPane().setLayout(null);
        				
        				JLabel lblNuovoFilm = new JLabel("Nuovo Film");
        				lblNuovoFilm.setHorizontalAlignment(SwingConstants.CENTER);
        				lblNuovoFilm.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
        				lblNuovoFilm.setBounds(0, 11, 500, 28);
        				frame.getContentPane().add(lblNuovoFilm);
        				
        				JLabel lblCodiceFilm = new JLabel("Codice Film");
        				lblCodiceFilm.setHorizontalAlignment(SwingConstants.CENTER);
        				lblCodiceFilm.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblCodiceFilm.setBounds(10, 71, 185, 28);
        				frame.getContentPane().add(lblCodiceFilm);
        				
        				JLabel lblTitolo = new JLabel("Titolo Film");
        				lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        				lblTitolo.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblTitolo.setBounds(10, 120, 185, 28);
        				frame.getContentPane().add(lblTitolo);
        				
        				JLabel lblGenere = new JLabel("Genere");
        				lblGenere.setHorizontalAlignment(SwingConstants.CENTER);
        				lblGenere.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblGenere.setBounds(10, 168, 185, 28);
        				frame.getContentPane().add(lblGenere);
        				
        				JLabel lblDurata = new JLabel("Regista");
        				lblDurata.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDurata.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDurata.setBounds(10, 216, 185, 28);
        				frame.getContentPane().add(lblDurata);
        				
        				JLabel lblDurata_1 = new JLabel("Durata");
        				lblDurata_1.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDurata_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDurata_1.setBounds(10, 264, 185, 28);
        				frame.getContentPane().add(lblDurata_1);
        				
        				JLabel lblDescrizione = new JLabel("Descrizione");
        				lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDescrizione.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDescrizione.setBounds(10, 312, 185, 28);
        				frame.getContentPane().add(lblDescrizione);
        				
        				JTextField txtField = new JTextField();
        				txtField.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
        				txtField.setText(film[list.getSelectedIndex()].getCodice());
        				txtField.setBounds(276, 72, 198, 27);
        				frame.getContentPane().add(txtField);
        				txtField.setColumns(10);
        				
        				JTextField txtField_0 = new JTextField();
        				txtField_0.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				txtField_0.setText(film[list.getSelectedIndex()].getTitolo());
        				txtField_0.setColumns(10);
        				txtField_0.setBounds(276, 120, 198, 27);
        				frame.getContentPane().add(txtField_0);
        				
        				JTextField textField_1 = new JTextField();
        				textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_1.setText(film[list.getSelectedIndex()].getGenere());
        				textField_1.setColumns(10);
        				textField_1.setBounds(276, 168, 198, 27);
        				frame.getContentPane().add(textField_1);
        				
        				JTextField textField_2 = new JTextField();
        				textField_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_2.setText(film[list.getSelectedIndex()].getRegista());
        				textField_2.setColumns(10);
        				textField_2.setBounds(276, 216, 198, 27);
        				frame.getContentPane().add(textField_2);
        				
        				JTextField textField_3 = new JTextField();
        				textField_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_3.setText(Integer.toString(film[list.getSelectedIndex()].getDurata()));
        				textField_3.setColumns(10);
        				textField_3.setBounds(276, 264, 198, 27);
        				frame.getContentPane().add(textField_3);
        				
        				JTextArea textArea = new JTextArea();
        				textArea.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textArea.setLineWrap(true);
        				textArea.setText(film[list.getSelectedIndex()].getDescrizione());
        				textArea.setBounds(276, 317, 198, 197);

        				JScrollPane scrollPane = new JScrollPane(textArea, 
        						   JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        				scrollPane.setBounds(276, 317, 198, 197);
        				frame.getContentPane().add(scrollPane);
        				
        				JButton btnNewButton = new JButton("Salva");
        				btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				btnNewButton.setBounds(59, 477, 109, 38);
        				frame.getContentPane().add(btnNewButton);
        				btnNewButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
							  int risposta=JOptionPane.showConfirmDialog(frame, "Vuoi procedere con il salvataggio delle modifiche?","Modifica",JOptionPane.YES_NO_OPTION);
		                      if(risposta==JOptionPane.YES_OPTION){	
								ClientResource cr;
								Gson gson = new Gson();
								Status status;
								String URI, json;
								
								Film filmDaModifi = film[list.getSelectedIndex()];
								URI = "http://localhost:8182/film/"+filmDaModifi.getCodice()+"&"+token;   
								cr = new ClientResource(URI);
								
								responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
								if (responseHeaders == null) { 
									responseHeaders = new Series<Header>(Header.class); 
									cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								}
							    responseHeaders.set("User-Agent", "ClientJava");
							    
								Film p=new Film(txtField.getText().toString(),txtField_0.getText().toString(),Integer.parseInt(textField_3.getText().toString()),textField_1.getText().toString(),textField_2.getText().toString(),textArea.getText().toString(), filmDaModifi.getCopertina());
								try {
								  json = cr.put(gson.toJson(p,Film.class)).getText();
								  status = cr.getStatus();
								  
								  if(status.getCode()!=200){
									switch(status.getCode()){
										case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
											JOptionPane.showMessageDialog(frame, "Impossibile collegarsi al database");
											break;
										case Costanti.ECCEZIONE_FILM_DUPLICATO: 
											JOptionPane.showMessageDialog(frame, "Attenzione il film con codice '"+p.getCodice()+"' già è presente nel database");
										 	break;
										case Costanti.ECCEZIONE_FILM_INESISTENTE: 
											JOptionPane.showMessageDialog(frame, "Impossibile inserire il film nel database");
											break;
										default: 
											JOptionPane.showMessageDialog(frame, "Errore generico");
											break;
									}
								  } 
								  else {
									URI = "http://localhost:8182/film/all/";
									cr = new ClientResource(URI);
									
									responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
									if (responseHeaders == null) { 
										responseHeaders = new Series<Header>(Header.class); 
										cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
									}
									responseHeaders.set("User-Agent", "ClientJava");
									
									json=cr.get().getText();
									film=gson.fromJson(json, Film[].class);	
								  }
						    						
						            list.setListData(film);
						            
						            URI = "http://localhost:8182/prog/all/";
									cr = new ClientResource(URI);
									
									responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
									if (responseHeaders == null) { 
										responseHeaders = new Series<Header>(Header.class); 
										cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
									}
									responseHeaders.set("User-Agent", "ClientJava");
									
									json=cr.get().getText();
									programmazioni=gson.fromJson(json, Programmazione[].class);
									
						            list_1.setListData(programmazioni);
					                frame.dispose();
						      }
						      catch(ResourceException | IOException e5){
						    	 e5.printStackTrace();
						      }
							}
						  }
						});
        				
        				frame.setVisible(true);

                    }
					
				}
				else{
					JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona il film da modificare");
				}
			}
		});
		
		JButton btnNewButton_2 = new JButton("Elimina");
		btnNewButton_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_3.add(btnNewButton_2);
		btnNewButton_2.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!list.isSelectionEmpty()){
					int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi eliminare il film: " + film[list.getSelectedIndex()].getTitolo() + "?" +
					 		 "\nAttenzione: la programmazione associata a questo film verra eliminata","Elimina",JOptionPane.YES_NO_OPTION);
                    if(risposta==JOptionPane.YES_OPTION){	
                    	ClientResource cr;
						Gson gson = new Gson();
						Status status;
						String URI, json;						
						
						Film filmDaModifi=film[list.getSelectedIndex()];
						URI = "http://localhost:8182/film/"+filmDaModifi.getCodice()+"&"+token;   

						cr = new ClientResource(URI);
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
							cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						try {
						json = cr.delete().getText();
						status = cr.getStatus();
						
						if(status.getCode()!=200){
							switch(status.getCode()){
								case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
									break;
								case Costanti.ECCEZIONE_FILM_INESISTENTE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile inserire il film nel database");
									break;
								default: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
									break;
							}
						} 
						else {
							URI = "http://localhost:8182/film/all/";
							cr = new ClientResource(URI);
							
							responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
							if (responseHeaders == null) { 
								responseHeaders = new Series<Header>(Header.class); 
								cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
							}
							responseHeaders.set("User-Agent", "ClientJava");
							
							json=cr.get().getText();
							film=gson.fromJson(json, Film[].class);	
						}
				    
				        list.setListData(film);
				        URI = "http://localhost:8182/prog/all/";
						cr = new ClientResource(URI);
					    
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
						    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						json=cr.get().getText();
						programmazioni=gson.fromJson(json, Programmazione[].class);
							
				        list_1.setListData(programmazioni);

				   }
				   catch(ResourceException | IOException e5){
				    	 e5.printStackTrace();
				     }
				}
				}
				else{
					JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona il film da eliminare");
				}
			}
			
		});
		
		JScrollPane scrollPane = new JScrollPane();
		panel.add(scrollPane, BorderLayout.CENTER);
		
		list = new JList();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setListData(film);
		scrollPane.setViewportView(list);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("PROGRAMMAZIONI", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_1.add(panel_4, BorderLayout.SOUTH);
		
		JButton btnNewButton_3 = new JButton("Aggiungi");
		btnNewButton_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_4.add(btnNewButton_3);
		btnNewButton_3.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFrame fram = new JFrame();
            	fram.setTitle("Aggiungi");
				fram.setResizable(false);
				fram.setBounds(450, 100, 500, 510);
				fram.getContentPane().setLayout(null);
				
				JLabel lblNuovoFilm = new JLabel("Nuova Programmazione");
				lblNuovoFilm.setHorizontalAlignment(SwingConstants.CENTER);
				lblNuovoFilm.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				lblNuovoFilm.setBounds(10, 6, 484, 38);
				fram.getContentPane().add(lblNuovoFilm);
				
				JLabel lblCodiceFilm = new JLabel("Codice Programmazione");
				lblCodiceFilm.setHorizontalAlignment(SwingConstants.CENTER);
				lblCodiceFilm.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblCodiceFilm.setBounds(10, 72, 185, 28);
				fram.getContentPane().add(lblCodiceFilm);
				
				JLabel lblTitolo = new JLabel("Codice Film");
				lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
				lblTitolo.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblTitolo.setBounds(10, 120, 185, 28);
				fram.getContentPane().add(lblTitolo);
				
				JLabel lblGenere = new JLabel("Titolo FIlm");
				lblGenere.setHorizontalAlignment(SwingConstants.CENTER);
				lblGenere.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblGenere.setBounds(10, 168, 185, 28);
				fram.getContentPane().add(lblGenere);
				
				JLabel lblDurata = new JLabel("Sala");
				lblDurata.setHorizontalAlignment(SwingConstants.CENTER);
				lblDurata.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDurata.setBounds(10, 216, 185, 28);
				fram.getContentPane().add(lblDurata);
				
				JLabel lblDurata_1 = new JLabel("Data(dd/MM/yyyy)");
				lblDurata_1.setHorizontalAlignment(SwingConstants.CENTER);
				lblDurata_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDurata_1.setBounds(10, 264, 185, 28);
				fram.getContentPane().add(lblDurata_1);
				
				JLabel lblDescrizione = new JLabel("Ora(hh:mm)");
				lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
				lblDescrizione.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblDescrizione.setBounds(10, 312, 185, 28);
				fram.getContentPane().add(lblDescrizione);
				
				JTextField textField_0 = new JTextField();
				textField_0.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
				textField_0.setBounds(276, 72, 198, 27);
				fram.getContentPane().add(textField_0);
				textField_0.setColumns(10);
				
				JTextField textField_1 = new JTextField();
				textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_1.setColumns(10);
				textField_1.setBounds(276, 120, 198, 27);
				fram.getContentPane().add(textField_1);
				
				JTextField textField_2 = new JTextField();
				textField_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_2.setColumns(10);
				textField_2.setBounds(276, 168, 198, 27);
				fram.getContentPane().add(textField_2);
				
				JTextField textField_3 = new JTextField();
				textField_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_3.setColumns(10);
				textField_3.setBounds(276, 216, 198, 27);
				fram.getContentPane().add(textField_3);
				
				JTextField textField_4 = new JTextField();
				textField_4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_4.setColumns(10);
				textField_4.setBounds(276, 264, 198, 27);
				fram.getContentPane().add(textField_4);
				
				JTextField textField_5 = new JTextField();
				textField_5.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_5.setColumns(10);
				textField_5.setBounds(276, 313, 198, 27);
				fram.getContentPane().add(textField_5);
				
				JLabel lblPostiPrenotati = new JLabel("Posti Prenotati");
				lblPostiPrenotati.setHorizontalAlignment(SwingConstants.CENTER);
				lblPostiPrenotati.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblPostiPrenotati.setBounds(10, 360, 185, 28);
				fram.getContentPane().add(lblPostiPrenotati);
				
				JTextField textField_6 = new JTextField();
				textField_6.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_6.setColumns(10);
				textField_6.setBounds(276, 360, 198, 27);
				fram.getContentPane().add(textField_6);		
				
				JButton btnNewButton = new JButton("Aggiungi");
				btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				btnNewButton.setBounds(193, 427, 109, 38);
				fram.getContentPane().add(btnNewButton);				
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(!textField_0.getText().isEmpty() && !textField_1.getText().isEmpty() && !textField_2.getText().isEmpty() && !textField_3.getText().isEmpty()&& !textField_4.getText().isEmpty()&& !textField_5.getText().isEmpty() && !textField_6.getText().isEmpty() ){ 
							int risposta=JOptionPane.showConfirmDialog(fram, "Vuoi procedere con il salvataggio?","AGGIUNGI",JOptionPane.YES_NO_OPTION);
			                if(risposta==JOptionPane.YES_OPTION){	
							    ClientResource cr;
								Gson gson = new Gson();
								Status status;
								String URI, json;
								
		                        Programmazione p=new Programmazione(textField_0.getText().toString(),textField_3.getText().toString(),textField_1.getText().toString(),textField_2.getText().toString(),textField_4.getText().toString(),textField_5.getText().toString(),Integer.parseInt(textField_6.getText().toString()));
								URI = "http://localhost:8182/prog/"+p.getCodice()+"&"+token;   
								cr = new ClientResource(URI);
								
								responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
								if (responseHeaders == null) { 
								    responseHeaders = new Series<Header>(Header.class); 
								    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								}
								responseHeaders.set("User-Agent", "ClientJava");
		                        
								try {
									json = cr.post(gson.toJson(p,Programmazione.class)).getText();
									status = cr.getStatus();
									
									if(status.getCode()!=200){
										switch(status.getCode()){
											case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
												JOptionPane.showMessageDialog(fram, "Impossibile inserire l'elemento! \nVerificare che 'Codice film' e 'Codice sala' corrispondano rispettivamente a film e sale esistenti");
												break;
											case Costanti.ECCEZIONE_PROGRAMMAZIONE_DUPLICATA: 
												JOptionPane.showMessageDialog(fram, "Attenzione la programmazione con codice '"+p.getCodice()+"' già è presente nel database");
											 	break;
											case Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE: 
												JOptionPane.showMessageDialog(fram, "Impossibile inserire la programmazione nel database");
												break;
											default: 
												JOptionPane.showMessageDialog(fram, "Errore generico");
												break;
										}
									} 
									else {
										URI = "http://localhost:8182/prog/all/";
										cr = new ClientResource(URI);
										
										responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
										if (responseHeaders == null) { 
											responseHeaders = new Series<Header>(Header.class); 
											cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
										}
										responseHeaders.set("User-Agent", "ClientJava");
										
										json=cr.get().getText();
										programmazioni=gson.fromJson(json, Programmazione[].class);	
									}
							    							    
							        list_1.setListData(programmazioni);
						            fram.dispose();
							     }
							     catch(ResourceException | IOException e5){
							    	 e5.printStackTrace();
							     }
							}
			                      
						}
						else{
							JOptionPane.showMessageDialog(fram, "Completare tutti i campi");
						}
							    	
					}
				});
				
				fram.setVisible(true);					

			}
		});
		
		JButton btnNewButton_4 = new JButton("Modifica");
		btnNewButton_4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_4.add(btnNewButton_4);
		btnNewButton_4.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!list_1.isSelectionEmpty()){
					int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi modificare la programmazione: "+ programmazioni[list_1.getSelectedIndex()].getCodice() + "?", "Modifica", JOptionPane.YES_NO_OPTION);
                    if(risposta==JOptionPane.YES_OPTION){
                    	JFrame fram = new JFrame();
                    	fram.setTitle("Modifica");
        				fram.setResizable(false);
        				fram.setBounds(450, 100, 500, 510);
        				fram.getContentPane().setLayout(null);
        				
        				JLabel lblNuovoFilm = new JLabel("Nuova Programmazione");
        				lblNuovoFilm.setHorizontalAlignment(SwingConstants.CENTER);
        				lblNuovoFilm.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
        				lblNuovoFilm.setBounds(10, 6, 484, 38);
        				fram.getContentPane().add(lblNuovoFilm);
        				
        				JLabel lblCodiceFilm = new JLabel("Codice Programmazione");
        				lblCodiceFilm.setHorizontalAlignment(SwingConstants.CENTER);
        				lblCodiceFilm.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblCodiceFilm.setBounds(10, 72, 185, 28);
        				fram.getContentPane().add(lblCodiceFilm);
        				
        				JLabel lblTitolo = new JLabel("Codice Film");
        				lblTitolo.setHorizontalAlignment(SwingConstants.CENTER);
        				lblTitolo.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblTitolo.setBounds(10, 120, 185, 28);
        				fram.getContentPane().add(lblTitolo);
        				
        				JLabel lblGenere = new JLabel("Titolo FIlm");
        				lblGenere.setHorizontalAlignment(SwingConstants.CENTER);
        				lblGenere.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblGenere.setBounds(10, 168, 185, 28);
        				fram.getContentPane().add(lblGenere);
        				
        				JLabel lblDurata = new JLabel("Sala");
        				lblDurata.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDurata.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDurata.setBounds(10, 216, 185, 28);
        				fram.getContentPane().add(lblDurata);
        				
        				JLabel lblDurata_1 = new JLabel("Data(dd/MM/yyyy)");
        				lblDurata_1.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDurata_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDurata_1.setBounds(10, 264, 185, 28);
        				fram.getContentPane().add(lblDurata_1);
        				
        				JLabel lblDescrizione = new JLabel("Ora(hh:mm)");
        				lblDescrizione.setHorizontalAlignment(SwingConstants.CENTER);
        				lblDescrizione.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblDescrizione.setBounds(10, 312, 185, 28);
        				fram.getContentPane().add(lblDescrizione);
        				
        				JTextField textField_0 = new JTextField();
        				textField_0.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
        				textField_0.setText(programmazioni[list_1.getSelectedIndex()].getCodice());
        				textField_0.setBounds(276, 72, 198, 27);
        				fram.getContentPane().add(textField_0);
        				textField_0.setColumns(10);
        				
        				JTextField textField_1 = new JTextField();
        				textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_1.setText(programmazioni[list_1.getSelectedIndex()].getCodiceFilm());
        				textField_1.setColumns(10);
        				textField_1.setBounds(276, 120, 198, 27);
        				fram.getContentPane().add(textField_1);
        				
        				JTextField textField_2 = new JTextField();
        				textField_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_2.setText(programmazioni[list_1.getSelectedIndex()].getNomeFilm());
        				textField_2.setColumns(10);
        				textField_2.setBounds(276, 168, 198, 27);
        				fram.getContentPane().add(textField_2);
        				
        				JTextField textField_3 = new JTextField();
        				textField_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_3.setText(programmazioni[list_1.getSelectedIndex()].getSala());
        				textField_3.setColumns(10);
        				textField_3.setBounds(276, 216, 198, 27);
        				fram.getContentPane().add(textField_3);
        				
        				JTextField textField_4 = new JTextField();
        				textField_4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_4.setText(programmazioni[list_1.getSelectedIndex()].getData());
        				textField_4.setColumns(10);
        				textField_4.setBounds(276, 264, 198, 27);
        				fram.getContentPane().add(textField_4);
        				
        				JTextField textField_5 = new JTextField();
        				textField_5.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_5.setText(programmazioni[list_1.getSelectedIndex()].getOra());
        				textField_5.setColumns(10);
        				textField_5.setBounds(276, 313, 198, 27);
        				fram.getContentPane().add(textField_5);
        				
        				JLabel lblPostiPrenotati = new JLabel("Posti Prenotati");
        				lblPostiPrenotati.setHorizontalAlignment(SwingConstants.CENTER);
        				lblPostiPrenotati.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				lblPostiPrenotati.setBounds(10, 360, 185, 28);
        				fram.getContentPane().add(lblPostiPrenotati);
        				
        				JTextField textField_6 = new JTextField();
        				textField_6.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				textField_6.setText(Integer.toString(programmazioni[list_1.getSelectedIndex()].getPostiPrenotati()));
        				textField_6.setColumns(10);
        				textField_6.setBounds(276, 360, 198, 27);
        				fram.getContentPane().add(textField_6);	
        				fram.setVisible(true);
        				
        				JButton btnNewButton = new JButton("Salva");
        				btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
        				btnNewButton.setBounds(193, 427, 109, 38);
        				fram.getContentPane().add(btnNewButton);				
        				btnNewButton.addActionListener(new ActionListener() {
        					public void actionPerformed(ActionEvent e) {
        					  int risposta=JOptionPane.showConfirmDialog(fram, "Vuoi procedere con il salvataggio delle modifiche?","Modifica",JOptionPane.YES_NO_OPTION);
  		                      if(risposta==JOptionPane.YES_OPTION){	
  								ClientResource cr;
  								Gson gson = new Gson();
  								Status status;
  								String URI, json;
  								
  								Programmazione modifica=programmazioni[list_1.getSelectedIndex()];

		                        Programmazione p=new Programmazione(textField_0.getText().toString(),textField_3.getText().toString(),textField_1.getText().toString(),textField_2.getText().toString(),textField_4.getText().toString(),textField_5.getText().toString(),Integer.parseInt(textField_6.getText().toString()));
								URI = "http://localhost:8182/prog/"+modifica.getCodice()+"&"+token;   

								cr = new ClientResource(URI);
								
								responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
								if (responseHeaders == null) { 
									responseHeaders = new Series<Header>(Header.class); 
									cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								}
								responseHeaders.set("User-Agent", "ClientJava");
								
								try {
								json = cr.put(gson.toJson(p,Programmazione.class)).getText();
								status = cr.getStatus();
								if(status.getCode()!=200){
									switch(status.getCode()){
										case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
											JOptionPane.showMessageDialog(fram, "Impossibile inserire l'elemento! \nVerificare che 'Codice film' e 'Codice sala' corrispondano rispettivamente a film e sale esistenti");
											break;
										case Costanti.ECCEZIONE_PROGRAMMAZIONE_DUPLICATA: 
											JOptionPane.showMessageDialog(fram, "Attenzione la programmazione con codice '"+p.getCodice()+"' già è presente nel database");
										 	break;
										case Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE: 
											JOptionPane.showMessageDialog(fram, "Impossibile modificare la programmazione nel database");
											break;
										default: 
											JOptionPane.showMessageDialog(fram, "Errore generico");
											break;
									}
								} 
								else {
									URI = "http://localhost:8182/prog/all/";
									cr = new ClientResource(URI);
									
									responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
									if (responseHeaders == null) { 
										responseHeaders = new Series<Header>(Header.class); 
										cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								    }
								    responseHeaders.set("User-Agent", "ClientJava");
								    
									json=cr.get().getText();
									programmazioni=gson.fromJson(json, Programmazione[].class);	
								}
						    
						        list_1.setListData(programmazioni);
						        fram.dispose();
								
							}
							catch(ResourceException | IOException e5){
							    e5.printStackTrace();
							}
  		                  }
        			    }
        			});	
                   }
				}
				else
					   JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona la programmazione da modificare");
			}
		});

		JButton btnNewButton_5 = new JButton("Elimina");
		btnNewButton_5.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_4.add(btnNewButton_5);
		btnNewButton_5.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!list_1.isSelectionEmpty()){
					int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi eliminare la programmazione: " + programmazioni[list_1.getSelectedIndex()].getCodice() + "?", "Elimina", JOptionPane.YES_NO_OPTION);
                    if(risposta==JOptionPane.YES_OPTION){	
                    	ClientResource cr;
						Gson gson = new Gson();
						Status status;
						String URI, json;
						
						Programmazione prog = programmazioni[list_1.getSelectedIndex()];
						URI = "http://localhost:8182/prog/"+prog.getCodice()+"&"+token;   
						cr = new ClientResource(URI);
						 
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
							cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						try {
						json = cr.delete().getText();
						status = cr.getStatus();
						if(status.getCode()!=200){
							switch(status.getCode()){
								case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
									break;
								case Costanti.ECCEZIONE_PROGRAMMAZIONE_INESISTENTE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "programmazione non presente  nel database");
									break;
								default: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
									break;
							}
						} 
						else {
							URI = "http://localhost:8182/prog/all/";
							cr = new ClientResource(URI);

							responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
							if (responseHeaders == null) { 
								responseHeaders = new Series<Header>(Header.class); 
								cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
							}
							responseHeaders.set("User-Agent", "ClientJava");
							
							json=cr.get().getText();
							programmazioni=gson.fromJson(json, Programmazione[].class);
						}
				    
						list_1.setListData(programmazioni);
						
				     }
				     catch(ResourceException | IOException e5){
				    	e5.printStackTrace();
				     }
				  }
                    
				}
				else{
					JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona la programmazione da eliminare");
				}
                       
			}
		});
		
		JScrollPane scrollPane_1 = new JScrollPane();
		panel_1.add(scrollPane_1, BorderLayout.CENTER);
		
		list_1 = new JList();
		list_1.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_1.setListData(programmazioni);
		scrollPane_1.setViewportView(list_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("SALE", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_5 = new JPanel();
		panel_2.add(panel_5, BorderLayout.SOUTH);
		
		JButton btnNewButton_6 = new JButton("Aggiungi");
		btnNewButton_6.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_5.add(btnNewButton_6);
		btnNewButton_6.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {

				JFrame frmAggiungi=new JFrame();
				frmAggiungi.setTitle("Aggiungi");
				frmAggiungi.getContentPane().setLayout(null);
				frmAggiungi.setBounds(100, 100, 450, 280);
				
				JLabel lblNewLabel = new JLabel("Nuova Sala");
				lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
				lblNewLabel.setBounds(6, 11, 438, 30);
				frmAggiungi.getContentPane().add(lblNewLabel);
				
				JLabel lblNewLabel_1 = new JLabel("Nome Sala");
				lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
				lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblNewLabel_1.setBounds(50, 80, 109, 30);
				frmAggiungi.getContentPane().add(lblNewLabel_1);
				
				JLabel lblPostiDidponibili = new JLabel("Posti Totali");
				lblPostiDidponibili.setHorizontalAlignment(SwingConstants.CENTER);
				lblPostiDidponibili.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				lblPostiDidponibili.setBounds(50, 135, 109, 30);
				frmAggiungi.getContentPane().add(lblPostiDidponibili);
				
				JTextField textField = new JTextField();
				textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField.setBounds(220, 80, 200, 30);
				frmAggiungi.getContentPane().add(textField);
				textField.setColumns(10);
				
				JTextField textField_1 = new JTextField();
				textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				textField_1.setColumns(10);
				textField_1.setBounds(220, 135, 200, 30);
				frmAggiungi.getContentPane().add(textField_1);
				
				JButton btnNewButton = new JButton("Aggiungi");
				btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
				btnNewButton.setBounds(173, 204, 109, 25);
				frmAggiungi.getContentPane().add(btnNewButton);
				frmAggiungi.setVisible(true);
				btnNewButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent arg0) {
					if(!textField.getText().toString().isEmpty() && !textField_1.getText().toString().isEmpty()){	
					  int risposta=JOptionPane.showConfirmDialog(frmAggiungi, "Vuoi procedere con il salvataggio?","AGGIUNGI",JOptionPane.YES_NO_OPTION);
                      if(risposta==JOptionPane.YES_OPTION){	
						ClientResource cr;
						Gson gson = new Gson();
						Status status;
						String URI, json;
						
                        Sala p=new Sala(textField.getText(),Integer.parseInt(textField_1.getText()));
						URI = "http://localhost:8182/sale/"+p.getNome()+"&"+token;   
						cr = new ClientResource(URI);
						
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
							cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
                    try {
						json = cr.post(gson.toJson(p,Sala.class)).getText();
						status = cr.getStatus();
						
						if(status.getCode()!=200){
							switch(status.getCode()){
								case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
									JOptionPane.showMessageDialog(frmAggiungi, "Impossibile collegarsi al database");
									break;
								case Costanti.ECCEZIONE_SALA_DUPLICATA: 
									JOptionPane.showMessageDialog(frmAggiungi, "Attenzione la sala '"+p.getNome()+"' gi� � presente nel database");
								 	break;
								case Costanti.ECCEZIONE_SALA_INESISTENTE: 
									JOptionPane.showMessageDialog(frmAggiungi, "Impossibile inserire la sala nel database");
									break;
								default: 
									JOptionPane.showMessageDialog(frmAggiungi, "Errore generico");
									break;
							}
						} 
						else {
							URI = "http://localhost:8182/sale/all/";
							
							cr = new ClientResource(URI);
							responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
							if (responseHeaders == null) { 
								responseHeaders = new Series<Header>(Header.class); 
								cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
							}
							responseHeaders.set("User-agent", "ClientJava");
							
							json=cr.get().getText();
							sale=gson.fromJson(json, Sala[].class);	
						}
				    
				        list_2.setListData(sale);    
				        frmAggiungi.dispose();
				        
				   }
				   catch(ResourceException | IOException e5){
				    	 e5.printStackTrace();
				   }
				 }
				}  
				else
					JOptionPane.showMessageDialog(frmAggiungi, "Completare tutti i campi");
				
			  }
		   });
				
		  }	
		});
		
		JButton btnNewButton_7 = new JButton("Modifica");
		btnNewButton_7.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_5.add(btnNewButton_7);
		btnNewButton_7.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				if(!list_2.isSelectionEmpty()){
					int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi modificare la sala: "+ sale[list_2.getSelectedIndex()].getNome() + "?", "Modifica", JOptionPane.YES_NO_OPTION);
                    if(risposta==JOptionPane.YES_OPTION){
                    	JFrame frmAggiungi=new JFrame();
                		frmAggiungi.setTitle("Modifica");
                		frmAggiungi.getContentPane().setLayout(null);
                		frmAggiungi.setBounds(100, 100, 450, 280);
                		
                		JLabel lblNewLabel = new JLabel("Nuova Sala");
                		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
                		lblNewLabel.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
                		lblNewLabel.setBounds(6, 11, 438, 30);
                		frmAggiungi.getContentPane().add(lblNewLabel);
                		
                		JLabel lblNewLabel_1 = new JLabel("Nome Sala");
                		lblNewLabel_1.setHorizontalAlignment(SwingConstants.CENTER);
                		lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                		lblNewLabel_1.setBounds(50, 80, 109, 30);
                		frmAggiungi.getContentPane().add(lblNewLabel_1);
                		
                		JLabel lblPostiDidponibili = new JLabel("Posti Totali");
                		lblPostiDidponibili.setHorizontalAlignment(SwingConstants.CENTER);
                		lblPostiDidponibili.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                		lblPostiDidponibili.setBounds(50, 135, 109, 30);
                		frmAggiungi.getContentPane().add(lblPostiDidponibili);
                		
                		JTextField textField = new JTextField();
                		textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                		textField.setBounds(220, 80, 200, 30);
        				textField.setText(sale[list_2.getSelectedIndex()].getNome());
                		frmAggiungi.getContentPane().add(textField);
                		textField.setColumns(10);
                		
                		JTextField textField_1 = new JTextField();
                		textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                		textField_1.setColumns(10);
                		textField_1.setBounds(220, 135, 200, 30);
        				textField_1.setText(Integer.toString(sale[list_2.getSelectedIndex()].getPosti()));
                		frmAggiungi.getContentPane().add(textField_1);
                		frmAggiungi.setVisible(true);
                		
                		JButton btnNewButton = new JButton("Salva");
                		btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
                		btnNewButton.setBounds(173, 204, 109, 25);
                		frmAggiungi.getContentPane().add(btnNewButton);
                		btnNewButton.addActionListener(new ActionListener(){
							public void actionPerformed(ActionEvent arg0) {
							 if(!textField.getText().toString().isEmpty() && !textField_1.getText().toString().isEmpty()){	
							  int risposta=JOptionPane.showConfirmDialog(frmAggiungi, "Vuoi procedere con  il salvataggio delle modifiche ?","Modifica",JOptionPane.YES_NO_OPTION);
		                      if(risposta==JOptionPane.YES_OPTION){	
								ClientResource cr;
								Gson gson = new Gson();
								Status status;
								String URI, json;
	
								Sala salaDaMofidi=sale[list_2.getSelectedIndex()];
								URI = "http://localhost:8182/sale/"+salaDaMofidi.getNome()+"&"+token;    
								cr = new ClientResource(URI);
								
								responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
								if (responseHeaders == null) { 
									responseHeaders = new Series<Header>(Header.class); 
									cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								}
								responseHeaders.set("User-Agent", "ClientJava");
	                            
								Sala p = new Sala(textField.getText(),Integer.parseInt(textField_1.getText()));
						        try {
								json = cr.put(gson.toJson(p,Sala.class)).getText();
								status = cr.getStatus();
								
								if(status.getCode()!=200){
									switch(status.getCode()){
										case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
											JOptionPane.showMessageDialog(frmAggiungi, "Impossibile collegarsi al database");
											break;
										case Costanti.ECCEZIONE_SALA_DUPLICATA: 
											JOptionPane.showMessageDialog(frmAggiungi, "Attenzione la sala '"+p.getNome()+"' gi� � presente nel database");
										 	break;
										case Costanti.ECCEZIONE_SALA_INESISTENTE: 
											JOptionPane.showMessageDialog(frmAggiungi, "Impossibile inserire la sala nel database");
											break;
										default: 
											JOptionPane.showMessageDialog(frmAggiungi, "Errore generico");
											break;
									}
								} 
								else {
									URI = "http://localhost:8182/sale/all/";
									cr = new ClientResource(URI);
									
									responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
									if (responseHeaders == null) { 
										responseHeaders = new Series<Header>(Header.class); 
										cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
								    }
									responseHeaders.set("User-Agent", "ClientJava");
									
									json=cr.get().getText();
									sale=gson.fromJson(json, Sala[].class);	
								}
						    
						            list_2.setListData(sale);
						            
						            URI = "http://localhost:8382/prog/all/";
									cr = new ClientResource(URI);
									
									responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
									if (responseHeaders == null) { 
										responseHeaders = new Series<Header>(Header.class); 
										cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
									}
									responseHeaders.set("User-Agent", "ClientJava");
									
									json=cr.get().getText();
									programmazioni=gson.fromJson(json, Programmazione[].class);
									
						            list_1.setListData(programmazioni);
					                frmAggiungi.dispose();
						   }
						    catch(ResourceException | IOException e5){
						    	 e5.printStackTrace();
						     }
						 }
					  }
					}
					});
				  }
				}
				else
					JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona la sala da modificare");
			}
		});
                
				
		
		JButton btnNewButton_8 = new JButton("Elimina");
		btnNewButton_8.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_5.add(btnNewButton_8);
		btnNewButton_8.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(!list_2.isSelectionEmpty()){
					 int risposta=JOptionPane.showConfirmDialog(GUIGestore.this, "Vuoi eliminare la sala: " + sale[list_2.getSelectedIndex()].getNome() + "?" 
				                       + "\nAttenzione: Le Programmazioni associate a questa sala verranno eliminate ", "Elimina", JOptionPane.YES_NO_OPTION);
                     if(risposta==JOptionPane.YES_OPTION){	
						ClientResource cr;
						Gson gson = new Gson();
						Status status;
						String URI;
						String json = null ;
							
						Sala sal = sale[list_2.getSelectedIndex()];
						URI = "http://localhost:8182/sale/"+sal.getNome()+"&"+token;   
						cr = new ClientResource(URI);
						
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
							cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						try {
						json = cr.delete().getText();
						status = cr.getStatus();
						if(status.getCode()!=200){
							switch(status.getCode()){
								case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Impossibile collegarsi al database");
									break;
								case Costanti.ECCEZIONE_SALA_INESISTENTE: 
									JOptionPane.showMessageDialog(GUIGestore.this, "sala non presente  nel database");
									break;
								default: 
									JOptionPane.showMessageDialog(GUIGestore.this, "Errore generico");
									break;
							}
						} 
						else {
							URI = "http://localhost:8182/sale/all/";
							cr = new ClientResource(URI);
							
							responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
							if (responseHeaders == null) { 
								responseHeaders = new Series<Header>(Header.class); 
								cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
							}
							responseHeaders.set("User-Agent", "ClientJava");
							
							json=cr.get().getText();
							sale=gson.fromJson(json, Sala[].class);	
						}
				
				        list_2.setListData(sale);
				            
				        URI = "http://localhost:8182/prog/all/";
						cr = new ClientResource(URI);
							 
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
						    responseHeaders = new Series<Header>(Header.class); 
						    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						json=cr.get().getText();
						programmazioni=gson.fromJson(json, Programmazione[].class);
							
				        list_1.setListData(programmazioni);
				            
				   }
				     catch(ResourceException | IOException e5){
				    	 e5.printStackTrace();
				     }
					}
				}
				else{
					JOptionPane.showMessageDialog(GUIGestore.this, "Seleziona la sala da eliminare");
				}
			}
		});
		
		JScrollPane scrollPane_2 = new JScrollPane();
		panel_2.add(scrollPane_2, BorderLayout.CENTER);
		
		list_2 = new JList();
		list_2.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list_2.setListData(sale);
		scrollPane_2.setViewportView(list_2);
		
		this.setVisible(true);

	}

}
