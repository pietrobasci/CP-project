package com.unisannio.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JOptionPane;
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
import com.unisannio.model.Utente;


@SuppressWarnings("serial")
public class GUIReg extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JPasswordField passwordField;
	private JPasswordField passwordField_1;

	/**
	 * Create the frame.
	 */
	public GUIReg() {
		setTitle("Registrazione");
		setBounds(100, 100, 450, 530);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		JLabel label = new JLabel("Nome");
		label.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label.setBounds(72, 39, 34, 17);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		textField.setColumns(15);
		textField.setBounds(224, 35, 190, 26);
		contentPane.add(textField);
		
		JLabel label_1 = new JLabel("Cognome");
		label_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label_1.setBounds(61, 105, 55, 17);
		contentPane.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		textField_1.setColumns(15);
		textField_1.setBounds(224, 101, 190, 26);
		contentPane.add(textField_1);
		
		JLabel label_2 = new JLabel("Email");
		label_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label_2.setBounds(73, 175, 32, 17);
		contentPane.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		textField_2.setColumns(15);
		textField_2.setBounds(224, 171, 190, 26);
		contentPane.add(textField_2);
		
		JLabel label_3 = new JLabel("Username");
		label_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label_3.setBounds(59, 246, 59, 17);
		contentPane.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		textField_3.setColumns(15);
		textField_3.setBounds(224, 242, 190, 26);
		contentPane.add(textField_3);
		
		JLabel label_4 = new JLabel("Password");
		label_4.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label_4.setBounds(60, 316, 58, 17);
		contentPane.add(label_4);
		
		passwordField = new JPasswordField();
		passwordField.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		passwordField.setColumns(15);
		passwordField.setBounds(224, 312, 190, 26);
		contentPane.add(passwordField);
		
		JLabel label_5 = new JLabel("Conferma password");
		label_5.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		label_5.setBounds(30, 387, 118, 17);
		contentPane.add(label_5);
		
		passwordField_1 = new JPasswordField();
		passwordField_1.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		passwordField_1.setColumns(15);
		passwordField_1.setBounds(224, 383, 190, 26);
		contentPane.add(passwordField_1);
		
		JButton button = new JButton("Salva");
		button.setFont(new Font("Helvetica Neue", Font.PLAIN, 13));
		button.setBounds(189, 451, 76, 29);
		contentPane.add(button);
		this.setVisible(true);
		
		button.addActionListener(new ActionListener(){

			@SuppressWarnings({ "deprecation", "unchecked" })
			@Override
			public void actionPerformed(ActionEvent e) {
				if(!textField.getText().isEmpty()&&!textField_1.getText().isEmpty()&&!textField_2.getText().isEmpty()&&!passwordField.getText().isEmpty() && !passwordField_1.getText().isEmpty()&&!textField_3.getText().isEmpty()){
             	   
					if((passwordField.getText().equalsIgnoreCase(passwordField_1.getText())) && textField_2.getText().contains("@") ){
             		   
             		   ClientResource cr;
					   Gson gson = new Gson();
					   Status status;
					   String json = null ;
					   
					   Utente p =new Utente(true,textField_3.getText(),passwordField.getText(),textField.getText(),textField_1.getText(),textField_3.getText());
					   String uri = "http://localhost:8182/login/"+p.getUsername()+"&"+p.getPassword();
					   cr = new ClientResource(uri);

					   Series<Header> responseHeaders = (Series<Header>) cr.getRequestAttributes().get("org.restlet.http.headers"); 
					   if (responseHeaders == null) { 
						   responseHeaders = new Series<Header>(Header.class); 
						   cr.getRequestAttributes().put( "org.restlet.http.headers",responseHeaders); 
						   }
					   responseHeaders.set("User-Agent", "ClientJava");
					   
					   try {
						   json = cr.post(gson.toJson(p,Utente.class)).getText();
						   status = cr.getStatus();
						   if (status.getCode() != 200) {
							   switch(status.getCode()){
							       case Costanti.ECCEZIONE_COLLEGAMENTO_DATABASE: 
								   JOptionPane.showMessageDialog(GUIReg.this, "Impossibile collegarsi al database");
								   GUIReg.this.dispose();
								   break;
								   case Costanti.ECCEZIONE_AmministratoreEsistente: 
								   JOptionPane.showMessageDialog(GUIReg.this, "L'amministratore è stato già registrato nel database");
								   GUIReg.this.dispose();
								   break;
								   default: 
								   JOptionPane.showMessageDialog(GUIReg.this, "Errore generico");
								   GUIReg.this.dispose();
								   break;
								   }
						   }
						   else {
								Utente user = gson.fromJson(json, Utente.class);
								JOptionPane.showMessageDialog(GUIReg.this, "L'utente '"+user.getUsername()+"' è stato aggiunto correttamente");
								GUIReg.this.dispose();
								}
						}
						catch (ResourceException | IOException e1){
							e1.printStackTrace();
						}
	                   
             	   }
             	   else {
             		   if(!(passwordField.getText().equalsIgnoreCase(passwordField_1.getText())) && !textField_2.getText().contains("@") ){
            				JOptionPane.showMessageDialog(GUIReg.this, "Le due password non corrispondo!" + "\nEmail non corretta");			
             		   }
             	        else if(!(passwordField.getText().equalsIgnoreCase(passwordField_1.getText()))){
              				JOptionPane.showMessageDialog(GUIReg.this, "Le due password non corrispondo!");			
                	        }
                	        else if(!textField_2.getText().contains("@")){
             				JOptionPane.showMessageDialog(GUIReg.this, "Email non corretta!");			
                	        }
             	       }   
             	
                }
                else{
    				JOptionPane.showMessageDialog(GUIReg.this, "Completare tutti i campi");			
                }
				
			}				
			
		});
		
		
	}
}
