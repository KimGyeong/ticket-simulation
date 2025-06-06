package com.kimgyeong.ticketingsimulation.global.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kimgyeong.ticketingsimulation.global.config.security.JwtTokenProvider;
import com.kimgyeong.ticketingsimulation.global.config.security.SecurityConfig;

@Import({JwtTokenProvider.class, SecurityConfig.class})
@WebMvcTest()
public abstract class AbstractControllerTest {
	protected MockMvc mockMvc;

	protected ObjectMapper objectMapper;

	@BeforeEach
	void setUp(WebApplicationContext webApplicationContext) {
		objectMapper = new ObjectMapper();
		this.mockMvc = MockMvcBuilders
			.webAppContextSetup(webApplicationContext)
			.addFilters(new CharacterEncodingFilter("UTF-8", true))
			.apply(springSecurity())
			.alwaysDo(print())
			.build();
	}
}
