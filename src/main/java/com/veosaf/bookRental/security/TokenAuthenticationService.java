package com.veosaf.bookRental.security;

import java.io.UnsupportedEncodingException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.veosaf.bookRental.dto.UserDto;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "Authorization";


	public Authentication getAuthentication(HttpServletRequest request) {
		

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			return authentication;
		}
		
		final String token = request.getHeader(AUTH_HEADER_NAME);
		
		if (token == null) {
			return authentication;
		}
		
		try {
		    Algorithm algorithm = Algorithm.HMAC256(CustomAuthenticationProvider.AUTH_SECRET);
		    JWTVerifier verifier = JWT.require(algorithm)
		        .withIssuer("auth0")
		        .build(); //Reusable verifier instance
		    DecodedJWT jwt = verifier.verify(token);
		} catch (UnsupportedEncodingException exception){
		    //UTF-8 encoding not supported
		} catch (JWTVerificationException exception){
		    //Invalid signature/claims
		}
		authentication = new UsernamePasswordAuthenticationToken(token, "n/a", null);

		return authentication;
	}
}