package com.veosaf.bookRental.resources.impl;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;
import com.veosaf.bookRental.dto.UserDto;
import com.veosaf.bookRental.resources.AuthenticationResource;
import com.veosaf.bookRental.security.TokenHandler;


@Component
public class AuthenticationResourceImpl implements AuthenticationResource {

	@Inject
	private AuthenticationManager authenticationManager;

	@Context
	private HttpServletRequest request;
	
	@Inject
	private TokenHandler tokenHandler;
	


	@Override
	public Response login(String login, String password) {
		Authentication auth = new UsernamePasswordAuthenticationToken(login, password);
		auth = authenticationManager.authenticate(auth);
		if (auth == null) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		SecurityContextHolder.getContext().setAuthentication(auth);
		return Response.ok((String) auth.getPrincipal()).build();

	}

	@Override
	public Response logout() {
		try {
			request.logout();
		} catch (ServletException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(null).build();
	}

	@Override
	public Response getCurrentUser() {
		if (request.getUserPrincipal() instanceof UsernamePasswordAuthenticationToken) {
			UsernamePasswordAuthenticationToken principal = (UsernamePasswordAuthenticationToken) request
					.getUserPrincipal();
			if (principal != null && principal.getPrincipal() != null) {
				return Response.ok(tokenHandler.parseFromToken((String) principal.getPrincipal())).build();
			} else {
				Response.status(Response.Status.UNAUTHORIZED).entity("USER unauthorized").build();
			}
		}

		return null;
	}

	@Override
	public Response authorize() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		Object principal = auth.getPrincipal();
		return Response.ok(principal).build();
	}

}
