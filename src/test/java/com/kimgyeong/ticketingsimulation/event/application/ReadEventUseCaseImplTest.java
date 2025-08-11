package com.kimgyeong.ticketingsimulation.event.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.model.EventDetailResult;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.application.port.out.SeatRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

@ExtendWith(MockitoExtension.class)
class ReadEventUseCaseImplTest {
	@Mock
	EventRepositoryPort eventPort;

	@Mock
	SeatRepositoryPort seatPort;

	@InjectMocks
	ReadEventUseCaseImpl useCase;

	@Test
	void findById() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1000L, 1L);
		given(eventPort.findById(anyLong())).willReturn(Optional.of(event));
		given(seatPort.countAvailableByEventId(anyLong())).willReturn(5L);

		EventDetailResult result = useCase.findById(1L);

		assertThat(result.event().title()).isEqualTo(event.title());
		assertThat(result.availableSeatCount()).isEqualTo(5L);
	}
}