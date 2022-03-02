package com.unisannio.gui;
import java.awt.EventQueue;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;

import org.restlet.data.Status;
import org.restlet.engine.header.Header;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;

import com.google.gson.Gson;
import com.unisannio.model.Costanti;


public class GUIAccesso {

	private JFrame frmAccedi;
	private JTextField textField;
	private JPasswordField passwordField;
	private Series<Header> responseHeaders;


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new GUIAccesso();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIAccesso() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmAccedi = new JFrame();
		frmAccedi.setResizable(false);
		frmAccedi.setTitle("Accedi");
		frmAccedi.setBounds(100, 100, 450, 300);
		frmAccedi.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmAccedi.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JLabel lblNewLabel = new JLabel("Accedi come Amministratore");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Helvetica Neue", Font.ITALIC, 30));
		frmAccedi.getContentPane().add(lblNewLabel, BorderLayout.NORTH);
		
		JPanel panel = new JPanel();
		frmAccedi.getContentPane().add(panel, BorderLayout.SOUTH);
		
		JLabel lblNewLabel_1 = new JLabel("Non sei ancora registrato?");
		lblNewLabel_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("Registrati");
		btnNewButton.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel.add(btnNewButton);
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		       new GUIReg();
		    } 
		});
		
		JPanel panel_1 = new JPanel();
		panel_1.setLayout(null);
		frmAccedi.getContentPane().add(panel_1, BorderLayout.CENTER);
		
		JLabel lblUsername = new JLabel("Username");
		lblUsername.setBounds(108, 37, 59, 17);
		lblUsername.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_1.add(lblUsername);
		
		textField = new JTextField();
		textField.setBounds(197, 32, 120, 27);
		textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		textField.setHorizontalAlignment(SwingConstants.LEFT);
		textField.setColumns(10);
		panel_1.add(textField);
		
		JLabel lblPassword = new JLabel("Password");
		lblPassword.setBounds(108, 96, 59, 17);
		lblPassword.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		panel_1.add(lblPassword);
		
		passwordField = new JPasswordField();
		passwordField.setBounds(197, 91, 120, 27);
		passwordField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		passwordField.setHorizontalAlignment(SwingConstants.LEFT);
		passwordField.setColumns(10);
		panel_1.add(passwordField);
		
		JButton btnLogin = new JButton("Login");
		btnLogin.setBounds(186, 136, 79, 29);
		panel_1.add(btnLogin);

		btnLogin.addActionListener(new ActionListener() {
		     @SuppressWarnings({ "deprecation", "unchecked" })
			 public void actionPerformed(ActionEvent e) {
		    	 if(!textField.getText().isEmpty() && !passwordField.getText().isEmpty()){
					try {
						String user = textField.getText();
						String pass = passwordField.getText();
						String uri = "http://localhost:8182/login/" + user + "&" + pass;
						ClientResource cr = new ClientResource(uri);
						Gson gson = new Gson();
						
						responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
						if (responseHeaders == null) { 
							responseHeaders = new Series<Header>(Header.class); 
						    cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						}
						responseHeaders.set("User-Agent", "ClientJava");
						
						String json = cr.get().getText();
						Status stato = cr.getStatus();
						if(stato.getCode() != 200){
							switch(stato.getCode()){
								case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
									JOptionPane.showMessageDialog(frmAccedi, "Impossibile collegarsi al database");
									break;
								case Costanti.ECCEZIONE_PASSWORD_ERRATA: 
									JOptionPane.showMessageDialog(frmAccedi, "Attenzione password errata");
									break;
								case Costanti.ECCEZIONE_UTENTE_INESISTENTE: 
									JOptionPane.showMessageDialog(frmAccedi, "L'utente non è presente nel database");
									break;
								default: 
									JOptionPane.showMessageDialog(frmAccedi, "Errore generico");
									break;
							}
						}
						else{
							String admin [] = gson.fromJson(json, String[].class);
							if(admin[0].equalsIgnoreCase("admin")){
								JOptionPane.showMessageDialog(frmAccedi, "Benvenuto " + user + "!");
								frmAccedi.dispose();
								new GUIGestore(user, admin[1]);	
							}
							else
								JOptionPane.showMessageDialog(frmAccedi, "Non sei registrato come admin");
						}
						
					} 
					catch (ResourceException |IOException ex) {
							ex.printStackTrace();
					} 
						
				} 
		    	else{
		    		JOptionPane.showMessageDialog(frmAccedi, "Completare entrambi i campi");			
		    	}
		    } 
		  });
		
		frmAccedi.setVisible(true);

	}

}
