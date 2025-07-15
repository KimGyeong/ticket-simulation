package com.kimgyeong.ticketingsimulation.event.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.application.port.in.UpdateEventCommand;
import com.kimgyeong.ticketingsimulation.event.application.port.out.EventRepositoryPort;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;
import com.kimgyeong.ticketingsimulation.global.exception.EventAccessDeniedException;
import com.kimgyeong.ticketingsimulation.global.exception.EventModificationTimeExpiredException;

@ExtendWith(MockitoExtension.class)
class UpdateEventUseCaseImplTest {
	@Mock
	EventRepositoryPort port;

	@InjectMocks
	UpdateEventUseCaseImpl useCase;

	@Test
	void update() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1L);

		given(port.findById(anyLong())).willReturn(Optional.of(event));
		given(port.save(any(Event.class))).willAnswer(invocation -> invocation.getArgument(0));

		UpdateEventCommand updateEventCommand = new UpdateEventCommand("변경 테스트", "변경 설명", "테스트 이미지",
			LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), 100);

		Event result = useCase.updateEvent(1L, 1L, updateEventCommand);

		assertThat(result.id()).isEqualTo(event.id());
		assertThat(result.title()).isEqualTo(updateEventCommand.title());
		assertThat(result.description()).isEqualTo(updateEventCommand.description());
	}

	@Test
	void update_whenUserIsNotOwner() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(1), 100, 1L);

		given(port.findById(anyLong())).willReturn(Optional.of(event));

		UpdateEventCommand updateEventCommand = new UpdateEventCommand("변경 테스트", "변경 설명", "테스트 이미지",
			LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), 100);

		assertThatThrownBy(() -> useCase.updateEvent(2L, 1L, updateEventCommand))
			.isInstanceOf(EventAccessDeniedException.class);
	}

	@Test
	void update_whenTimeAfterTicketingOpen() {
		Event event = new Event(1L, "테스트 이벤트", "테스트 설명", "테스트 이미지", LocalDateTime.now().minusMinutes(10),
			LocalDateTime.now().plusDays(1), 100, 1L);

		given(port.findById(anyLong())).willReturn(Optional.of(event));

		UpdateEventCommand updateEventCommand = new UpdateEventCommand("변경 테스트", "변경 설명", "테스트 이미지",
			LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(1), 100);

		assertThatThrownBy(() -> useCase.updateEvent(1L, 1L, updateEventCommand))
			.isInstanceOf(EventModificationTimeExpiredException.class);
	}
}