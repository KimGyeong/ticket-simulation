package com.kimgyeong.ticketingsimulation.event.application;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAccessDeniedException;

@ExtendWith(MockitoExtension.class)
class DeleteEventUseCaseImplTest {
	@Mock
	private EventRepositoryPort port;

	@InjectMocks
	private DeleteEventUseCaseImpl useCase;

	@Test
	void deleteById() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1000L, 1L);
		given(port.findById(anyLong())).willReturn(Optional.of(event));

		useCase.deleteById(1L, 1L);

		then(port).should().deleteById(1L);
	}

	@Test
	void deleteById_whenUserIsNotOwner() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1000L, 1L);
		given(port.findById(anyLong())).willReturn(Optional.of(event));

		assertThrows(EventAccessDeniedException.class, () -> useCase.deleteById(1L, 2L));
	}
}