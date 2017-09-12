package com.veosaf.bookRental.security;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.veosaf.bookRental.dto.UserDto;
import com.veosaf.bookRental.services.UserService;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Inject
	private UserService userService;
	
	public static final String AUTH_SECRET = "test_secret";

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
		String password = (String) authentication.getCredentials();
		if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
			throw new BadCredentialsException("Bad Credentials");
		}
		UserDto user = userService.getByCredentials(username, password);
		if (user == null) {
			throw new BadCredentialsException("User not found");
		}
		Algorithm algorithm;
		try {
			algorithm = Algorithm.HMAC256(AUTH_SECRET);
		} catch (IllegalArgumentException | UnsupportedEncodingException e) {
			throw new TokenCreationException("Unable to create token for authentication");
		}
	    String token = JWT.create()
	    		.withClaim("id", user.getId())
	    		.withClaim("firstName", user.getFirstName())
	    		.withClaim("lastName", user.getLastName())
	    		.withClaim("email", user.getEmail())
	        .withIssuer("auth0")
	        .sign(algorithm);
		return new UsernamePasswordAuthenticationToken(token, password, null);
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return true;
	}
}
