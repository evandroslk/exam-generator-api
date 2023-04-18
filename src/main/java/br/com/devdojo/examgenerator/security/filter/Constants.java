package br.com.devdojo.examgenerator.security.filter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Constants {

	public static final String SECRET = "secret";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final long EXPIRATION_TIME = 86400000L; // 1 day

	public static void main(String[] args) {
		
		System.out.println(new BCryptPasswordEncoder().encode("devdojo"));
		
	}
	
}

