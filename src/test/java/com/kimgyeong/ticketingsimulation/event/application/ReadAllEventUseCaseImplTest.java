package com.kimgyeong.ticketingsimulation.event.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

@ExtendWith(MockitoExtension.class)
class ReadAllEventUseCaseImplTest {
	@Mock
	EventRepositoryPort port;

	@InjectMocks
	ReadAllEventUseCaseImpl useCase;

	@Test
	void findAll() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1000L, 1L);
		given(port.findAll()).willReturn(List.of(event));

		List<Event> result = useCase.findAll();

		assertThat(result.get(0).id()).isEqualTo(event.id());
	}

}