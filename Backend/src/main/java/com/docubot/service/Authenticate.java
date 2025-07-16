package com.docubot.service;

import org.springframework.stereotype.Service;

@Service
public class Authenticate {
	
	public boolean authlogin(String email, String password) {
		//verify login
		return true;
	}
	
	public String generatetoken(String email, String password) {
		//generate token
		return "token";
	}

	public String encrypt(String password) {
		// Password Encryption
		return "Encrypted password";
	}

}
