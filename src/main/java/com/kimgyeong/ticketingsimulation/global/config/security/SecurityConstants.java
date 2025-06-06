package com.kimgyeong.ticketingsimulation.global.config.security;

public class SecurityConstants {
	public static final String TOKEN_HEADER = "Authorization";
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String CLAIM_ROLES = "roles";
	public static final long EXPIRATION_TIME = 1000 * 60 * 60;
}
