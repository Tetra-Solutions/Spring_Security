package com.spring.security.util;

import static com.spring.security.util.SecurityConstant.AUTHORITIES;
import static com.spring.security.util.SecurityConstant.EXPIRATION_TIME;
import static com.spring.security.util.SecurityConstant.GET_ARRAYS_ADMINISTRATION;
import static com.spring.security.util.SecurityConstant.GET_ARRAYS_LLC;
import static com.spring.security.util.SecurityConstant.TOKEN_CANNOT_BE_VERIFIED;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.spring.security.UserCustody;

@Component
public class JWTTokenProvider {

	@Value("jwt.secret")
	private String secret;

	public String generateJwtToken(UserCustody userCustody) {

		String[] claims = getClaimsFromUser(userCustody);

		return JWT.create().withIssuer(GET_ARRAYS_LLC).withAudience(GET_ARRAYS_ADMINISTRATION).withIssuedAt(new Date())
				.withSubject(userCustody.getUsername()).withArrayClaim(AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities,
			HttpServletRequest httpRequest) {

		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, null, authorities);
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));

		return usernamePasswordAuthenticationToken;
	}

	public List<GrantedAuthority> getAuthorities(String token) {

		String[] claims = getClaimsFromToken(token);
		return Arrays.stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	public boolean isTokenValid(String username, String token) {

		JWTVerifier jwtVerifier = getJWTVerifier();

		return StringUtils.isNotEmpty(username) && !isTokenExpired(jwtVerifier, token);
	}

	public String getSubject(String token) {

		JWTVerifier jwtVerifier = getJWTVerifier();

		return jwtVerifier.verify(token).getSubject();
	}

	private boolean isTokenExpired(JWTVerifier jwtVerifier, String token) {

		Date expiration = jwtVerifier.verify(token).getExpiresAt();

		return expiration.before(new Date());
	}

	private String[] getClaimsFromToken(String token) {

		JWTVerifier jwtVerifier = getJWTVerifier();

		return jwtVerifier.verify(token).getClaim(AUTHORITIES).asArray(String.class);
	}

	private JWTVerifier getJWTVerifier() {

		JWTVerifier jwtVerifier;
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			jwtVerifier = JWT.require(algorithm).withIssuer(GET_ARRAYS_LLC).build();
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException(TOKEN_CANNOT_BE_VERIFIED);
		}
		return jwtVerifier;
	}

	private String[] getClaimsFromUser(UserCustody userCustody) {

		List<String> authorities = new ArrayList<>();

		for (GrantedAuthority grantedAuthority : userCustody.getAuthorities()) {
			authorities.add(grantedAuthority.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}

}
