package com.kimgyeong.ticketingsimulation.global.security.factory;

import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;

public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

	@Override
	public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
		long id = customUser.id();
		String email = customUser.email();
		String role = customUser.role();

		CustomUserDetails principal = new CustomUserDetails(id, email, List.of(new SimpleGrantedAuthority(role)));

		UsernamePasswordAuthenticationToken authentication =
			new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());

		SecurityContext context = SecurityContextHolder.createEmptyContext();
		context.setAuthentication(authentication);
		return context;
	}
}