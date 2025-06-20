package com.kimgyeong.ticketingsimulation.event.adapter.out.persistence;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.entity.SeatEntity;
import com.kimgyeong.ticketingsimulation.event.adapter.out.persistence.mapper.SeatEntityMapper;
import com.kimgyeong.ticketingsimulation.event.domain.model.Seat;
import com.kimgyeong.ticketingsimulation.event.domain.model.SeatStatus;

@ExtendWith(MockitoExtension.class)
class SeatPersistenceAdapterTest {
	@InjectMocks
	private SeatPersistenceAdapter adapter;

	@Mock
	private JpaSeatRepository repository;

	@Test
	void findAllByEventId() {
		SeatEntity seatEntity = new SeatEntity(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), 1L);
		List<SeatEntity> entities = List.of(seatEntity);
		given(repository.findAllByEventId(anyLong())).willReturn(entities);

		List<Seat> result = adapter.findAllByEventId(1L);

		assertThat(result).isNotEmpty();
		assertThat(result.get(0).eventId()).isEqualTo(1L);
	}

	@Test
	void save() {
		Seat seat = new Seat(1L, 1L, SeatStatus.AVAILABLE, 1, LocalDateTime.now(), 1L);
		SeatEntity entity = SeatEntityMapper.toEntity(seat);

		given(repository.save(any(SeatEntity.class))).willReturn(entity);

		adapter.save(seat);

		then(repository).should().save(any(SeatEntity.class));
	}
}