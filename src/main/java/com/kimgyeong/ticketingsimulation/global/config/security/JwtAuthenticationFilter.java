package com.kimgyeong.ticketingsimulation.global.config.security;

import java.io.IOException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.kimgyeong.ticketingsimulation.global.auth.CustomUserDetails;

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
				String email = jwtTokenProvider.getEmail(token);
				Long userId = jwtTokenProvider.getUserId(token);
				List<String> roles = jwtTokenProvider.getRoles(token);

				List<SimpleGrantedAuthority> authorities = roles.stream()
					.map(SimpleGrantedAuthority::new)
					.toList();

				CustomUserDetails userDetails = new CustomUserDetails(userId, email, authorities);
				UsernamePasswordAuthenticationToken authentication =
					new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());

				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
		}

		filterChain.doFilter(servletRequest, servletResponse);
	}
}
