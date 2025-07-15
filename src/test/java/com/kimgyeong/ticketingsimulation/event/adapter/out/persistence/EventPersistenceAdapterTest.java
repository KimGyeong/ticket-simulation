package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.EventEntity;
import com.kimgyeong.ticketingsimulation.event.domain.model.Event;

@ExtendWith(MockitoExtension.class)
class EventPersistenceAdapterTest {
	@Mock
	private JpaEventRepository repository;

	@InjectMocks
	private EventPersistenceAdapter adapter;

	@Test
	void save() {
		Event event = new Event(null, "이벤트 이름", "이벤트 설명", "이벤트이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(2), 100, 1L);
		EventEntity entity = new EventEntity(1L, event.title(), event.description(), event.imageUrl(),
			event.ticketingStartAt(), event.eventStartAt(), event.maxAttendees(), 1L);

		when(repository.save(any())).thenReturn(entity);

		Event result = adapter.save(event);

		assertThat(result.id()).isEqualTo(1L);
		assertThat(result.title()).isEqualTo(event.title());

		ArgumentCaptor<EventEntity> captor = ArgumentCaptor.forClass(EventEntity.class);
		verify(repository).save(captor.capture());

		EventEntity passedToRepo = captor.getValue();
		assertThat(passedToRepo.getTitle()).isEqualTo(event.title());
	}

	@Test
	void findAll() {
		EventEntity entity = new EventEntity(1L, "이벤트 이름", "이벤트 설명", "이벤트이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(2), 100, 1L);

		when(repository.findAll()).thenReturn(List.of(entity));

		List<Event> result = adapter.findAll();

		assertThat(result.get(0).id()).isEqualTo(entity.getId());
		assertThat(result.get(0).title()).isEqualTo(entity.getTitle());
	}

	@Test
	void findById() {
		EventEntity entity = new EventEntity(1L, "이벤트 이름", "이벤트 설명", "이벤트이미지", LocalDateTime.now().plusDays(1),
			LocalDateTime.now().plusDays(2), 100, 1L);

		when(repository.findById(anyLong())).thenReturn(Optional.of(entity));

		Optional<Event> result = adapter.findById(1L);

		assertThat(result.get().id()).isEqualTo(entity.getId());
		assertThat(result.get().title()).isEqualTo(entity.getTitle());
	}
}