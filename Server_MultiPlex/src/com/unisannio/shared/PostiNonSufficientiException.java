package com.unisannio.shared;

@SuppressWarnings("serial")
public class PostiNonSufficientiException extends Exception {
	
	public PostiNonSufficientiException(){
		super("Posti in sala insufficienti");
	}
}
