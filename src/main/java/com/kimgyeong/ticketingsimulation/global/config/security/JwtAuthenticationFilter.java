package com.kimgyeong.ticketingsimulation.global.config.security;

import java.io.IOException;
import java.util.Collections;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {
	private final JwtTokenProvider jwtTokenProvider;

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws
		IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest)servletRequest;
		String header = httpRequest.getHeader(SecurityConstants.TOKEN_HEADER);

		if (header != null && header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			String token = header.substring(SecurityConstants.TOKEN_PREFIX.length());
			if (jwtTokenProvider.validateToken(token)) {
				String subject = jwtTokenProvider.getSubject(token);
				User principal = new User(subject, "", Collections.emptyList());
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					principal, token, principal.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
