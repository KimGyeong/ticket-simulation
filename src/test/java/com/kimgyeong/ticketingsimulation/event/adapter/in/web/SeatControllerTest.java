package com.kimgyeong.ticketingsimulation.event.adapter.in.web;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import com.kimgyeong.ticketingsimulation.event.application.port.in.HoldSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.application.port.in.ReadSeatUseCase;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;
import com.kimgyeong.ticketingsimulation.global.controller.AbstractControllerTest;
import com.kimgyeong.ticketingsimulation.global.exception.SeatAlreadyBookedException;
import com.kimgyeong.ticketingsimulation.global.security.annotation.WithMockCustomUser;

@WebMvcTest(controllers = SeatController.class)
class SeatControllerTest extends AbstractControllerTest {
	@MockitoBean
	ReadSeatUseCase readSeatUseCase;

	@MockitoBean
	HoldSeatUseCase holdSeatUseCase;

	@Test
	@WithMockCustomUser
	void getSeatsByEventId() throws Exception {
		Long eventId = 1L;
		Seat seat = new Seat(1L, 1L, SeatStatus.AVAILABLE, 1, null, null);

		when(readSeatUseCase.getSeatsByEventId(eventId)).thenReturn(List.of(seat));

		mockMvc.perform(get("/api/seats")
				.param("event-id", String.valueOf(eventId))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.seatResponses").isArray())
			.andExpect(jsonPath("$.seatResponses.length()").value(1))
			.andExpect(jsonPath("$.seatResponses[0].id").value(1L))
			.andExpect(jsonPath("$.seatResponses[0].number").value(1))
			.andExpect(jsonPath("$.seatResponses[0].status").value("AVAILABLE"));
	}

	@Test
	@WithMockCustomUser
	void holdSeat() throws Exception {
		Long seatId = 1L;
		Long userId = 1L;

		willDoNothing().given(holdSeatUseCase).holdSeat(seatId, userId);

		mockMvc.perform(post("/api/seats/{seatId}/hold", seatId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk());

		then(holdSeatUseCase).should().holdSeat(seatId, userId);
	}

	@Test
	@WithMockCustomUser
	void holdSeat_alreadyBooked_conflict() throws Exception {
		Long seatId = 2L;
		Long userId = 1L;

		willThrow(new SeatAlreadyBookedException()).given(holdSeatUseCase).holdSeat(seatId, userId);

		mockMvc.perform(post("/api/seats/{seatId}/hold", seatId)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest());
	}
}