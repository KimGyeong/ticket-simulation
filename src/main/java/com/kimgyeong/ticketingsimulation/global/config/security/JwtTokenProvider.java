package com.kimgyeong.ticketingsimulation.global.config.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtTokenProvider {
	private final SecretKey key;

	public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
		this.key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
	}

	public String generateToken(Long userId, String email, List<String> roles) {
		return Jwts.builder()
			.subject(email)
			.claim("userId", userId)
			.claim(SecurityConstants.CLAIM_ROLES, roles)
			.issuedAt(new Date())
			.expiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
			.signWith(key)
			.compact();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parser()
				.verifyWith(key)
				.build()
				.parseSignedClaims(token);
			return true;
		} catch (JwtException e) {
			return false;
		}
	}

	public String getEmail(String token) {
		return getClaims(token).getSubject();
	}

	public Long getUserId(String token) {
		Object claim = getClaims(token).get("userId");

		if (claim instanceof Integer i) {
			return i.longValue();
		}

		return Long.valueOf(claim.toString());
	}

	public List<String> getRoles(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload()
			.get(SecurityConstants.CLAIM_ROLES, List.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parser()
			.verifyWith(key)
			.build()
			.parseSignedClaims(token)
			.getPayload();
	}
}
