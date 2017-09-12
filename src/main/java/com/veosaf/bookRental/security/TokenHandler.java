package com.veosaf.bookRental.security;

import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.veosaf.bookRental.dto.UserDto;

@Service
public final class TokenHandler {

	public UserDto parseFromToken(String token) {
		if (StringUtils.isNotEmpty(token)) {
			DecodedJWT jwt = null;
			try {
			    jwt = JWT.decode(token);
			} catch (JWTDecodeException exception){
			    //Invalid token
			}
			Map<String, Claim> claims = jwt.getClaims(); 
			UserDto user = new UserDto();
			user.setEmail(claims.get("email").asString());
			user.setFirstName(claims.get("firstName").asString());
			user.setLastName(claims.get("lastName").asString());
			user.setId(claims.get("id").asLong());
			return user;
		}
		return null;
	}

}
